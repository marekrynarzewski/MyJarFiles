package adnotacje;

import io.Plik;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import kolekcje.Kolekcje;
import pomoc.Para;

//import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.AbstractMap;

import pomoc.Tablice;
import text.TextUtils;

public class Adnotacje
{
	public static Annotation[] uzyskajAdnotacje(Class<?> klasa)
	{
		Annotation[] da, a, result;
		
		
		da = klasa.getDeclaredAnnotations();
		a = klasa.getAnnotations();
		result = concat(a, da, Annotation.class);
		return result;
	}
	
	public static Annotation[] getAnnotations(Class<?> clazz)
	{
		Field[] fields;
		Method[] methods;
		Annotation[] tmp, result;
		
		result = new Annotation[0];
		fields = getAllFields(clazz);
	    for (Field field : fields)
	    {
	    	System.out.println(field);
	        tmp  = field.getDeclaredAnnotations();
	        result = concat(result, tmp, Annotation.class);
	    }
	    
	    methods = getAllMethods(clazz);
	    for (Method method : methods)
	    {
	    	System.out.println(method);
	        tmp  = method.getDeclaredAnnotations();
	        result = concat(result, tmp, Annotation.class);
	    }
	    
	    return result;
	    
	}
	
	@SuppressWarnings ("unchecked")
	public static <T> T[] concat(T[] a, T[] b, Class<T> clazz)
	{
	   int aLen, bLen, newSize;
	   T[] result;
	   
	   aLen = a.length;
	   bLen = b.length;
	   newSize = aLen+bLen;
	   result =(T[]) Array.newInstance(clazz,newSize);
	   System.arraycopy(a, 0, result, 0, aLen);
	   System.arraycopy(b, 0, result, aLen, bLen);
	   
	   return result;
	}
	
	public static Field[] getAllFields(Class<?> clazz)
	{
		Field[] tmp, result;
		
		result = new Field[0];
		tmp = clazz.getDeclaredFields();
		result = concat(result, tmp, Field.class);
		tmp = clazz.getFields();
		result = concat(result, tmp, Field.class);
		
		return result;
	}
	
	public static Method[] getAllMethods(Class<?> clazz)
	{
		Method[] tmp, result;
		
		result = new Method[0];
		tmp = clazz.getDeclaredMethods();
		result = concat(result, tmp, Method.class);
		tmp = clazz.getMethods();
		result = concat(result, tmp, Method.class);
		
		return result;
	}
	
	public static Collection<Method> uzyskajMetodyOAdnotacji(Class<?> klasa, Class<? extends Annotation> adnotacja)
	{
		Method[] m, dm, zbior;
		Collection <Method> wynik;
		
		m = klasa.getMethods();
		dm = klasa.getDeclaredMethods();
		zbior = concat(m, dm, Method.class);
		wynik = new HashSet<Method>();
		for (Method metoda : zbior)
		{
			if (metoda.isAnnotationPresent(adnotacja))
			{
				wynik.add(metoda);
			}
		}
		
		return wynik;
	}
	
	public static Map<Method, Annotation> uzyskajMetodyIAdnotacjeOAdnotacji(
			Class<?> klasa, Class<? extends Annotation> klasaAdnotacji)
	{
		
		Method[] metody;
		Annotation adnotacja;
		Map<Method, Annotation> wynik;
		
		metody = klasa.getDeclaredMethods();
		wynik = new HashMap<>();
		for (Method metoda : metody)
		{
			if (metoda.isAnnotationPresent(klasaAdnotacji))
			{
				adnotacja = metoda.getDeclaredAnnotation(klasaAdnotacji);
				wynik.put(metoda, adnotacja);
			}
		}
		
		return wynik;		
	}
	
	@SuppressWarnings ("unchecked")
	public static Entry<String, Object> getFieldNameFromAnnotationAndGetterMethod(Method m, Object o) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		String fieldName;
		Object fieldValue;
		Getter getter;
		Entry<String, Object> wynik;
		
		getter = m.getAnnotation(Getter.class);
		fieldName = getter.field();
		fieldValue = m.invoke(o);
		wynik = new Para<>(fieldName, fieldValue);
		
		return wynik;
	}
	
	public static class KreatorAdnotacji
	{
		private final String nazwa;
		private final Collection<ElementType> rodzaje = new ArrayList<>();
		private static final String nazwaKlasy = "ElementType.";
		private String nazwaPakietu;
		private final Map<Class<?>, String> mapa = new HashMap<>();
		
		public KreatorAdnotacji(String nazwa)
		{
			this.nazwa = nazwa;
		}
		
		public KreatorAdnotacji ustawRodzaje(ElementType[] rodzaje)
		{
			this.rodzaje.clear();
			this.rodzaje.addAll(Kolekcje.toList(rodzaje));
			return this;
		}
		
		public KreatorAdnotacji ustawPakiet(String nazwaPakietu)
		{
			this.nazwaPakietu = nazwaPakietu;
			return this;
		}
		
		public KreatorAdnotacji dodajSygnatureMetody(Class<?> typZwracany, String nazwaMetody)
		{
			this.mapa.put(typZwracany, nazwaMetody);
			return this;
		}
		
		private final List<String> uzyskajImporty()
		{
			List<String> wynik;
			
			wynik = new ArrayList<>();
			wynik.add("import java.lang.annotation.ElementType;");
			wynik.add("import java.lang.annotation.Retention;");
			wynik.add("import java.lang.annotation.RetentionPolicy;");
			wynik.add("import java.lang.annotation.Target;");
			wynik.add("");
			
			return wynik;

		}
		
		private final List<String> uzyskajPakiet()
		{
			List<String> wynik;
			
			wynik = new ArrayList<>();
			wynik.add("package "+this.nazwaPakietu+";");
			wynik.add("");
			
			return wynik;

		}
		
		private final List<String> generujAdnotacje()
		{
			List<String> wynik;
			
			wynik = new ArrayList<>();
			wynik.add(this.uzyskajRodzaje());
			wynik.add("@Retention(RetentionPolicy.RUNTIME)");
			wynik.add("public @interface "+this.nazwa);
			wynik.add("{");
			wynik.addAll(this.uzyskajMetody());
			wynik.add("}");
			wynik.add("");
			
			return wynik;

		}

		private final List<String> uzyskajMetody()
		{
			Set<Entry<Class<?>, String>> zbior;
			Class<?> rodzaj;
			String nazwaRodzaju, nazwaMetody;
			List<String> wynik;
				
			wynik = new ArrayList<>();
			zbior = this.mapa.entrySet();
			for (Entry<Class<?>, String> e : zbior)
			{
				rodzaj = e.getKey();
				nazwaRodzaju = rodzaj.getName();
				nazwaMetody = e.getValue();
				wynik.add("\t"+nazwaRodzaju+" "+nazwaMetody+"();");
			}
			
			return wynik;
		}

		private final String uzyskajRodzaje()
		{
			String wynik;
			
			wynik = "@Target(";
			wynik += this.implode();
			wynik += ")";
			
			return wynik;
		}
		
		private final String implode()
		{
			Collection<String> temp;
			String stringTemp;
			String wynik;
			
			temp = new ArrayList<>();
			for (ElementType rodzaj : this.rodzaje)
			{
				temp.add(nazwaKlasy+rodzaj);
			}
			temp = TextUtils.addToAnyItem(nazwaKlasy, this.rodzaje);
			stringTemp = temp.toString();
			stringTemp = stringTemp.replace("[", "{");
			stringTemp = stringTemp.replace("]", "}");
			wynik = stringTemp;
			
			return wynik;
		}

		private List<String> generujTresc()
		{
			List<String> wynik;
			
			wynik = new ArrayList<>();
			wynik.addAll(this.uzyskajPakiet());
			wynik.addAll(this.uzyskajImporty());
			wynik.addAll(this.generujAdnotacje());
			
			return wynik;
		}
		
		public long zapisz() throws IOException
		{
			List<String> plik;
			String nazwaPliku, sciezka;
			long wynik;
			
			nazwaPliku = this.nazwa+".java";
			plik = this.generujTresc();
			wynik = Plik.zapisz(nazwaPliku, plik);
			
			return wynik;
		}
	}
}
