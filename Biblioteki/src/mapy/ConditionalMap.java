package mapy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings ("serial")
public class ConditionalMap<K, V> extends HashMap<K, V>
{
	private Predicate<K> keyPredicate;
	private Predicate<V> valuePredicate;
	private MapHandler<K, V> handler;
	
	public ConditionalMap()
	{
		
	}
	
	public ConditionalMap(Predicate<K> keyPredicate, Predicate<V> valuePredicate)
	{
		this.keyPredicate = keyPredicate;
		this.valuePredicate = valuePredicate;
	}
	
	public static <K, V> ConditionalMap<K, V> createMapByKeyPredicate(Predicate<K> keyPredicate)
	{
		ConditionalMap<K, V> result;
		
		result = new ConditionalMap<>(keyPredicate, null);
		
		return result;
	}
	
	public static <K, V> ConditionalMap<K, V> createMapByValuePredicate(Predicate<V> valuePredicate)
	{
		ConditionalMap<K, V> result;
		
		result = new ConditionalMap<>(null, valuePredicate);
		
		return result;
	}

	/**
	 * @returns value if operation succeeded or null if failed
	 */
	@Override
	public V put(K key, V value)
	{
		V result;
		
		result = null;
		if (this.applyPredicate(key, value))
		{
			super.put(key, value);
			result = value;
		}
		else
		{
			if (this.handler != null)
			{
				this.handler.onChangeValue(this, key, value);
			}
		}
		return result;
	}
	
	@SuppressWarnings ({ "rawtypes", "unchecked" })
	public void putAll(Map<? extends K, ? extends V> innaMapa)
	{
		Set zbior;
		Entry pairs;
		Iterator iterator;
		K klucz;
		V wartosc;
		
		zbior = innaMapa.entrySet();
		iterator = zbior.iterator();
        while (iterator.hasNext()) 
        {
            pairs = (Entry) iterator.next();
            klucz = (K) pairs.getKey();
            wartosc = (V) pairs.getValue();
            this.put(klucz, wartosc);
        }
	}
	
	public void setMapHandler(MapHandler<K, V> handler)
	{
		this.handler = handler;
	}
	
	public MapHandler<K, V> getMapHandler()
	{
		return this.handler;
	}
	
	private boolean applyPredicate(K key, V value)
	{
		boolean cond1, cond2, result;
		
		cond1 = true;
		if (this.keyPredicate != null)
		{
			cond1 = this.keyPredicate.test(key);
		}
		cond2 = true;
		if (this.valuePredicate != null)
		{
			cond2 = this.valuePredicate.test(value);
		}
		result = cond1 && cond2;
		
		return result;
	}
	
	public static final <K>  ConditionalMap<K, Number> getCondMapMinimumValue(Number minimum)
	{
		Predicate<Number> predicate;
		ConditionalMap<K, Number> result;
		
		predicate = getMinimumPredicate(minimum);
		result = ConditionalMap.createMapByValuePredicate(predicate);
		
		return result;
	}
	
	public static final Predicate<Number> getMinimumPredicate(final Number minimum)
	{
		Predicate<Number> result;
		
		result = new Predicate<Number>(){

			@Override
			public boolean test(Number number)
			{
				double currentValue, minimumValue;
				boolean result;
				
				currentValue = number.doubleValue();
				minimumValue = minimum.doubleValue();
				result = (currentValue >= minimumValue);
				
				return result;
				
			}
			
		};
		
		return result;
	}


}
