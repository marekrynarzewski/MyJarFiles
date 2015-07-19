package kolekcje;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class Kolekcje
{
	public static <A> Collection<A> wypelnij(A wartosc, int liczba)
	{
		Collection<A> wynik;
		int i;
		
		wynik = new ArrayList<>();
		for (i = 1; i <= liczba; i++)
		{
			wynik.add(wartosc);
		}
		
		return wynik;
	}
	
	public static <T> Collection<T> filtruj(Collection<T> kolekcja, Predicate<T> predykat)
	{
		Collection<T> wynik;
		
		wynik = new ArrayList<T>();
		for (T element: kolekcja) 
		{
			if (predykat.test(element)) 
			{
				wynik.add(element);
			}
		}
		return wynik;
	}
	
	public static <T> Collection<T> toList(T[] array)
	{
		Collection<T> wynik;
		
		wynik = new ArrayList<>();
		for (T element : array)
		{
			wynik.add(element);
		}
		
		return wynik;
	}
	
	public static <T> void repeat(Collection<T> collection, int times, T element)
	{
		int i;
		
		for (i = 1; i <= times; i++)
		{
			collection.add(element);
		}
	}
}
