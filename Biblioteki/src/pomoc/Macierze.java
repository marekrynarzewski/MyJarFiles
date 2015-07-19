package pomoc;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Macierze
{
	public static int[][] diagonalna(int rozmiar)
	{
		return diagonalna(rozmiar, 1);
	}
	
	public static int[][] diagonalna(int rozmiar, int wartosc)
	{
		int[][] wynik = new int[rozmiar][rozmiar];
		for (int i = 0; i < rozmiar; i++)
		{
			for (int j = 0; j < rozmiar; j++)
			{
				if (i == j)
					wynik[i][j] = wartosc;
			}
		}
		return wynik;
	}
	
	public static <O> O[][] diagonalna(Class<O> o, int rozmiar, O wartosc)
	{
		@SuppressWarnings ("unchecked")
		O[][] wynik = (O[][]) Array.newInstance(o,rozmiar);
		for (int i = 0; i < rozmiar; i++)
		{
			for (int j = 0; j < rozmiar; j++)
			{
				if (i == j)
					wynik[i][j] = wartosc;
			}
		}
		return wynik;
	}
	
	public static interface Warunek<T>
	{
		boolean zastosuj(T dane);
	}
	
	public static <Klucz> List<Entry<Integer, Integer>> uzyskajIndeksySpelniajaceWarunek(Klucz[][] macierz, Warunek<Klucz> warunek)
	{
		List<Entry<Integer, Integer>> wynik =  new ArrayList<Entry<Integer, Integer>>();
		 for (int i = 0; i < macierz.length; i++)
		 {
			 for (int j = 0; j < macierz[i].length; j++)
			 {
				 if (warunek.zastosuj(macierz[i][j]))
				 {
					 Entry<Integer, Integer> para = new Para<Integer, Integer>(i, j);
					 wynik.add(para);
				 }
			 }
		 }
		 return wynik;
	}
	
	public static <Dim1, Dim2, Val> List<Entry<Dim1, Dim2>> uzyskajIndeksy(Map<Dim1, Map<Dim2, Val>> macierz)
	{
		List<Entry<Dim1, Dim2>> result =  new ArrayList<Entry<Dim1, Dim2>>();
		for (Entry<Dim1, Map<Dim2, Val>> level1 : macierz.entrySet())
		{
			Map<Dim2, Val> index1 = level1.getValue();
			for (Entry<Dim2, Val> level2 : index1.entrySet())
			{
				Dim2 index2 = level2.getKey();
				Entry<Dim1, Dim2> pair = new Para<Dim1, Dim2>(level1.getKey());
				pair.setValue(index2);
				result.add(pair);
			}
		}
		return result;
	}
	
	public static <Dim1, Dim2, Val> List<Entry<Dim1, Dim2>> uzyskajIndeksySpelniajaceWarunek(Map<Dim1, Map<Dim2, Val>> macierz, Warunek<Val> warunek)
	{
		List<Entry<Dim1, Dim2>> result =  new ArrayList<Entry<Dim1, Dim2>>();
		for (Entry<Dim1, Map<Dim2, Val>> level1 : macierz.entrySet())
		{
			Map<Dim2, Val> index1 = level1.getValue();
			for (Entry<Dim2, Val> level2 : index1.entrySet())
			{
				Dim2 index2 = level2.getKey();
				if (warunek.zastosuj(level2.getValue()))
				{
					Entry<Dim1, Dim2> pair = new Para<Dim1, Dim2>(level1.getKey());
					pair.setValue(index2);
					result.add(pair);
				}
			}
		}
		return result;
	}
	
	public static <Value> Map<Integer, Map<Integer, Value>> doMacierzy(Value[][] macierz)
	{
		Map<Integer, Map<Integer, Value>> wynik = new TreeMap<Integer, Map<Integer, Value>>();
		for (int i = 0; i < macierz.length; i++)
		{
			Map<Integer, Value> podWynik = new TreeMap<Integer, Value>();
			for (int j = 0; j < macierz[i].length; j++)
			{
				podWynik.put(j, macierz[i][j]);
			}
			wynik.put(i, podWynik);
		}
		return wynik;
	}
	
	public static <Value> List<Value> getRow(Value[][] macierz, int row) throws ArrayIndexOutOfBoundsException
	{
		int size = macierz[row].length;
		List<Value> result = new ArrayList<Value>();
		for (int i = 0; i < size; i++)
		{
			result.add(macierz[row][i]);
		}
		return result;
	}
	
	public static <Value> List<Value> getColumn(Value[][] macierz, int column) throws ArrayIndexOutOfBoundsException
	{
		int size =  macierz.length;
		List<Value> result = new ArrayList<Value>();
		for (int i = 0; i < size; i++)
		{
			
			result.add(macierz[i][column]);
		}
		return result;
	}
	
	public static <Dim1, Dim2, Value> Value[] getColumn(Map<Dim1, Map<Dim2, Value>> macierz, Dim1 column) throws ArrayIndexOutOfBoundsException
	{
		return null;
	}
	
	public static <Dim1, Dim2, Value> Value[] getRow(Map<Dim1, Map<Dim2, Value>> macierz, Dim2 row) throws ArrayIndexOutOfBoundsException
	{
		return null;
	}
	
	public static <T> String toString(T[][] macierz)
	{
		String wynik = "";
		for (T[] wiersz : macierz)
		{
			String swiersz = Arrays.toString(wiersz);
			wynik += swiersz+"\n";
		}
		return wynik;
	}
	
	public static Integer[][] mnoz(Integer[][] mA, Integer[][] mB) throws Exception
	{
		if (liczbaKolumn(mA) != liczbaWierszy(mB))
		{
			throw new UnsupportedOperationException("Mno¿enie niewykonalne");
		}
		int lwmA = liczbaWierszy(mA);
		int lkmB = liczbaKolumn(mB);
		Integer[][] wynik = new Integer[lwmA][lkmB];
		for (int i = 0; i < lwmA; i++)
		{
			for (int j = 0; j < lkmB; j++)
			{
				wynik[i][j] = suma(mA, mB, i, j);
			}
		}
		return wynik;
		
	}
	
	public static Integer suma(Integer[][] mA, Integer[][] mB, int i, int j) throws Exception
	{
		Integer wynik = 0;
		for (int r = 0; r < liczbaWierszy(mA); r++)
		{
			wynik += mA[i][r]*mB[r][j];
		}
		return wynik;
	}

	public static <T> int liczbaKolumn(T[][] m)
	{
		return m.length;
	}
	
	public static <T> int liczbaWierszy(T[][] m) throws Exception
	{
		if (liczbaKolumn(m) == 0)
		{
			throw new Exception("Zbyt ma³a liczba kolumn");
		}
		return m[0].length;
	}
	
	public static Integer[][] potega(Integer[][] m, int p) throws Exception
	{
		Integer[][] wynik = m;
		for (int i = 1; i <= p; i++)
		{
			wynik = mnoz(wynik, wynik);
		}
		return wynik;
	}
	
	public static <T> T[] przesunWPrawo(T[] lista, int offset)
	{
		T[] wynik = lista.clone();
		int j = Math.abs(offset) % lista.length;
		for (int i = 0; i < lista.length; i++)
		{
			wynik[i] = lista[j];
			j=next(j, lista);
		}
		return wynik;
	}
	
	public static <T> T[] przesunWLewo(T[] lista, int offset)
	{
		T[] wynik = lista.clone();
		int j = Math.abs(offset) % lista.length;
		for (int i = 0; i < lista.length; i++)
		{
			wynik[i] = lista[j];
			j=prev(j, lista);
		}
		return wynik;
	}

	public static <T> int next(int obecny, T[] tablica)
	{
		obecny++;
		if (obecny > tablica.length-1)
		{
			obecny = 0;
		}
		return obecny;
	}
	
	public static <T> int prev(int obecny, T[] tablica)
	{
		obecny--;
		if (obecny < 0)
		{
			obecny = tablica.length;
		}
		return obecny;
	}
	
	
}
