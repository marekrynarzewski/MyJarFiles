package zrodla_danych.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import firebase4j.error.FirebaseException;
import firebase4j.error.JacksonUtilityException;
import firebase4j.model.FirebaseResponse;
import firebase4j.service.Firebase;

import java.util.ArrayList;
import java.util.Collection;

import zrodla_danych.BazaDanych;
import zrodla_danych.BazaDanych.WyjatekBazyDanych;

public class JSONBaza implements BazaDanych
{
	private Firebase fb;
	private static final String rootUrl = "https://[NAZWA-BAZY].firebaseio.com";
	private String lastInsertId;
	
	public JSONBaza(String nazwaBazy)
	{
		String thisUrl = rootUrl.replace("[NAZWA-BAZY]", nazwaBazy);
		try
		{
			fb = new Firebase(thisUrl);
		}
		catch (FirebaseException e)
		{
			System.err.println("Blad polaczenia z baza danych");
		}
	}
	
	public JSONBaza()
	{
		this("androidmyapporganize");
	}
	public JSONObject obiektJSON(String sciezka) throws FirebaseException
	{
		FirebaseResponse odpowiedz = fb.get(sciezka);
		JSONObject wynik;
		if (odpowiedz.getSuccess())
		{
			String dane = odpowiedz.getRawBody();
			if (dane.startsWith("{") && dane.endsWith("}"))
			{
				wynik = new JSONObject(dane);
				return wynik;
			}
			else if (dane.equals("null"))
			{
				dane = "{}";
				wynik = new JSONObject(dane);
				return wynik;
			}
			else
			{
				System.err.println(sciezka+", "+dane);
			}
		}
		wynik = new JSONObject();
		return wynik;
	}
	
	public JSONArray tablica(String sciezka) throws FirebaseException
	{
		JSONArray wynik;
		FirebaseResponse odpowiedz = fb.get(sciezka);
		if (odpowiedz.getSuccess())
		{
			String rawBody = odpowiedz.getRawBody();
			if (rawBody.startsWith("{") && rawBody.endsWith("}"))
			{
				System.err.println("Zapytanie o sciezke("+sciezka+") zwrocilo "+rawBody);
				throw new JSONException("");
			}
			if (rawBody.equals("null"))
			{
				rawBody = "[]";
			}
			wynik = new JSONArray(rawBody);
			return wynik;
		}
		wynik = new JSONArray();
		return wynik;
	}
	
	public Map<String, Object> obiektMap(String sciezka) throws FirebaseException
	{
		FirebaseResponse odpowiedz = fb.get(sciezka);
		Map<String, Object> wynik;
		if (odpowiedz.getSuccess())
		{
			wynik = odpowiedz.getBody();
			return wynik;
		}
		wynik = new HashMap<String, Object>();
		return wynik;
	}
	public boolean zapisz(String sciezka, JSONObject dane) throws FirebaseException
	{
		String json = dane.toString();
		FirebaseResponse odpowiedz = fb.put(sciezka, json);
		return odpowiedz.getSuccess();
	}
	
	public boolean zapisz(String sciezka, JSONArray dane) throws FirebaseException
	{
		String json = dane.toString();
		FirebaseResponse odpowiedz = fb.put(sciezka, json);
		return odpowiedz.getSuccess();
	}
	
	public boolean zapisz(String sciezka, Map<String, Object> dane) throws FirebaseException, JacksonUtilityException
	{
		FirebaseResponse odpowiedz = fb.put(sciezka, dane);
		return odpowiedz.getSuccess();
	}
	
	public boolean zapisz(String sciezka, String wartosc) throws FirebaseException
	{
		FirebaseResponse odpowiedz = fb.put(sciezka, wartosc);
		return odpowiedz.getSuccess();
	}

	public boolean dolacz(String sciezka, Map<String, Object> dane) throws FirebaseException
	{
		FirebaseResponse odpowiedz;
		try
		{
			odpowiedz = fb.post(sciezka, dane);
			lastInsertId = (String)odpowiedz.getBody().get("name");
			return odpowiedz.getSuccess();
		}
		catch (JacksonUtilityException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean dolacz(String sciezka, JSONObject dane) throws FirebaseException
	{
		String json = dane.toString();
		FirebaseResponse odpowiedz = fb.post(sciezka, json);
		lastInsertId = (String)odpowiedz.getBody().get("name");
		return odpowiedz.getSuccess();
	}
	
	public static Collection<String> klucze(JSONObject dane)
	{
		Collection<String> wynik = new ArrayList<>();
		for (Object klucz : dane.keySet())
		{
			String sklucz = String.valueOf(klucz);
			wynik.add(sklucz);
		}
		return wynik;
	}
	public String getRawBody(String sciezka) throws FirebaseException
	{
		FirebaseResponse odpowiedz = fb.get(sciezka);
		return odpowiedz.getRawBody();
	}
	public String string(String sciezka) throws FirebaseException
	{
		FirebaseResponse odpowiedz = fb.get(sciezka);
		String wynik = odpowiedz.getRawBody();
		return wynik;
	}
	
	public String lastInsertId()
	{
		return lastInsertId;
	}

	public boolean usun(String sciezka) throws WyjatekBazyDanych
	{
		FirebaseResponse odpowiedz;
		try
		{
			odpowiedz = fb.delete(sciezka);
			boolean result = (odpowiedz.getSuccess());
			return result;
		}
		catch (FirebaseException e)
		{
		}
		return false;
	}

	@Override
	public List<Map<String, String>> uzyskajListe(String zapytanie) throws WyjatekBazyDanych
	{
		return null;
	}

	@Override
	public Map<String, String> uzyskajMape(String sciezka) throws WyjatekBazyDanych
	{
		try
		{
			Map<String, Object> mapaObiektow =  this.obiektMap(sciezka);
			return doMapyStringow(mapaObiektow);
		}
		catch (FirebaseException e)
		{
			throw new WyjatekBazyDanych();
		}
	}

	@Override
	public long zmien(String sciezka, Map<String, String> wartosci, String warunek) throws WyjatekBazyDanych
	{
		long wynik = 0;
		for (Entry<String, String> para : wartosci.entrySet())
		{
			String nowaSciezka = sciezka+"/"+para.getKey();
			try
			{
				boolean tmp = this.zapisz(nowaSciezka, ""+para.getValue());
				if (tmp)
				{
					wynik++;
				}
			}
			catch (FirebaseException e)
			{
				throw new WyjatekBazyDanych();
			}
		}
		return wynik;
	}

	@Override
	public long usun(String sciezka, String warunek) throws WyjatekBazyDanych
	{
		boolean tmp = this.usun(sciezka);
		return ((tmp)?(1):(0));
	}

	@Override
	public String wstaw(String sciezka, Map<String, String> dane) throws WyjatekBazyDanych
	{
		Map<String, Object> mapaObiektow = doMapyObiektow(dane);
		try
		{
			dolacz(sciezka, mapaObiektow);
			return lastInsertId();
		}
		catch (FirebaseException e)
		{
			throw new WyjatekBazyDanych(new Exception(e));
		}
	}

	private Map<String, String> doMapyStringow(Map<String, Object> mapaObiektow) throws WyjatekBazyDanych
	{
		Map<String, String> wynik = new TreeMap<>();
		for (Entry<String, Object> wpis : mapaObiektow.entrySet())
		{
			String klucz = wpis.getKey();
			Object wartosc = wpis.getValue();
			wynik.put(klucz, wartosc.toString());
		}
		return wynik;
	}

	private Map<String, Object> doMapyObiektow(Map<String, String> dane) throws WyjatekBazyDanych
	{
		Map<String, Object> wynik = new TreeMap<>();
		for (Entry<String, String> wpis : dane.entrySet())
		{
			String klucz = wpis.getKey();
			String tymczasowaWartosc = wpis.getValue();
			wynik.put(klucz, tymczasowaWartosc);
		}
		return wynik;
	}

	@Override
	public String wstaw(String sciezka, List<Map<String, String>> dane) throws WyjatekBazyDanych
	{
		return null;
	}

    @Override
    public boolean testujPolaczenie() 
    {
        boolean wynikTestu;
        
        try
        {
                this.obiektJSON("/");
                wynikTestu = true; 
        }
        catch(FirebaseException e)
        {
                wynikTestu = false;
        }
        
        return wynikTestu;
    }
}
