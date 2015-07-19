package mapy.countery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;

import kolekcje.Kolekcje;
import mapy.Mapy;

public class CounterNumber<K> implements AbstractCounter<K, Number>
{
	private static final Comparator<Number>	comparatorNumber = new Comparator<Number>(){

		@Override
		public int compare(Number a, Number b)
		{
			Double valueOfA, valueOfB;
			int comparison, result;
			
			valueOfA = a.doubleValue();
			valueOfB = b.doubleValue();
			comparison = valueOfB.compareTo(valueOfA);
			if (comparison != 0)
			{
				result = comparison;
			}
			else
			{
				result = 1;
			}
			
			return result;
		}
		
	};
	
	public CounterNumber()
	{
		
	}
	
	public CounterNumber(Collection<K> collection)
	{
		this(summarize(collection));
	}
	
	public CounterNumber(Map<K, Number> map)
	{
		this.map.putAll(map);
	}
	
	public CounterNumber(CounterNumber<K> otherCounter)
	{
		this(otherCounter.map);
	}
	
	private final Map<K, Number> map = new HashMap<>();
	
	@Override
	public Iterator<K> elements()
	{
		Set<Entry<K, Number>> entriesSet;
		Collection<K> collection;
		Iterator<K> result;
		
		entriesSet = this.map.entrySet();
		collection = new ArrayList<>();
		for (Entry<K, Number> entry : entriesSet)
		{
			this.forEachEntry(entry, collection);
		}
		result = collection.iterator();
		
		return result;
	}
	
	@Override
	public List<Entry<K, Number>> mostCommon()
	{
		List<Entry<K, Number>> result;
		int n;
		
		n = this.getSize();
		result = this.mostCommon(n);
		
		return result;
	}

	@Override
	public List<Entry<K, Number>> mostCommon(int n)
	{
		SortedMap<K, Number> sortedMap;
		Set<Entry<K, Number>> entriesSet;
		Iterator<Entry<K, Number>> entryIterator;
		Entry<K, Number> entry;
		int current;
		List<Entry<K, Number>> result;
		
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
		Map<K, Number> map;
		
		map = summarize(collection);
		this.subtract(map);
	}

	@Override
	public void subtract(Map<K, Number> map)
	{
		Set<Entry<K, Number>> entriesSet;
		
		entriesSet = map.entrySet();
		for (Entry<K, Number> entry : entriesSet)
		{
			this.subtractEntry(entry);
		}
	}
	
	@Override
	public void subtract(AbstractCounter<K, Number> counter)
	{
		CounterNumber<K> counterInteger;
		
		counterInteger = (CounterNumber<K>) counter;
		this.subtract(counterInteger.map);
	}

	@Override
	public void update(Map<K, Number> map)
	{
		Set<Entry<K, Number>> entriesSet;
		
		entriesSet = map.entrySet();
		for (Entry<K, Number> entry : entriesSet)
		{
			this.updateEntry(entry);
		}
	}

	@Override
	public void update(Collection<K> collection)
	{
		Map<K, Number> map;
		
		map = summarize(collection);
		this.update(map);
	}

	@Override
	public void update(AbstractCounter<K, Number> counter)
	{
		CounterNumber<K> counterInteger;
		
		counterInteger = (CounterNumber<K>) counter;
		this.update(counterInteger.map);
	}

	@Override
	public Number getCount(K key)
	{
		Number result;
		
		result = 0;
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

	private void forEachEntry(Entry<K, Number> entry, Collection<K> collection)
	{
		K element;
		Number countNumber;
		int intValue;
		
		element = entry.getKey();
		countNumber = entry.getValue();
		intValue = countNumber.intValue();
		Kolekcje.repeat(collection, intValue, element);
	}

	private SortedMap<K, Number> sortMap()
	{
	    SortedMap<K, Number> result;
	    
	    result = Mapy.sortByValues(this.map, comparatorNumber);
	    
	    return result;
	 }

	private void updateEntry(Entry<K, Number> entry)
	{
		Number newNumber, currentNumber, resultNumber;
		double newValue, currentValue, resultValue;
		K element;
		
		newNumber = entry.getValue();
		newValue = getValue(newNumber);
		element = entry.getKey();
		currentNumber = this.getCount(element);
		currentValue = getValue(currentNumber);
		resultValue = currentValue + newValue;
		resultNumber = (Number) resultValue;
		this.map.put(element, resultNumber);
	}

	private void subtractEntry(Entry<K, Number> entry)
	{
		Number newNumber, currentNumber, resultNumber;
		double newValue, currentValue, resultValue;
		K element;
		
		newNumber = entry.getValue();
		newValue = getValue(newNumber);
		element = entry.getKey();
		currentNumber = this.getCount(element);
		currentValue = getValue(currentNumber);
		resultValue = currentValue - newValue;
		resultNumber = (Number) resultValue;
		this.map.put(element, resultNumber);
	}

	private double getValue(Number n)
	{
		return n.doubleValue();
	}

	public static <K> Map<K, Number> summarize(Collection<K> collection)
	{
		Operator<Number> operator;
		Map<K, Number> result;
		
		operator = Operator.operatorNumber;
		result = Counter.summarize(collection, 0, operator, 1);
		
		return result;
		
	}
}