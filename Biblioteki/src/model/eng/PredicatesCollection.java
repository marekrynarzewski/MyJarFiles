package model.eng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class PredicatesCollection<T> implements Predicate<T>
{
	protected final Collection<Predicate<T>> predicates = new ArrayList<>();

	public static boolean and(Collection<Boolean> bools)
	{
		boolean result;
		
		result = true;
		for (Boolean bool : bools)
		{
			if (!bool)
			{
				result = false;
				break;
			}
		}
		
		return result;
	}
	@Override
	public boolean test(T item)
	{
		Collection<Boolean> bools;
		boolean tmp, result;
		
		bools = new ArrayList<>();
		for (Predicate<T> predicate : this.predicates)
		{
			tmp = predicate.test(item);
			bools.add(tmp);
		}
		result = and(bools);
		
		return result;
	}
	
	public void add(Predicate<T> predicate)
	{
		this.predicates.add(predicate);
	}
	
	public void remove(Predicate<T> predicate)
	{
		this.predicates.add(predicate);
	}
	
	public void clear()
	{
		this.predicates.clear();
	}

}
