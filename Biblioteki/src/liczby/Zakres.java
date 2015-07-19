package liczby;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import data.GeneratorDat;

public class Zakres
{
	/**
	 * generates range from minimum to maximum in steps specified by the generator
	 * @param min a starting point of range
	 * @param max a ending point of range
	 * @param generator class that gives step and test end of range
	 * @return list of objects from range
	 */
	public static <T extends Comparable> List<T> zakres(T min, T max, Generator<T> generator)
	{
		List<T> wynik;
		T i;
		
		wynik = new ArrayList<>();
		for (i = min; generator.test(i, max); i = generator.next(i))
		{
			wynik.add(i);
		}
		
		return wynik;
	}
		
	/**
	 * generates all the natural numbers from a minimum to a maximum of 1
	 * @param min a starting point of range inclusive
	 * @param max a ending point of range exclusive
	 * @return list of integers from range
	 */
	public static List<Integer> zakres(int min, int max)
	{
		List<Integer> wynik;
		
		wynik = zakres(min, max, new Inkrementacja());
				
		return wynik;
	}
	
	/**
	 * generates all the natural numbers from 0 to a maximum every 1 
	 * @param max a ending point of range exclusive
	 * @return list of integers from range
	 */
	public static List<Integer> zakres(int max)
	{
		List<Integer> wynik;
		
		wynik = zakres(0, max);
				
		return wynik;
	}	
}
