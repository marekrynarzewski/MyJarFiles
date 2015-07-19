package mapy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A map which store a user defined <br />
 * implementation of interface Entry&lt;K, V&gt;
 * @author Marek
 *
 * @param <K> key of Entry&lt;K, V&gt;
 * @param <V> value of Entry&lt;K, V&gt;
 */
public class CustomEntryMap<K, V> implements Map<K, V>
{
	private Map<K, Entry<K, V>> map = new HashMap<>();
	private EntryFactory<K, V> factory;
	
	/**
	 * default constructor of CustomEntryMap
	 */
	public CustomEntryMap()
	{
		
	}
	
	/**
	 * A constructor which set an EntryFactory&lt;K, V&gt; <br />
	 *  by user defined used in {@code java.util.Map.put(K key, V value);}
	 * @param factory
	 */
	public CustomEntryMap(EntryFactory<K, V> factory)
	{
		this.factory = factory;
	}
	
	/**
	 * A constructor which allows initialize <br />
	 * a CustomEntryMap from another map
	 * @param map a map which a entries <br />
	 * will be imported to CustomEntryMap
	 */
	public CustomEntryMap(Map<K, V> map)
	{
		for (Entry<K, V> entry : map.entrySet())
		{
			this.put(entry);
		}
	}

	/**
	 * get a factory of entries
	 * @return a object which implemented EntryFactory&lt;K, V&gt; interface
	 */
	public EntryFactory<K, V> getFactory()
	{
		return this.factory;
	}
	
	/**
	 * clear map
	 */
	@Override
	public void clear()
	{
		this.map.clear();
	}

	/**
	 * check if object as key contains in map
	 */
	@Override
	public boolean containsKey(Object key)
	{
		return this.map.containsKey(key);
	}

	/**
	 * check if object as value contains in map
	 */
	@Override
	public boolean containsValue(Object value)
	{
		return this.map.containsValue(value);
	}

	/**
	 * get a set of entries stored in map
	 */
	@Override
	public Set<Entry<K, V>> entrySet()
	{
		Set<Entry<K, V>> result = new HashSet<>();
		for (Entry<K, V> entry : this.map.values())
		{
			result.add(entry);
		}
		return result;
	}

	/**
	 * get associated object as value by key
	 */
	@Override
	public V get(Object key)
	{
		Entry<K, V> entry = this.map.get(key);
		V value = entry.getValue();
		return value;
	}

	/**
	 * check if map is empty
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}

	/**
	 * get a set of keys from map
	 */
	@Override
	public Set<K> keySet()
	{
		return this.map.keySet();
	}

	/**
	 * A method which stores a key associate with value <br />
	 * It is effectively if factory of entries are set. 
	 */
	@Override
	public V put(K key, V value)
	{
		if (this.factory != null)
		{
			Entry<K, V> entry = this.factory.produce(key, value);
			this.put(entry);
		}
		return value;
	}
	
	/**
	 * A method allow you to put your implemented Entry&lt;K, V&gt;
	 * @param entry a entry to put into map
	 */
	public void put(Entry<K, V> entry)
	{
		this.map.put(entry.getKey(), entry);
	}
	

	/**
	 * allows to import entries from otherMap
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> otherMap)
	{
		if (this.factory == null)
		{
			return;
		}
		for (Entry<? extends K, ? extends V> entry : otherMap.entrySet())
		{
			K key = entry.getKey();
			V value = entry.getValue();
			Entry<K, V> newEntry = this.factory.produce(key, value);
			this.put(newEntry);
		}
	}

	/**
	 * remove object associate with given key
	 */
	@Override
	public V remove(Object key)
	{
		Entry<K, V> entry = this.map.remove(key);
		V value = entry.getValue();
		return value;
	}

	/**
	 * get size of map
	 */
	@Override
	public int size()
	{
		return this.map.size();
	}

	/**
	 * get values of map
	 */
	@Override
	public Collection<V> values()
	{
		Collection<V> result = new ArrayList<>();
		for (Entry<K, Entry<K, V>> pair : this.map.entrySet())
		{
			Entry<K, V> entry = pair.getValue();
			V value = entry.getValue();
			result.add(value);
		}
		return result;
	}
	
	/**
	 * get entry associate with given key
	 * @param key a key
	 * @return a your entry
	 */
	public Entry<K, V> getEntry(K key)
	{
		return this.map.get(key);
	}

}
