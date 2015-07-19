package ludzie;
import finanse.Portfel;
import firebase4j.error.FirebaseException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import java.util.Set;
import java.util.Vector;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;

import org.joda.time.DateTime;
import org.joda.time.Years;

import pomoc.Czas;
import pomoc.Konfiguracja;
import pomoc.Para;
import pomoc.Serializacja;
import pomoc.Serializacja.JSONable;
import zrodla_danych.json.JSONBaza;

public class Czlowiek implements JSONable
{
	public final long nr;
	public final String imie;
	public final String nazwisko;
	private Portfel portfel;
	
	private Czlowiek matka = null;
	private Czlowiek ojciec = null;
	private int zycie = 100;
	
	private Entry<Czas, Czas> data;
	protected JSONObject dane;
	protected Vector<Czlowiek> dzieci = new Vector<Czlowiek>();
	
	public Czlowiek(String imie, String nazwisko)
	{
		this.imie = imie;
		this.nazwisko = nazwisko;
		this.data = new Para<Czas, Czas>(new Czas());
		this.nr = Konfiguracja.nowyNumer(this);
		zapisz();
	}
	
	protected void zapisz()
	{
		JSONBaza baza = new JSONBaza();
		JSONObject dane = toJSON();
		try
		{
			baza.zapisz("ludzie/"+nr, dane);
		}
		catch (FirebaseException ex)
		{
			ex.printStackTrace();
		}
	}
	
	protected Czlowiek(String sciezka, long nr) throws CzlowiekWyjatek
	{
		this.nr = nr;
		JSONBaza baza = new JSONBaza();
		try
		{
			dane = baza.obiektJSON(sciezka+"/"+nr);
			if (dane.has("czlowiek"))
			{
				nr = dane.getLong("czlowiek");
				wczytajCzlowieka(nr);
				dane = baza.obiektJSON("ludzie/"+nr);
				imie = dane.getString("imie");
				nazwisko = dane.getString("nazwisko");
			}
			else
			{
				throw new CzlowiekWyjatek();
			}
			
			
		}
		catch (FirebaseException ex)
		{
			throw new CzlowiekWyjatek();
		}
	}
	
	protected void wczytajCzlowieka(long nr) throws CzlowiekWyjatek
	{
		JSONBaza baza = new JSONBaza();
		try
		{
			JSONObject dane = baza.obiektJSON("ludzie/"+nr);
			wczytajDaty(dane);
		}
		catch (FirebaseException ex)
		{
			throw new CzlowiekWyjatek();
		}
	}
	public Czlowiek(long nr) throws CzlowiekWyjatek
	{
		JSONBaza baza = new JSONBaza();
		JSONObject dane;
		try
		{
			dane = baza.obiektJSON("ludzie/"+nr);
			this.nr = nr;
			imie = dane.getString("imie");
			nazwisko = dane.getString("nazwisko");
		}
		catch (FirebaseException e)
		{
			throw new CzlowiekWyjatek("Brak czlowieka o id ("+nr+")");
		} 
		
		wczytajCzlowieka(nr);
	}
	public Czlowiek(String imie)
	{
		this(imie, "");
	}

	public void umiera()
	{
		if (this.zyje())
		{
			data.setValue(new Czas());
		}
	}
	
	public int wiek()
	{
		DateFormat formater = new SimpleDateFormat(Czas.formatGlobalny);
		String rokUrodzenia = data.getKey().toString();
		String dzisiaj = Czas.teraz.toString();
		int years;
		try 
		{
			Date dru = formater.parse(rokUrodzenia);
			DateTime dtru = new DateTime(dru);
			if (data.getValue() != null)
			{
				String rokSmierci = data.getValue().toString();
				Date drs = formater.parse(rokSmierci);
				DateTime dtrs = new DateTime(drs);
				years = Years.yearsBetween(dtru, dtrs).getYears();
			
			}
			else
			{
				Date dzis = formater.parse(dzisiaj);
				DateTime dttd = new DateTime(dzis);
				years = Years.yearsBetween(dtru, dttd).getYears();
			}
			return years;
			
		} 
		catch (ParseException e) {}
		return 0;
	}
	
	public Czas dataUrodzenia()
	{
		return data.getKey();
	}
	
	public Czas dataSmierci()
	{
		return data.getValue();

	}
	
	public void zmienWskaznik()
	{
		if (zycie != 0)
		{
			Random gen = new Random();
			boolean plusIminus = gen.nextBoolean();
			int wartosc = gen.nextInt(20);
			zycie += plusIminus ? +wartosc : -wartosc;
			if (zycie > 100)
				zycie = 100;
			if (zycie == 0)
			{
				this.umiera();
			}
		}
	}
	
	public String toString()
	{
		return imie+" "+nazwisko+" ("+wiek()+")";
	}
	
	public double wskaznik()
	{
		return zycie;
	}
	

	@Override
	public JSONObject toJSON() throws JSONException
	{
		JSONObject wynik = Serializacja.toJSON(this);
		wynik.put("dataUrodzenia", data.getKey());
		wynik.put("dataSmierci", data.getValue());
		wynik.put("matka", ((matka != null)?(matka.nr):("")));
		wynik.put("ojciec", ((ojciec != null)?(ojciec.nr):("")));
		return wynik;
	}

	@Override
	public List<String> polaJSONowe()
	{
		return Arrays.asList("nr", "imie", "nazwisko");
	}
	
	private void wczytajDaty(JSONObject dane)
	{
		String dataUrodzenia = dane.getString("dataUrodzenia");
		Czas odur = new Czas(dataUrodzenia);
		this.data = new Para<Czas, Czas>(odur);
		try
		{
			String dataSmierci = dane.getString("dataSmierci");
			Czas odsm = new Czas(dataSmierci);
			this.data.setValue(odsm);
		}
		catch(JSONException e){}
	}

	public boolean zyje()
	{
		boolean wynik = true;
		if (data.getValue() != null)
		{
			wynik = false;
		}
		return wynik;
	}
	
	public static List<Long> uzyskajNumery()
	{
		List<Long> wynik = new Vector<Long>();
		JSONBaza baza = new JSONBaza();
		try
		{
			Map<String, Object> dane = baza.obiektMap("ludzie");
			Set<String> snumery = dane.keySet();
			for (String snumer : snumery)
			{
				Long inumer = Long.valueOf(snumer);
				wynik.add(inumer);
			}
		}
		catch (FirebaseException e)
		{
		}
		return wynik;
	}
	
	protected JSONObject superToJSON(Class<?> klasa, JSONObject d)
	{
		String canName = klasa.getSimpleName().toLowerCase();
		long val = this.nr;
		d.put(canName, val);
		return d;
	}
	
	public JSONObject superToJSON()
	{
		JSONObject d = new JSONObject();
		d = superToJSON(this.getClass(), d);
		return d;
	}
	
	public JSONObject superToJSON(JSONObject d)
	{
		d = superToJSON(this.getClass(), d);
		return d;
	}
	
	public Portfel portfel()
	{
		return portfel;
	}
	
	public Czlowiek rodziSie(String imie, String nazwisko)
	{
		Czlowiek wynik = new Czlowiek(imie, nazwisko);
		dzieci.add(wynik);
		return wynik;
	}
}
