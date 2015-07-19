package pomoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Tablice
{
	public static Integer[] kopiuj(int[] zrodlo)
	{
		Integer[] wynik = new Integer[zrodlo.length];
		int indeks = 0;
		for (int i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Double[] kopiuj(double[] zrodlo)
	{
		Double[] wynik = new Double[zrodlo.length];
		int indeks = 0;
		for (double i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Float[] kopiuj(float[] zrodlo)
	{
		Float[] wynik = new Float[zrodlo.length];
		int indeks = 0;
		for (float i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Long[] kopiuj(long[] zrodlo)
	{
		Long[] wynik = new Long[zrodlo.length];
		int indeks = 0;
		for (long i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Short[] kopiuj(short[] zrodlo)
	{
		Short[] wynik = new Short[zrodlo.length];
		int indeks = 0;
		for (short i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Boolean[] kopiuj(boolean[] zrodlo)
	{
		Boolean[] wynik = new Boolean[zrodlo.length];
		int indeks = 0;
		for (boolean i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Byte[] kopiuj(byte[] zrodlo)
	{
		Byte[] wynik = new Byte[zrodlo.length];
		int indeks = 0;
		for (byte i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static Character[] kopiuj(char[] zrodlo)
	{
		Character[] wynik = new Character[zrodlo.length];
		int indeks = 0;
		for (char i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static int[] kopiuj(Integer[] zrodlo)
	{
		int[] wynik = new int[zrodlo.length];
		int indeks = 0;
		for (Integer i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static double[] kopiuj(Double[] zrodlo)
	{
		double[] wynik = new double[zrodlo.length];
		int indeks = 0;
		for (Double i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static float[] kopiuj(Float[] zrodlo)
	{
		float[] wynik = new float[zrodlo.length];
		int indeks = 0;
		for (Float i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static long[] kopiuj(Long[] zrodlo)
	{
		long[] wynik = new long[zrodlo.length];
		int indeks = 0;
		for (Long i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static short[] kopiuj(Short[] zrodlo)
	{
		short[] wynik = new short[zrodlo.length];
		int indeks = 0;
		for (Short i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static boolean[] kopiuj(Boolean[] zrodlo)
	{
		boolean[] wynik = new boolean[zrodlo.length];
		int indeks = 0;
		for (Boolean i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static byte[] kopiuj(Byte[] zrodlo)
	{
		byte[] wynik = new byte[zrodlo.length];
		int indeks = 0;
		for (Byte i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	public static char[] kopiuj(Character[] zrodlo)
	{
		char[] wynik = new char[zrodlo.length];
		int indeks = 0;
		for (Character i : zrodlo)
		{
			wynik[indeks] = i;
			indeks++;
		}
		return wynik;
	}
	
	
	
	public static <K> int indeks(K[] tablica, K wartosc)
	{
		return indeks(tablica, 1, wartosc);
	}
	
	public static <K> int indeks(K[] tablica, int numer, K wartosc)
	{
		int licznik = 0;
		for (int indeks = 0; indeks < tablica.length; indeks++)
		{
			K obiekt = tablica[indeks];
			if (obiekt != null && obiekt.equals(wartosc))
			{
				licznik++;
				if (licznik == numer)
				{
					return indeks;
				}
			}
		}
		return -1;
	}
	
	public static int[] dodaj(int[] tablica, int element)
	{
		int nowyRozmiar = tablica.length+1;
		int[] wynik = new int[nowyRozmiar];
		for (int i = 0; i < tablica.length; i++)
		{
			wynik[i] = tablica[i];
		}
		wynik[nowyRozmiar-1] = element;
		return wynik;
	}
	
	@SuppressWarnings ("unchecked")
	public static <T> T[] dodaj(T[] tablica, T element)
	{
		int nowyRozmiar = tablica.length+1;
		T[] wynik = (T[]) new Object[nowyRozmiar];
		for (int i = 0; i < tablica.length; i++)
		{
			wynik[i] = tablica[i];
		}
		wynik[nowyRozmiar-1] = element;
		return wynik;
	}
	public static int[] pop(int[] tablica)
	{
		int[] wynik = new int[tablica.length-1];
		for (int i = 0, j = 0;  j < tablica.length-1; i++, j++)
		{
			wynik[i] = tablica[j];
		}
		return wynik;
	}
	
	
	
	public static <K> List<K> asList(Collection<K> source)
	{
		List<K> result = new ArrayList<K>();
		for (K item : source)
		{
			result.add(item);
		}
		return result;
	}
	
	public static <T> T[] removeElements(T[] input, T item) {
	    List<T> list;
	    T[] result;
	    
	    list = new LinkedList<T>();
	    for(T current : input)
	    {
	        if(!item.equals(current))
	        {
	            list.add(item);
	        }
	    }
	    result = list.toArray(input);

	    return result;
	}
	
	public static int[] unique(int[] array)
	{
		Integer[] narray;
		List<Integer> list;
		Set<Integer> set;
		Integer[] result;
		
		narray = kopiuj(array);
		list = Arrays.asList(narray);
		set = new LinkedHashSet<Integer>(list);
		result = (Integer[]) set.toArray();
		array = kopiuj(result);
		
		return array;
	}
	
	
	
	public static <T> T[] polacz(T[] tablica1, T[] tablica2)
	{
		Collection<T> kolekcja;
		T[] wynik;
		
		
		kolekcja = new ArrayList<>();
		for (T element : tablica1)
		{
			kolekcja.add(element);
		}
		for (T element : tablica2)
		{
			kolekcja.add(element);
		}
		wynik = (T[]) kolekcja.toArray();
		
		return wynik;
	}

	
	
	
	
	public static <T> void kopiuj(T[] zrodlo, T[] cel)
	{
		int j;
		
		j = 0;
		for (T i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(int[] zrodlo, int[] cel)
	{
		int j;
		
		j = 0;
		for (int i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(double[] zrodlo, double[] cel)
	{
		int j;
		
		j = 0;
		for (double i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(float[] zrodlo, float[] cel)
	{
		int j;
		
		j = 0;
		for (float i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(long[] zrodlo, long[] cel)
	{
		int j;
		
		j = 0;
		for (long i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(short[] zrodlo, short[] cel)
	{
		int j;
		
		j = 0;
		for (short i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(boolean[] zrodlo, boolean[] cel)
	{
		int j;
		
		j = 0;
		for (boolean i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(byte[] zrodlo, byte[] cel)
	{
		int j;
		
		j = 0;
		for (byte i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	public static void kopiuj(char[] zrodlo, char[] cel)
	{
		int j;
		
		j = 0;
		for (char i : zrodlo)
		{
			cel[j] = i;
			j++;
		}
	}
	
	
}
