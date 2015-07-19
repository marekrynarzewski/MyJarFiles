package zrodla_danych.mysql;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

public class Zapytania
{
	public static String kwotuj(Object wartosc)
	{
		return "\""+wartosc+"\"";
	}
	
	public static String listowanie(Collection<String> kolekcja)
	{
		String wynik;
		if (kolekcja.isEmpty())
		{
			wynik = "* ";
		}
		else
		{
			wynik = "";
			int indeks = 0;
			int przedostatniIndeks = kolekcja.size()-1;
			for (String element : kolekcja)
			{
				wynik += element;
				if (indeks < przedostatniIndeks)
				{
					wynik += ", ";
				}
				indeks++;
			}
			wynik += " ";
		}
		return wynik;
	}
	public static String uzyskajZaznaczenie(String tabela, Collection<String> kolumny, UUID uuid)
	{
		String wynik = uzyskajZaznaczenieZWarunkiem(tabela, kolumny, "uuid", uuid.toString());
		return wynik;
	}
	
	public static String uzyskajZaznaczenie(String tabela, UUID uuid)
	{
		Collection<String> c = new ArrayList<>();
		String wynik = uzyskajZaznaczenieZWarunkiem(tabela, c, "uuid", uuid.toString());
		return wynik;
	}
	
	public static String uzyskajZaznaczenie(String tabela, Collection<String> kolumny)
	{
		String wynik = "SELECT ";
		wynik += listowanie(kolumny);
		wynik += "FROM "+tabela+" ";
		return wynik;
	}
	public static String uzyskajZaznaczenie(String tabela, Long id)
	{
		return uzyskajZaznaczenieZWarunkiem(tabela, "id", String.valueOf(id));
	}
	public static String uzyskajZaznaczenieZWarunkiem(String tabela, Collection<String> kolumny, BigInteger numerKonta)
	{
		String wynik = uzyskajZaznaczenieZWarunkiem(tabela, kolumny, "numer_konta", numerKonta.toString());
		return wynik;
	}
	public static String uzyskajZaznaczenieZWarunkiem(String tabela, Collection<String> kolumny, String pole, String wartosc)
	{
		String wynik = uzyskajZaznaczenie(tabela, kolumny);
		wynik += "WHERE "+warunek(pole, wartosc);
		return wynik;
	}
	public static String uzyskajZaznaczenieZWarunkiem(String tabela, String pole, String wartosc)
	{
		Collection<String> c = new ArrayList<>();
		String wynik = uzyskajZaznaczenieZWarunkiem(tabela, c, pole, wartosc);
		return wynik;
	}
	public static String uzyskajZaznaczenieZWarunkiem(String tabela,
			String pole, UUID uuid)
	{
		return uzyskajZaznaczenieZWarunkiem(tabela, pole, uuid.toString());
	}
	
	public static String uzyskajZaznaczenieZWarunkiem(String tabela, Collection<String> kolumny,
			String pole, UUID uuid)
	{
		return uzyskajZaznaczenieZWarunkiem(tabela, kolumny, pole, uuid.toString());
	}
	public static String uzyskajZaznaczenieZWarunkiem(String tabela,
			Collection<String> kolumny, String warunek)
	{
		String wynik = uzyskajZaznaczenie(tabela, kolumny);
		wynik += "WHERE "+warunek;
		return wynik;
	}
	
	public static String warunek(String pole, String operator, Object wartosc)
	{
		String wynik = pole;
		wynik += operator;
		wynik += kwotuj(wartosc);
		return wynik;
	}
	
	public static String warunek(String pole, Object wartosc)
	{
		return warunek(pole, "=", wartosc);
	}
	
	public static String wstawianie(String tabela, Map<String, String> kolumnyZWartosciami)
	{
		final String zapytanie = "INSERT INTO `%s` (%s) VALUES (%s)";
		Set<String> klucze;
		String nazwyKolumn, wartosciPol, wynik;
		Collection<String> wartosci;
		
		klucze = kolumnyZWartosciami.keySet();
		nazwyKolumn = uzyskajLancuchKolumn(klucze);
		wartosci = kolumnyZWartosciami.values();
		wartosciPol = kwotowacKolekcje(wartosci);
		wynik = String.format(zapytanie, tabela, nazwyKolumn, wartosciPol);
		
		return wynik;
	}
	
	private static String uzyskajLancuchKolumn(Set<String> klucze)
	{
		String kolekcjaWStringu = klucze.toString();
		kolekcjaWStringu = kolekcjaWStringu.replace("[", "");
		kolekcjaWStringu = kolekcjaWStringu.replace("]", "");
		return kolekcjaWStringu;
	}
	
	private static String kwotowacKolekcje(Collection<String> wartosci)
	{
		Collection<String> kolekcja = new ArrayList<>();
		for (String element : wartosci)
		{
			String wartosc = kwotowac(element);
			kolekcja.add(wartosc);
		}
		String wynik = join(kolekcja);
		return wynik;
	}

	private static String kwotowac(String wartosc)
	{
		String wynik = "\""+wartosc+"\"";
		return wynik;
	}
	
	private static <T> String join(Collection<T> kolekcja)
	{
		String wynik = "";
		int indeks = 1, rozmiar = kolekcja.size();
		for (T element : kolekcja)
		{
			wynik += element;
			if (indeks < rozmiar)
			{
				wynik += ", ";
			}
			indeks++;
		}
		return wynik;
	}
	
	private static String kwotowacMape(Map<String, String> wartosci)
	{
		Collection<String> kolekcja = new ArrayList<>();
		for (Entry<String, String> element : wartosci.entrySet())
		{
			String klucz = element.getKey();
			String wartosc = kwotowac(element.getValue());
			kolekcja.add(klucz+" = "+wartosc);
		}
		String wynik = join(kolekcja);
		return wynik;
	}

	public static String aktualizacja(String tabela, Map<String, String> wartosci, String warunek)
	{
		final String zapytanie = "UPDATE `%s` SET %s WHERE %s";
		String serializowaneWartosci, wynik;
		
		serializowaneWartosci = kwotowacMape(wartosci);
		wynik = String.format(zapytanie, tabela, serializowaneWartosci, warunek);
		
		return wynik;
	}
	
	public static String usuwanie(String tabela, String warunek)
	{
		final String zapytanie = "DELETE FROM `%s` WHERE %s";
		String wynik;
		
		wynik = String.format(zapytanie, tabela, warunek);
		
		return wynik;
	}
}
