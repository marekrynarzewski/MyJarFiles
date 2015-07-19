package model.eng;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import kolekcje.Kolekcje;

import com.sun.xml.internal.ws.util.StringUtils;



import pomoc.Tablice;
import mapy.Maper;
import mapy.Mapy;
import model.eng.FieldPredicates.PublicPredicate;
import model.eng.FieldPredicates.StaticPredicate;
import model.eng.MethodPredicates.FunctionPredicate;
import model.eng.MethodPredicates.MethodNamePredicate;
import model.eng.MethodPredicates.MethodNthArgumentsPredicate;
import model.eng.MethodPredicates.PreffixNamePredicate;

public abstract class Model
{
	/**
	 * represents uuid for this model in database
	 */
	public final UUID uuid;

	/**
	 * It must be called first to save and read from database by class extended from Model
	 * @param database instance of database class (cannot be null)
	 * @return true if database is not null, false otherwise
	 */
	public final static boolean setDatabase(Database database)
	{
		boolean result;
		
		result = false;
		if (database != null)
		{
			Model.database = database;
			Model.database.testConnection();
			result = true;
		}
		
		return result;
	}

	/**
	 * creates new Model instance with random uuid and marked isNew as true
	 */
	public Model()
	{
		this.uuid = UUID.randomUUID();
		this.isNew = true;

	}

	/**
	 * restore Model instance from database by given uuid (if not null)
	 * @param uuid uuid of object
	 * @throws DatabaseException if any exception by database class has been  thrown
	 */
	public Model(UUID uuid) throws DatabaseException
	{
		this.uuid = uuid;
		this.isNew = false;
		if (this.uuid != null)
		{
			this.data = database.getMap(this.getQuery());
			this.load();
		}
	}
	
	/**
	 * saves an object to database
	 * @throws DatabaseException if any exception by database class has been  thrown
	 */
	public final void save() throws DatabaseException
	{
		
		this.prepareToSave();
		if (this.isNew)
		{
			this.executeIfNew();
			this.isNew = false;
		}
		else if (this.isModified())
		{
			this.executeIfModified();
			this.modifiedValuesFieldsName.clear();
			this.modifiedFieldsFromGetters.clear();
			this.modifiedFieldsWithNames.clear();
			this.isModified = false;
		}
	}

	/**
	 * delete an object from database
	 * @return rows affected in database
	 * @throws DatabaseException  if any exception by database class has been thrown
	 */
	public final long delete() throws DatabaseException
	{
		String table, condition;
		long result;
		
		table = this.getDatabaseTable();
		condition = this.getCondition();
		result = database.delete(table, condition);
		
		return result;
	}

	/**
	 * an abstract field to manipulate in database 
	 */
	protected static Database database;
	
	protected static final Map<UUID, Model> cache = new HashMap<>();
	
	/**
	 * showUUID means that the representation of the human-readable object to be displayed using a String  
	 */
	protected boolean showUUID = false;
	
	/**
	 * gets a prefix of getter method
	 * @return
	 */
	protected String getGetterMethodName()
	{
		return getterMethodName;
	}

	/**
	 * stores a modified values by fields 
	 */
	protected final Collection<String> modifiedValuesFieldsName = new HashSet<>();

	/**
	 * stores a modified values by getters
	 */
	protected final Collection<String> modifiedFieldsFromGetters = new HashSet<>();

	/**
	 * stores a pairs (field name with value) as modified
	 */
	protected final Map<String, Object> modifiedFieldsWithNames = new HashMap<>();

	/**
	 * allows to set fields satisfied by {@code this.getGetterMethodPredicates} to raw values
	 * @param data a map of field names with values
	 * @return the same object
	 * @throws NoSuchFieldException if any field name isn't correct
	 * @throws SecurityException if any security exception has been thrown
	 * @throws IllegalArgumentException if any values hasn't correct type
	 * @throws IllegalAccessException if any illegal access exception has been thrown
	 */
	protected final Model setFields(Map<String, Object> data)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException
	{
		Set<Entry<String, Object>> setOfData;
		String fieldName;
		Object fieldValue;
	
		setOfData = data.entrySet();
		for (Entry<String, Object> entry : setOfData)
		{
			fieldName = entry.getKey();
			fieldValue = entry.getValue();
			this.setField(fieldName, fieldValue);
		}
		
		return this;
	}

	protected final Model setField(String name, Object value) 
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException
	{
		Collection<Field> fields;
		Map<String, Field> map;
		Field field;
		
		fields = this.getFields();
		map = Mapy.mapujPrzez(fields, fieldMaper);
		field = map.get(name);
		if (field == null)
		{
			throw new NoSuchFieldException(name);
		}
		field.set(this, value);
		
		return this;
	}

	protected final Model setMethod(String name, Object value) 
			throws SecurityException, 
			IllegalArgumentException, IllegalAccessException, 
			NoSuchMethodException, InvocationTargetException
	{
		Collection<Method> methods;
		Map<String, Method> map;
		Method method;
		
		methods = this.getMethods(MethodsType.Setter);
		map = Mapy.mapujPrzez(methods, methodMaper);
		method = map.get(name);
		if (method == null)
		{
			throw new NoSuchMethodException(name);
		}
		method.invoke(this, value);
		
		return this;
	}

	protected final Model setMethods(Map<String, Object> data) 
			throws SecurityException,
			IllegalArgumentException, IllegalAccessException, 
			NoSuchMethodException, InvocationTargetException
	{
		Set<Entry<String, Object>> setOfData; 
		String fieldName;
		Object fieldValue;
	
		setOfData = data.entrySet();
		for (Entry<String, Object> entry : setOfData)
		{
			fieldName = entry.getKey();
			fieldValue = entry.getValue();
			this.setMethod(fieldName, fieldValue);
		}
		
		return this;
	}

	protected final boolean isNew()
	{
		return this.isNew;
	}

	protected final Map<String, String> getModifiedData()
	{
		Map<String, Object> rawValuesFromFields, rawValueFromFieldsWithValues, combination;
		Map<String, String> result;
		
		rawValuesFromFields = this.getRawValuesFromFields();
		rawValueFromFieldsWithValues = this.getRawValuesFromFieldsWithValues();
		combination = Mapy.polacz(rawValueFromFieldsWithValues, rawValuesFromFields);
		result = convertToDatabaseFormat(combination);
	
		return result;
	}

	private final Map<String, Object> getRawValuesFromFieldsWithValues()
	{
		Set<Entry<String, Object>> modifiedFieldsWithValuesSet;
		Map<String, Object> result;
		String fieldName;
		Object fieldValue;
		
		modifiedFieldsWithValuesSet = this.modifiedFieldsWithNames.entrySet();
		result = new HashMap<>();
		for (Entry<String, Object> entry : modifiedFieldsWithValuesSet)
		{
			fieldName = entry.getKey();
			fieldValue = entry.getValue();
			result.put(fieldName, fieldValue);
		}
		
		return result;
		
	}

	private final Map<String, Object> getRawValuesFromFields()
	{
		Map<String, Object> result;
		Object fieldValue;
		
		result = new HashMap<>();
		for (String fieldName : this.modifiedValuesFieldsName)
		{
			try
			{
				fieldValue = this.getRawValueFromMethod(fieldName);
				result.put(fieldName, fieldValue);
			}
			catch (NoSuchFieldException | IllegalAccessException |
				IllegalArgumentException | InvocationTargetException e)
			{
			}
			
		}
		
		return result;
	}

	private final Object getRawValueFromMethod(String fieldName) 
			throws NoSuchFieldException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		Collection<Method> methods;
		Map<String, Method> map;
		Method method;
		String methodName;
		Object value;
		
		methods = this.getMethods(MethodsType.Getter);
		map = Mapy.mapujPrzez(methods, methodMaper);
		methodName = this.getGetterMethodName(fieldName);
		method = map.get(methodName);
		if (method == null)
		{
			throw new NoSuchFieldException(methodName);
		}
		value = method.invoke(this);
		
		return value;
	}

	private final String getGetterMethodName(String name)
	{
		String fieldName, result;
		
		fieldName = this.mapDatabaseColumnToField(name);
		result = getGetterMethodName() + fieldName;
		
		return result;
	}

	protected final Map<String, String> getNewData()
	{
		Map<String, Object> rawValues, filteredValues;
		Collection<String> excludedFields;
		Map<String, String> result;
	
		rawValues = this.getRawValuesFromFieldsAndMethodsSatisfiedByPredicates();
		excludedFields = this.getExcludedFields();
		filteredValues = Mapy.wykluczKlucze(rawValues, excludedFields);
		result = convertToDatabaseFormat(filteredValues);
		this.modifyNewData(result);
		
		return result;
	}

	/**
	 * dane z bazy danych
	 */
	protected Map<String, String> data;
	protected void executeIfModified() throws DatabaseException
	{
		String table;
		Map<String, String> data;
		String condition;
		
		table = this.getDatabaseTable();
		data = this.getModifiedData();
		condition = this.getCondition();
		database.update(table, data, condition);
		
	}

	/**
	 * wykonuje te funkcje jesli ma wykonac jakies niestandardowe operacje przy wstawieniu do bazy
	 * @throws WyjatekBazyDanych rzuca w wyniku jakiegokolwiek bledu bazy
	 */
	protected void executeIfNew() throws DatabaseException
	{
		String table;
		Map<String, String> data;
		
		table = this.getDatabaseTable();
		data = this.getNewData();
		database.insert(table, data);
	}

	protected Collection<String> getExcludedFields()
	{
		Collection<String> result;
		
		result = new ArrayList<>();
		result.add("class");
		
		return result;
	}


	protected FieldPredicates getFieldPredicates()
	{
		FieldPredicates result;
		
		result = new FieldPredicates();
		result.add(new PublicPredicate(true));
		result.add(new StaticPredicate(false));
		
		return result;
	}

	protected String getSetterMethodName()
	{
		return setterMethodName;
	}

	protected MethodPredicates getSetterMethodsPredicates()
	{
		MethodNamePredicate namePredicate;
		MethodNthArgumentsPredicate is1ArgumentPredicate;
		PreffixNamePredicate preffixNamePredicate;
		String preffixName;
		Predicate<Method> publicPredicate;
		MethodPredicates result;
		
		is1ArgumentPredicate = new MethodNthArgumentsPredicate(1);
		preffixName = this.getSetterMethodName();
		preffixNamePredicate = new PreffixNamePredicate(preffixName);
		namePredicate = new MethodNamePredicate(preffixNamePredicate);
		publicPredicate = new MethodPredicates.PublicPredicate(true);
		result = new MethodPredicates();
		result.add(is1ArgumentPredicate);
		result.add(publicPredicate);
		result.add(namePredicate);
		
		return result;
	}

	protected MethodPredicates getGetterMethodsPredicates()
	{
		Predicate<Method> isFunction, is0arg, startsWithGetpm, publicPredicate;
		Predicate<String> startsWithGetps;
		String getterMethodName;
		MethodPredicates result;
		
		isFunction = new FunctionPredicate();
		is0arg = new MethodNthArgumentsPredicate(0);
		getterMethodName = this.getGetterMethodName();
		startsWithGetps = new PreffixNamePredicate(getterMethodName);
		startsWithGetpm = new MethodNamePredicate(startsWithGetps);
		publicPredicate = new MethodPredicates.PublicPredicate(true);
		result = new MethodPredicates();
		result.add(publicPredicate);
		result.add(is0arg);
		result.add(isFunction);
		result.add(startsWithGetpm);
		
		return result;
	}

	protected Map<String, String> getMapped()
	{
		Map<String, String> result;
		
		result = new HashMap<>();
		
		return result;
	}

	protected abstract String getQuery();

	protected abstract String getCondition();

	protected abstract void load() throws DatabaseException;

	protected abstract String getDatabaseTable();

	protected abstract String mapFieldToDatabaseColumn(String fieldName);

	protected abstract String mapDatabaseColumnToField(String columnName);

	protected abstract void prepareToSave();

	protected abstract void modifyNewData(Map<String, String> currentData);

	protected abstract void modifyModifiedData(Map<String, String> currentData);
	
	protected abstract String show();

	protected boolean isNew;
	
	protected boolean isModified;
	
	private static final String getterMethodName = "get";
	
	private static final String setterMethodName = "set";
	
	/*
	 * private static MethodPredicates getterMethodPredicates;
	
	
	private static MethodPredicates setterMethodPredicates;
	*/
	
	//private static FieldPredicates fieldPredicates;
	
	private final static Maper<String, Field> fieldMaper = new Maper<String, Field>(){
		@Override
		public String mapuj(Field field)
		{
			return field.getName();
		}
	};
	
	

	private final Maper<String, Method> methodMaper = new Maper<String, Method>(){
		@Override
		public String mapuj(Method method)
		{
			return getFieldNameFromGetter(method);
		}
	};
	
	private static final Map<String, String> convertToDatabaseFormat(Map<String, Object> data)
	{
		Map<String, String> result;
		Set<Entry<String, Object>> setOfData;
		String key, stringValue;
		Object rawValue;
		
		result = new HashMap<>();
		setOfData = data.entrySet();
		for (Entry<String, Object> entry : setOfData)
		{
			key = entry.getKey();
			rawValue = entry.getValue();
			stringValue = rawValue.toString();
			result.put(key, stringValue);
		}
		
		return result;
	}

	private final String getFieldNameFromGetter(Method method)
	{
		String methodName, getterNameMethod, result;
		int position, len;
		
		methodName = method.getName();
		getterNameMethod = getGetterMethodName();
		len = getterNameMethod.length();
		position = methodName.indexOf(getterNameMethod)+len;
		result = methodName.substring(position);
		result = StringUtils.decapitalize(result);
		
		return result;
		
	}

	private final boolean isModified()
	{
		boolean isFieldsModified, isFieldWithValueModified, isModifiedFiledsFromGetters,
		both, result;
		
		isFieldsModified = this.modifiedValuesFieldsName.isEmpty();
		isFieldWithValueModified = this.modifiedFieldsWithNames.isEmpty();
		isModifiedFiledsFromGetters = this.modifiedFieldsFromGetters.isEmpty();
		both = !isFieldsModified && !isFieldWithValueModified && !isModifiedFiledsFromGetters;
		
		result = this.isModified || both;
		
		return result;
	}

	private final Map<String, Object> getRawValuesFromFieldsAndMethodsSatisfiedByPredicates()
	{
		Map<String, Object> rawValuesFromFields, rawValuesFromMethods, result;
		
		rawValuesFromFields = this.getRawValuesFromFieldsSatisfiedByPredicate();
		rawValuesFromMethods = this.getRawValuesFromMethodsSatisfiedByPredicate();
		result = Mapy.polacz(rawValuesFromFields, rawValuesFromMethods);
		
		return result;
	}

	private final Map<String, Object> getRawValuesFromMethodsSatisfiedByPredicate()
	{
		Map<String, Object> result;
		Collection<Method> methods;
		String fieldName;
		Object methodValue;
		
		result = new HashMap<>();
		methods = this.getMethods(MethodsType.Getter);
		for (Method method : methods)
		{
			fieldName = getFieldNameFromGetter(method);
			try
			{
				methodValue = this.getRawValue(method);
				result.put(fieldName, methodValue);
			}
			catch (IllegalArgumentException | IllegalAccessException
				| SecurityException | InvocationTargetException e)
			{
			}
		}
		
		return result;
	}

	private final Map<String, Object> getRawValuesFromFieldsSatisfiedByPredicate()
	{
		Map<String, Object> data;
		Collection<Field> fields;
		String fieldName;
		Object fieldValue;
		
		data = new HashMap<>();
		fields = this.getFields();
		for (Field field : fields)
		{
			fieldName = field.getName();
			try
			{
				fieldValue = this.getRawValue(field);
				data.put(fieldName, fieldValue);
			}
			catch (IllegalArgumentException 
				| IllegalAccessException
				| SecurityException e)
			{
			}
		}
		
		return data;
	}

	private final Object getRawValue(Field field)
			throws IllegalArgumentException, IllegalAccessException
	{
		Object fieldValue;
		
		fieldValue = field.get(this);
		
		return fieldValue;
	}

	private final Collection<Field> getFields()
	{
		Class<?> type;
		Field[] fields;
		Collection<Field> result;
		FieldPredicates fieldPredicates;
		
		type = this.getClass();
		fields = type.getFields();
		result = Arrays.asList(fields);
		fieldPredicates = getFieldPredicates();
		result = Kolekcje.filtruj(result, fieldPredicates);
		return result;
	}
	
	private final Collection<Method> getMethods(MethodsType methodType)
	{
		Class<?> type;
		Method[] methods;
		Collection<Method> result;
		MethodPredicates methodPredicates;
		
		type = this.getClass();
		methods = type.getMethods();
		result = Arrays.asList(methods);
		methodPredicates = this.getPredicatesByType(methodType);
		result = Kolekcje.filtruj(result, methodPredicates);
		
		return result;
	}
	
	private final MethodPredicates getPredicatesByType(MethodsType methodType)
	{
		MethodPredicates result;
		
		if (methodType == MethodsType.Getter)
		{
			result = this.getGetterMethodsPredicates();
		}
		else
		{
			result = this.getSetterMethodsPredicates();
		}
		
		return result;
	}

	private final Object getRawValue(Method metoda) 
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		Object methodValue;
		
		methodValue = metoda.invoke(this);
		
		return methodValue;
	}
	
	
	public final String toString()
	{
		String result;
		
		if (this.uuid != null && this.showUUID)
		{
			result = this.uuid.toString();
		}
		else
		{
			result = this.show();
		}
		
		return result;
	}
	
	public final <K, V> Map<K, V> getMap()
	{
		Map<K, V> helper;
		ObservableMap<K, V> result;
		
		helper = new HashMap<>();
 		result = FXCollections.observableMap(helper);
 		result.addListener(new MapChangeListener<K, V>(){
			public void onChanged(Change<? extends K, ? extends V> change)
			{
				// TODO Auto-generated method stub
				
			}
 			
 		});
 		
 		return result;
	}
	
	public final <T> Collection<T> getCollection()
	{
		List<T> helper;
		ObservableList<T> result;
		
		helper = new ArrayList<>();
 		result = FXCollections.observableList(helper);
 		result.addListener(new ListChangeListener<T>(){

			@Override
			public void onChanged(Change<? extends T> arg0)
			{
				// TODO Auto-generated method stub
				
			}
 			
 		});
 		
 		return result;
	}
}
