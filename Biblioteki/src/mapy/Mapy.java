package mapy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Predicate;

public class Mapy
{
	public static <K, V> Map<K, V> filtruj(Map<K, V> zrodlo, Predicate<Entry<K, V>> predykat)
	{
		Map<K, V> cel = new HashMap<>();
		Set<Entry<K, V>> zbior;
		
		zbior = zrodlo.entrySet();
		for (Entry<K, V> wpis : zbior)
		{
			if (predykat.test(wpis))
			{
				cel.put(wpis.getKey(), wpis.getValue());
			}
		}
		
		return cel;
	}
	
	public static <K, V> Map<K, V> wykluczKlucze(Map<K, V> zrodlo, Collection<K> klucze)
	{
		Set<K> kluczeZrodla;
		Collection<K> wynikWykluczenia;
		Map<K, V> wynik;
		V wartosc;
		
		kluczeZrodla = zrodlo.keySet();
		wynikWykluczenia = wyklucz(kluczeZrodla, klucze);
		wynik = new HashMap<>();
		for (K klucz : wynikWykluczenia)
		{
			wartosc = zrodlo.get(klucz);
			wynik.put(klucz, wartosc);
		}
			
		return wynik;
	}

	public static <K, V> Map<K, V> wykluczWartosci(Map<K, V> zrodlo, Collection<V> wartosci)
	{
		Collection<V> wartosciZrodla;
		Collection<V> wynikWykluczenia;
		Map<K, V> wynik;
		K klucz;
		
		wartosciZrodla = zrodlo.values();
		wynikWykluczenia = wyklucz(wartosciZrodla, wartosci);
		wynik = new HashMap<>();
		for (V wartosc : wynikWykluczenia)
		{
			klucz = znajdzKlucz(zrodlo, wartosc);
			wynik.put(klucz, wartosc);
		}
			
		return wynik;
	}
	
	public static <K, V> K znajdzKlucz(Map<K, V> zrodlo, V wartosc)
	{
		Set<Entry<K, V>> zbior;
		K klucz;
		V obecnaWartosc;
		
		zbior = zrodlo.entrySet();
		klucz = null;
		for (Entry<K, V> wpis : zbior)
		{
			obecnaWartosc = wpis.getValue();
			if (obecnaWartosc.equals(wartosc))
			{
				klucz = wpis.getKey();
				break;
			}
		}
		
		return klucz;
	}
	
	public static <K> Collection<K> wyklucz(Collection<K> zbiorA, Collection<K> zbiorB)
	{
		Collection<K> wynik;
		
		wynik = new ArrayList<>();
		for (K element : zbiorA)
		{
			if (!zbiorB.contains(element))
			{
				wynik.add(element);
			}
		}
		
		return wynik;
	}
	
	public static <K, V> Map<K, V> mapujPrzez(Collection<V> obiekty, Maper<K, V> maper)
	{
		Map<K, V> wynik;
		K klucz;
		
		wynik = new HashMap<>();
		for (V obiekt : obiekty)
		{
			klucz = maper.mapuj(obiekt);
			wynik.put(klucz, obiekt);
		}
		
		return wynik;
	}
	
	public static <K, V> Map<K, V> polacz(Map<K, V> mapa1, Map<K, V> mapa2)
	{
		Map<K, V> wynik;
		
		wynik = new HashMap<>();
		wynik.putAll(mapa1);
		wynik.putAll(mapa2);
		
		return wynik;
	}
	
	public static <K, V> Map<V, K> odwroc(Map<K, V> source)
	{
		Map<V, K> wynik;
		Set<Entry<K, V>> set;
		K key;
		V value;
		
		wynik = new HashMap<>();
		set = source.entrySet();
		for (Entry<K, V> entry : set)
		{
			key = entry.getKey();
			value = entry.getValue();
			wynik.put(value, key);
		}
		
		return wynik;
	}
	
	public static <K, V> SortedMap<K, V> sortujMape(Map<K, V> mapa, MapComparator<K, V> mc)
	{
		SortedMap<K, V> result;
		
        result = new TreeMap<>(mc);
        result.putAll(mapa);
        
        return result;
	}
	
	public static <K, V> String toString(Map<K, V> mapa, String joiner, String newLiner)
	{
		String wynik = "";
		for (Entry<K, V> entry : mapa.entrySet())
		{
			K key = entry.getKey();
			V value = entry.getValue();
			String pair = key+joiner+value+newLiner;
			wynik += pair;
		}
		return wynik;
	}
	
	public static <K, V> String toString(Map<K, V> mapa)
	{
		return toString(mapa, "=", "\n");
	}
	
	public static <K, V> int pozycjaWMapie(Map<K, V> zrodlo, K klucz)
	{
		int indeks = -1;
		for (Entry<K, V> e : zrodlo.entrySet())
		{
			K obecnyKlucz = e.getKey();
			if (!obecnyKlucz.equals(klucz))
			{
				indeks++;
			}
		}
		return indeks;
	}
	
	public static <K, V> K kluczNaPozycji(Map<K, V> zrodlo, int pozycja)
	{
		int indeks = 1;
		for (Entry<K, V> e : zrodlo.entrySet())
		{
			K klucz = e.getKey();
			if (indeks == pozycja)
			{
				return klucz;
			}
			indeks++;
		}
		return null;
	}
	
	public static <K, V> Map<K, V> inicjalizuj(Collection<K> klucze, V domyslnaWartosc)
	{
		Map<K, V> wynik = new HashMap<K, V>();
		for (K klucz : klucze)
		{
			wynik.put(klucz, domyslnaWartosc);
		}
		return wynik;
	}
	
	public static <K, V> Map<K, V> inicjalizuj(K[] klucze, V wartosc)
	{
		Map<K, V> wynik = new HashMap<K, V>();
		for (K klucz : klucze)
		{
			wynik.put(klucz, wartosc);
		}
		return wynik;
	}
	
	public static <K, V> SortedMap<K, V> sortByValues(Map<K, V> input, Comparator<V> comparator)
	{
        MapComparator<K, V> mapComparator;
        SortedMap<K, V> result;
        
        mapComparator =  new MapComparator<K, V>(input, comparator);
        result = new TreeMap<K, V>(mapComparator);
        result.putAll(input);
        
        return result;
	}
	
	public static <O, K, V> Map<K, V> mapujWiele(Collection<O> kolekcja, Maper<K, O> maperKlucza, Maper<V, O> maperWartosci)
	{
		Map<K, V> wynik;
		K klucz;
		V wartosc;
		
		wynik = new HashMap<>();
		for (O obiekt : kolekcja)
		{
			klucz = maperKlucza.mapuj(obiekt);
			wartosc = maperWartosci.mapuj(obiekt);
			wynik.put(klucz, wartosc);
		}
		
		return wynik;
	}

}
