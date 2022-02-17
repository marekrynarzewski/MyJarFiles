package myjarfiles.array;

import java.util.Map;
import java.util.Map.Entry;

public class Maps 
{
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
}
