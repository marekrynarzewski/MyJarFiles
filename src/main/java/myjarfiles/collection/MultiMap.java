package myjarfiles.collection;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

public class MultiMap<T1, T2> implements AbstractMultiMap<T1, T2>
{
	private Map<T1, Vector<T2>> map = new HashMap<T1, Vector<T2>>();
	
	public T2 put(T1 key, T2 value)
	{
		if (!map.containsKey(key))
		{
			Vector<T2> newMap = new Vector<T2>();
			newMap.add(value);
			map.put(key, newMap);
			return value;
		}
		else
		{
			Vector<T2> newMap = map.get(key);
			if (newMap.size() < Integer.MAX_VALUE)
			{
				newMap.add(value);
				map.put(key, newMap);
				return value;
			}
			else
			{
				return null;
			}
		}
	}
	
	@Override
	public void clear()
	{
		map.clear();	
	}
	
	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}
	
	@Override
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<T1, T2>> entrySet()
	{
		return portionEntrySet(firstKey(), lastKey());
	}
	
	private T1 lastKey()
	{
		NavigableMap<T1, Vector<T2>> map = new TreeMap<T1, Vector<T2>>(this.map);
		Entry<T1, Vector<T2>> lastEntry = map.lastEntry();
		return lastEntry.getKey();
	}
	
	private T1 firstKey()
	{
		NavigableMap<T1, Vector<T2>> map = new TreeMap<T1, Vector<T2>>(this.map);
		Entry<T1, Vector<T2>> firstEntry = map.firstEntry();
		return firstEntry.getKey();
	}
	
	public  Set<java.util.Map.Entry<T1, T2>> portionEntrySet(T1 odKlucza, T1 doKlucza)
	{
		TreeMap<T1, Vector<T2>> drzewo = new TreeMap<T1, Vector<T2>>(map);
		SortedMap<T1, Vector<T2>> sMap = drzewo.subMap(odKlucza, true, doKlucza, true);
		Set<T1> klucze = sMap.keySet();
		Set<Map.Entry<T1, T2>> wynik = new HashSet<Entry<T1, T2>>();
		for (T1 klucz: klucze)
		{
			Vector<T2> wartosci = map.get(klucz);
			for (T2 wartosc: wartosci)
			{
				wynik.add(new AbstractMap.SimpleEntry<T1, T2>(klucz, wartosc));
			}
		}
		return wynik;
	}
	
	@Override
	public T2 get(Object key)
	{
		return null;
	}
	
	@Override
	public List<T2> getElements(T1 key)
	{
		List<T2> result;
		
		result = new ArrayList<>();
		if (this.map.containsKey(key))
		{
			result = this.map.get(key);
		}
		
		return result;
	}
	
	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}
	
	@Override
	public Set<T1> keySet()
	{
		return map.keySet();
	}
	
	@SuppressWarnings ("unchecked")
	@Override
	public void putAll(Map<? extends T1, ? extends T2> m)
	{
		Set<?> set;
		Iterator<?> iterator;
		Object object;
		Entry<?, ?> entry;
		T1 key;
		T2 value;
				
		set = m.entrySet();
		iterator = set.iterator();
		while (iterator.hasNext())
		{
			object = iterator.next();
			entry = (Entry<?, ?>) object;
			key = (T1) entry.getKey();
			value = (T2) entry.getValue();
			this.put(key, value);
		}
	}
	
	@Override
	public T2 remove(Object key) throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Vector<T2> removeElements(T1 key)
	{
		return map.remove(key);
	}
	
	@Override
	public boolean remove(Object key, Object value)
	{
		List<T2> list;
		boolean result;
		
		list = this.map.get(key);
		result = list.remove(value);
		
		return result;
	}
	
	@Override
	public int size()
	{
		return map.size();
	}
	
	@Override
	public Collection<T2> values()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		return this.map.toString();
	}
	
	@Override
	public boolean contains(T1 key, T2 value)
	{
		List<T2> list;
		boolean result;
		
		list = this.map.get(key);
		result = list.contains(value);
		
		return result;
	}
	
}
