package data;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import org.joda.time.DateTime;

import liczby.Zakres;

/**
 * class represents period of time
 * @author Marek
 *
 */
public final class Okres
{
	private final Entry<Date, Date> daty;
	private final SimpleDateFormat formater;
	
	/**
	 * initialize class with poczatek and koniec and formater
	 * @param poczatek
	 * @param koniec
	 * @param formater
	 */
	public Okres(Date poczatek, Date koniec, SimpleDateFormat formater)
	{
		this.daty = new AbstractMap.SimpleEntry<>(poczatek, koniec);
		this.formater = formater;
	}

	public Okres(Date poczatek, Date koniec, String format)
	{
		this(poczatek, koniec, new SimpleDateFormat(format));
	}

	public Okres(Date poczatek, String format)
	{
		this(poczatek, null, new SimpleDateFormat(format));
	}

	public Okres(Date poczatek, SimpleDateFormat formater)
	{
		this(poczatek, null, formater);
	}
	
	public Okres(Date poczatek)
	{
		this(poczatek, null, new SimpleDateFormat());
	}

	public Okres(Date poczatek, Date koniec)
	{
		this(poczatek, koniec, new SimpleDateFormat());
	}
	
	public Date uzyskajPoczatek()
	{
		Date poczatek;
		
		poczatek = this.daty.getKey();
		
		return poczatek;
	}
	
	public Date uzyskajKoniec()
	{
		Date koniec;
		
		koniec = this.daty.getValue();
		
		return koniec;
	}
	
	public String uzyskajFormat()
	{
		String format;
		
		format = this.formater.toPattern();
		
		return format;
	}
	
	public SimpleDateFormat uzyskajFormater()
	{
		return this.formater;
	}
	
	public Okres ustawKoniec(Date koniec)
	{
		this.daty.setValue(koniec);
		return this;
	}
	
	public Okres ustawFormat(String format)
	{
		this.formater.applyPattern(format);
		return this;
	}
	
	public boolean czyPrzed(Date innaData)
	{
		boolean wynik;
		
		wynik = innaData.before(this.uzyskajPoczatek());
		
		return wynik;
	}
	
	
	public boolean czyPrzed()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.czyPrzed(dzisiaj);
		
		return wynik;
	}
	
	
	public boolean czyWTrakcie(Date innaData)
	{
		boolean warunek1, warunek2, wynik;
		Date koniec;
		
		warunek1 = innaData.after(this.uzyskajPoczatek());
		koniec = this.uzyskajKoniec();
		if (koniec != null)
		{
			warunek2 = innaData.before(koniec);
		}
		else
		{
			warunek2 = true;
		}
		wynik = warunek1 && warunek2;
		
		
		
		return wynik;
	}
	
	
	public boolean czyWTrakcie()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.czyWTrakcie(dzisiaj);
		
		return wynik;
	}
	
	public boolean czyZakonczony(Date innaData)
	{
		boolean wynik;
		Date koniec;
		
		koniec = this.uzyskajKoniec();
		if (koniec != null)
		{
			wynik = innaData.after(koniec);
		}
		else
		{
			wynik = false;
		}
		
		return wynik;
	}
	
	public boolean czyZakonczony()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.czyZakonczony(dzisiaj);
		
		return wynik;
	}
		
	public String toString()
	{
		String wynik;
		
		wynik = this.toString(this.formater);
		
		return wynik;
	}
	
	public String toString(String format)
	{
		String wynik;
		SimpleDateFormat sdf;
		
		sdf = new SimpleDateFormat(format);
		wynik = this.toString(sdf);
		
		return wynik;
	}
	
	public String toString(SimpleDateFormat formater)
	{
		String wynik;
		Date poczatek, koniec;
		
		wynik = "";
		poczatek = this.uzyskajPoczatek();
		wynik += "(";
		wynik += formater.format(poczatek);
		koniec = this.uzyskajKoniec();
		if (poczatek.equals(koniec))
		{
			wynik += ")";
		}
		else
		{
			wynik += " - ";
			wynik += formater.format(koniec);
			wynik += ")";
		}
		
		return wynik;
	}
	
	public List<Date> zakres()
	{
		List<Date> wynik;
		Date poczatek, koniec;
		GeneratorDat gd;
		
		koniec = this.uzyskajKoniec();
		if (koniec == null)
		{
			wynik = new ArrayList<>();
		}
		else
		{
			poczatek = this.uzyskajPoczatek();
			gd = new GeneratorDat();
			wynik = Zakres.zakres(poczatek, koniec, gd);
		}
		
		return wynik;
	}
	
}
