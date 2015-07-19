package text;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collection;

public class TextUtils
{
	public static String ucfirst(String source)
	{
		String first = source.substring(0,1);
		first = first.toUpperCase();
		String rest = source.substring(1);
		String result = first+rest;
		return result;
	}
	
	public static String repeat(String s, int times)
	{
		char[] tablicaZnakow;
		String wynik;
		
		tablicaZnakow = new char[times];
		wynik = new String(tablicaZnakow);
		wynik = wynik.replace("\0", s);
		
		return wynik;
	}

	public static String lcfirst(String input)
	{
		String first = input.substring(0, 1);
		first = first.toUpperCase();
		String rest = input.substring(1);
		String result = first+rest;
		return result;
	}
	
	public static <T> Collection<String> addToAnyItem(Collection<T> kolekcja, String klucz)
	{
		Collection<String> wynik;

		wynik = new ArrayList<>();
		for (T element : kolekcja)
		{
			wynik.add(element+klucz);
		}
		
		return wynik;
	}
	
	public static <T> Collection<String> addToAnyItem(String klucz, Collection<T> kolekcja)
	{
		Collection<String> wynik;

		wynik = new ArrayList<>();
		for (T element : kolekcja)
		{
			wynik.add(klucz+element);
		}
		
		return wynik;
	}
}
