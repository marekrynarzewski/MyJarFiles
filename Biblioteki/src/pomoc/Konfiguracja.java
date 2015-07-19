package pomoc;

import firebase4j.error.FirebaseException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import json.JSONException;
import json.JSONObject;
import pomoc.Serializacja.JSONable;
import zrodla_danych.json.JSONBaza;

public class Konfiguracja implements JSONable
{
	private static long aktualnyNumer = 1;
	private static Map<Long, String> obiekty = new HashMap<Long, String>();

	public static void czytaj()
	{
		JSONBaza baza = new JSONBaza();
		try
		{
			JSONObject dane = baza.obiektJSON("konfiguracja");
			aktualnyNumer = dane.getLong("aktualnyNumer");
			wczytajObiekty(dane);
			System.out.println(obiekty);
		}
		catch (FirebaseException | JSONException ex)
		{
			czysta();
		}
	}

	public static long nowyNumer(Object obj)
	{
		long an = aktualnyNumer;
		aktualnyNumer += 1;
		zapisz("aktualnyNumer");
		String nazwaKlasy = obj.getClass().getName();
		obiekty.put(an, nazwaKlasy);
		zapiszObiekty();
		return an;
	}

	/**
	 * ustawia numer dla kolejnych ID
	 * @param liczba obecne id
	 */
	public static void ustawNumer(long liczba)
	{
		aktualnyNumer = liczba;
	}

	/**
	 * znajduje klase obiektu o przypisanym globalnym id
	 * @param nr dane Id
	 * @return Klasa obiektu o tym id
	 */
	public static String klasa(long nr)
	{
		String klasa = obiekty.get(nr);
		return klasa;
	}
	
	public static int dlugoscSemestru()
	{
		JSONBaza baza = new JSONBaza();
		String wartosc;
		try
		{
			wartosc = baza.string("konfiguracja/dlugosc_semestru");
			int wynik = Integer.valueOf(wartosc);
			return wynik;
		}
		catch (FirebaseException e){}
		return 15;
	}
	
	/**
	 * reprezentacja JSONowa Konfiguracji
	 */
	@Override
	public JSONObject toJSON()
	{
		JSONObject wynik = Serializacja.toJSON(this);
		wynik.put("obiekty", new JSONObject(obiekty));
		return wynik;
	}

	@Override
	public List<String> polaJSONowe()
	{
		return Arrays.asList("aktualnyNumer");
	}
	
	private static void zapisz()
	{
		Konfiguracja k = new Konfiguracja();
		JSONObject dane = k.toJSON();
		JSONBaza baza = new JSONBaza();
		try
		{
			baza.zapisz("konfiguracja", dane);
		}
		catch (FirebaseException ex){}
	}

	@SuppressWarnings ("unchecked")
	private static void zapisz(String nazwaPola)
	{
		Konfiguracja k = new Konfiguracja();
		Class<Konfiguracja> klasa = (Class<Konfiguracja>) k.getClass();
		try
		{
			Field pole = klasa.getDeclaredField(nazwaPola);
			pole.setAccessible(true);
			String wartosc = (String) pole.get(k);
			JSONBaza baza = new JSONBaza();
			baza.zapisz("konfiguracja/"+nazwaPola, wartosc);
		}
		catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | FirebaseException e)
		{
			//e.printStackTrace();
		}
	}
	
	private static void wczytajObiekty(JSONObject dane)
	{
		Map<Long, String> o = new HashMap<Long, String>();
		try
		{	
			JSONObject os = dane.getJSONObject("obiekty");
			if (os.length() > 0)
			{
				for (Object k : os.keySet())
				{
					String sk = String.valueOf(k);
					Long lk = Long.valueOf(sk);
					String klasa = os.getString(sk);
					o.put(lk, klasa);
				}
				obiekty = o;
			}
		}
		catch(JSONException e){}
	
	}

	private static void zapisz(String nazwaPola, String wartosc)
	{
		JSONBaza baza = new JSONBaza();
		try
		{
			baza.zapisz("konfiguracja/"+nazwaPola, wartosc);
		}
		catch (FirebaseException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void zapiszObiekty()
	{
		JSONObject dane = new JSONObject(obiekty);
		System.out.println(dane);
		zapisz("obiekty", dane.toString());
	}
	
	private static void czysta()
	{
		JSONObject dane = new JSONObject();
		dane.put("aktualnyNumer", 1);
		dane.put("dlugosc_semestru", 15);
		dane.put("lat_studiow", 5);
		dane.put("semestrow", 2);
		JSONBaza baza = new JSONBaza();
		try
		{
			baza.zapisz("konfiguracja", dane);
		}
		catch (FirebaseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
