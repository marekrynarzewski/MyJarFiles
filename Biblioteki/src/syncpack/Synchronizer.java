package syncpack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;

import pomoc.Para;
import syncpack.ex.NonSatisfiedPrerequisitesException;

public class Synchronizer
{
	private Collection<DataSource> dataSources = new ArrayList<DataSource>();
	private Rule globalRule;
	private Map<DataSource, Rule> dataSourceRules = new HashMap<DataSource, Rule>();
	private Map<Entry<DataSource, String>, Rule> tableRules = new HashMap<Entry<DataSource, String>, Rule>();
	private Map<Entry<DataSource, String>, Collection<String>> syncedColumns = new HashMap<Entry<DataSource, String>, Collection<String>>();
	private Date	lastSynchronization;
	private String	lastModifiedColumn;
	private SimpleDateFormat formatLastModifiedColumn;
	private Map<DataSource, String> dataSourceLastModifiedColumns = new HashMap<DataSource, String>();
	private Map<Entry<DataSource, String>, String> tableLastModifiedColumns = new HashMap<Entry<DataSource, String>, String>();
	private Map<DataSource, Collection<String>> syncedTables = new HashMap<DataSource, Collection<String>>();
	public void addDataSource(DataSource ds)
	{
		dataSources.add(ds);
	}
	
	public void removeDataSource(DataSource ds)
	{
		dataSources.remove(ds);
	}
	public void setRule(Rule rule)
	{
		globalRule = rule;
	}
	public void setRule(DataSource ds, Rule rule)
	{
		dataSourceRules.put(ds, rule);
	}
	public void setRule(DataSource ds, String tableName, Rule rule)
	{
		Entry<DataSource, String> e = new Para<DataSource, String>(ds, tableName);
		tableRules.put(e, rule);
	}
	public void removeRule()
	{
		globalRule = null;
	}
	public void removeRule(DataSource ds)
	{
		dataSourceRules.remove(ds);
	}
	public void removeRule(DataSource ds, String tableName)
	{
		Entry<DataSource, String> e = new Para<DataSource, String>(ds, tableName);
		tableRules.remove(e);
	}
	public boolean markColumnAsSynced(DataSource ds, String tableName, String columnName)
	{
		if (columnExists(ds, tableName, columnName))
		{
			Entry<DataSource, String> entry = new Para<DataSource, String>(ds, tableName);
			Collection<String> value;
			if (syncedColumns.containsKey(entry))
			{
				value = syncedColumns.get(entry);
			}
			else
			{
				value = new ArrayList<String>();
			}
			value.add(columnName);
			syncedColumns.put(entry, value);
			return true;
		}
		return false;
	}
	public boolean markColumnsAsSynced(DataSource ds, String tableName, String[] columnNames)
	{
		for (String columnName : columnNames)
		{
			boolean result = markColumnAsSynced(ds, tableName, columnName);
			if (!result)
			{
				return false;
			}
		}
		return true;
	}
	public boolean markColumnAsUnsynced(DataSource ds, String tableName, String columnName)
	{
		if (columnExists(ds, tableName, columnName))
		{
			Entry<DataSource, String> entry = new Para<DataSource, String>(ds, tableName);
			if (syncedColumns.containsKey(entry))
			{
				Collection<String> value = syncedColumns.get(entry);
				value.remove(columnName);
				syncedColumns.put(entry, value);
			}
			return true;
		}
		return false;
	}
	public boolean markColumnsAsUnsynced(DataSource ds, String tableName, String[] columnNames)
	{
		for (String columnName : columnNames)
		{
			boolean result = markColumnAsUnsynced(ds, tableName, columnName);
			if (!result)
			{
				return false;
			}
		}
		return true;
	}
	
	public void setLastSynchronizationDate(Date lastSynchronization)
	{
		this.lastSynchronization = lastSynchronization;
	}
	
	public void setFormatLastModifiedColumn(String format) throws IllegalArgumentException
	{
		this.formatLastModifiedColumn = new SimpleDateFormat(format);
	}
	
	public void setLastModifiedColumn(String columnName)
	{
		lastModifiedColumn = columnName;
	}
	
	public void setLastModifiedColumn(DataSource ds, String columnName)
	{
		dataSourceLastModifiedColumns.put(ds, columnName);
	}
	
	public void setLastModifiedColumn(DataSource ds, String tableName, String columnName)
	{
		Entry<DataSource, String> key = new Para<DataSource, String>(ds, tableName);
		tableLastModifiedColumns.put(key, columnName);
	}
	
	public void removeLastModifiedColumn(DataSource ds)
	{
		dataSourceLastModifiedColumns.remove(ds);
	}
	
	public void removeLastModifiedColumn(DataSource ds, String tableName)
	{
		Entry<DataSource, String> key = new Para<DataSource, String>(ds, tableName);
		tableLastModifiedColumns.remove(key);
	}

	private boolean tableExists(DataSource ds, String tableName)
	{
		String[] tables = ds.tables();
		Arrays.sort(tables);
		int position = Arrays.binarySearch(tables, tableName);
		boolean result = (position != -1);
		return result;
	}
	private boolean columnExists(DataSource ds, String tableName, String columnName)
	{
		String[] columns = ds.columns(tableName);
		Arrays.sort(columns);
		int position = Arrays.binarySearch(columns, columnName);
		boolean result = (position != -1);
		return result;
	}
	public boolean markTableAsSynced(DataSource ds, String tableName)
	{
		if (tableExists(ds, tableName))
		{
			Collection<String> tables;
			if (syncedTables.containsKey(ds))
			{
				tables = syncedTables.get(ds);
			}
			else
			{
				tables = new ArrayList<String>();
			}
			tables.add(tableName);
			syncedTables.put(ds, tables);
			return true;
		}
		return false;
	}
	public boolean markTableAsUnsynced(DataSource ds, String tableName)
	{
		if (tableExists(ds, tableName))
		{
			Collection<String> tables;
			if (syncedTables.containsKey(ds))
			{
				tables = syncedTables.get(ds);
				tables.remove(tableName);
				syncedTables.put(ds, tables);
			}
			
			return true;
		}
		return false;
	}
	public boolean markTablesAsSynced(DataSource ds, String[] tableNames)
	{
		for (String tableName : tableNames)
		{
			boolean result = markTableAsSynced(ds, tableName);
			if (!result)
			{
				return true;
			}
		}
		return false;
	}
	public boolean markTablesAsUnsynced(DataSource ds, String[] tableNames)
	{
		for (String tableName : tableNames)
		{
			boolean result = markTableAsUnsynced(ds, tableName);
			if (!result)
			{
				return true;
			}
		}
		return false;
	}
	public void synchronize() throws NonSatisfiedPrerequisitesException
	{
		Entry<Boolean, ArrayList<String>> entry;
		entry = checkPrerequisites();
		if (!entry.getKey())
		{
			throw new NonSatisfiedPrerequisitesException(entry.getValue());
		}
		Collection<Entry<DataSource, DataSource>> dataSources;
		dataSources = getPairsToSynchronization();
		for (Entry<DataSource, DataSource> dsd : dataSources)
		{
			DataSource ds1 = dsd.getKey();
			DataSource ds2 = dsd.getValue();
			dataFlow(ds1, ds2);
			dataFlow(ds2, ds1);
		}
	}
	
	private long dataFlow(DataSource source, DataSource destination)
	{
		Collection<String> tables = syncedTables.get(source);
		long count = 0;
		for (String table : tables)
		{
			count += dataFlow(source, table, destination);
		}
		return count;
	}

	private long dataFlow(DataSource source, String table, DataSource destination)
	{
		//Collection<Map<String, Object>> rows = source.
		return 0;
	}

	private Collection<Entry<DataSource, DataSource>> getPairsToSynchronization()
	{
		Collection<Entry<DataSource, DataSource>> result;
		result = new ArrayList<Entry<DataSource, DataSource>>();
		for (DataSource ds1 : dataSources)
		{
			for (DataSource ds2 : dataSources)
			{
				if (ds1.id > ds2.id)
				{
					Entry<DataSource, DataSource> entry;
					entry = new Para<DataSource, DataSource>(ds1, ds2);
					result.add(entry);
				}
			}
		}
		return null;
	}

	private Entry<Boolean, ArrayList<String>> checkPrerequisites()
	{
		boolean b1 = (this.dataSources.size() >= 2);
		boolean b2 = (lastSynchronization != null);
		boolean b3 = (formatLastModifiedColumn != null);
		boolean b4 = !syncedColumns.isEmpty();
		boolean b5 = (b1 && b2 && b3 && b4);
		Entry<Boolean, ArrayList<String>> result;
		result = new Para<Boolean, ArrayList<String>>(b5, null);
		if (!b5)
		{
			ArrayList<String> value = new ArrayList<String>();
			if (!b1)
			{
				value.add("number of dataSources is less than required to synchronization");
			}
			if (!b2)
			{
				value.add("lastSynchronization is not defined");
			}
			if (!b3)
			{
				value.add("formatLastModifiedColumn is not defined");
			}
			if (!b4)
			{
				value.add("No columns are not marked for synchronization");
			}
			result.setValue(value);
		}
		return result;
	}
	
}
