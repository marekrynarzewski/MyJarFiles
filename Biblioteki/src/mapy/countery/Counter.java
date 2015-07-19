package mapy.countery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import mapy.Mapy;

public class Counter<K, V> implements AbstractCounter<K, V>
{
	private final Map<K, V> map = new HashMap<>();
	private final Comparator<V> comparator;
	private final V defaultValue;
	private final Operator<V> operator;
	private V nextValue;
	
	public Counter(Comparator<V> comparator, Operator<V> operator, V defaultValue, V nextValue)
	{
		this.comparator = comparator;
		this.operator = operator;
		this.defaultValue = defaultValue;
		this.nextValue = nextValue;
	}
		
	@Override
	public Iterator<K> elements()
	{
		Collection<K> collection;
		Iterator<K> result;
		
		collection = new ArrayList<>();
		result = collection.iterator();
		
		return result;
	}

	@Override
	public List<Entry<K, V>> mostCommon()
	{
		List<Entry<K, V>> result;
		int n;
		
		n = this.getSize();
		result = this.mostCommon(n);
		
		return result;
	}

	@Override
	public List<Entry<K, V>> mostCommon(int n)
	{
		SortedMap<K, V> sortedMap;
		Set<Entry<K, V>> entriesSet;
		Iterator<Entry<K, V>> entryIterator;
		Entry<K, V> entry;
		int current;
		List<Entry<K, V>> result;
		
		if (n < 0 || n > this.getSize())
		{
			n = this.getSize();
		}
		sortedMap = this.sortMap();
		entriesSet = sortedMap.entrySet();
		entryIterator = entriesSet.iterator();
		current = 1;
		result = new ArrayList<>();
		while (current <= n && entryIterator.hasNext())
		{
			entry = entryIterator.next();
			result.add(entry);
			current ++;
		}		
		
		return result;
	}

	@Override
	public void subtract(Collection<K> collection)
	{
		Map<K, V> map;
		
		map = summarize(collection, this.defaultValue, this.operator, this.nextValue);
		this.subtract(map);
	}

	@Override
	public void subtract(Map<K, V> map)
	{
		Set<Entry<K, V>> entriesSet;
		
		entriesSet = map.entrySet();
		for (Entry<K, V> entry : entriesSet)
		{
			this.subtractEntry(entry);
		}
	}

	@Override
	public void subtract(AbstractCounter<K, V> otherCounter)
	{
		Counter<K, V> counter;
		
		counter = (Counter<K, V>) otherCounter;
		this.subtract(counter.map);
	}

	@Override
	public void update(Map<K, V> map)
	{
		Set<Entry<K, V>> entriesSet;
		
		entriesSet = map.entrySet();
		for (Entry<K, V> entry : entriesSet)
		{
			this.updateEntry(entry);
		}
	}

	@Override
	public void update(Collection<K> collection)
	{
		//Map<K, V> map;
		
		//map = summarize(collection);
		//this.update(map);
	}

	@Override
	public void update(AbstractCounter<K, V> otherCounter)
	{
		Counter<K, V> counter;
		
		counter = (Counter<K, V>) otherCounter;
		this.update(counter.map);
	}

	@Override
	public V getCount(K key)
	{
		V result;
		
		result = defaultValue;
		if (this.map.containsKey(key))
		{
			result = this.map.get(key);
		}
		
		return result;
	}

	@Override
	public int getSize()
	{
		int result;
		
		result = this.map.size();
		
		return result;
	}
	
	private void updateEntry(Entry<K, V> entry)
	{
		V newNumber, currentNumber, resultNumber;
		K element;
		
		newNumber = entry.getValue();
		element = entry.getKey();
		currentNumber = this.getCount(element);
		resultNumber = operator.update(currentNumber, newNumber);
		this.map.put(element, resultNumber);
	}

	private void subtractEntry(Entry<K, V> entry)
	{
		V newNumber, currentNumber, resultNumber;
		K element;
		
		newNumber = entry.getValue();
		element = entry.getKey();
		currentNumber = this.getCount(element);
		resultNumber = operator.subtract(currentNumber, newNumber);
		this.map.put(element, resultNumber);
	}

	private SortedMap<K, V> sortMap()
	{
		SortedMap<K, V> result;
	    
	    result = Mapy.sortByValues(this.map, this.comparator);
	    
	    return result;
	}
	
	public static <K, V> Map<K, V> summarize(Collection<K> collection, 
			V defVal, Operator<V> operator, V nextVal)
	{
		
		V currentCount, newCount;
		Map<K, V> result;
		
		result = Mapy.inicjalizuj(collection, defVal);
		for (K item : collection)
		{
			
			currentCount = result.get(item);
			newCount = operator.update(currentCount,  nextVal);
			result.put(item, newCount);
		}
		
		return result;
	}
}
