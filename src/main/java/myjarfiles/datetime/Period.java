package myjarfiles.datetime;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map.Entry;

//import data.GeneratorDat;
//import liczby.Zakres;

public class Period 
{
	private final Entry<Date, Date> daty;
	private final SimpleDateFormat formater;
	
	/**
	 * initialize class with start and end and formatter
	 * @param poczatek
	 * @param koniec
	 * @param formater
	 */
	public Period(Date poczatek, Date koniec, SimpleDateFormat formater)
	{
		this.daty = new AbstractMap.SimpleEntry<>(poczatek, koniec);
		this.formater = formater;
	}

	public Period(Date poczatek, Date koniec, String format)
	{
		this(poczatek, koniec, new SimpleDateFormat(format));
	}

	public Period(Date poczatek, String format)
	{
		this(poczatek, null, new SimpleDateFormat(format));
	}

	public Period(Date poczatek, SimpleDateFormat formater)
	{
		this(poczatek, null, formater);
	}
	
	public Period(Date poczatek)
	{
		this(poczatek, null, new SimpleDateFormat());
	}

	public Period(Date poczatek, Date koniec)
	{
		this(poczatek, koniec, new SimpleDateFormat());
	}
	
	public Date getStart()
	{
		Date start;
		
		start = this.daty.getKey();
		
		return start;
	}
	
	public Date getEnd()
	{
		Date end;
		
		end = this.daty.getValue();
		
		return end;
	}
	
	public String getFormat()
	{
		String format;
		
		format = this.formater.toPattern();
		
		return format;
	}
	
	public SimpleDateFormat getFormatter()
	{
		return this.formater;
	}
	
	public Period setEnd(Date koniec)
	{
		this.daty.setValue(koniec);
		return this;
	}
	
	public Period setPattern(String format)
	{
		this.formater.applyPattern(format);
		return this;
	}
	
	public boolean before(Date innaData)
	{
		boolean wynik;
		
		wynik = innaData.before(this.getStart());
		
		return wynik;
	}
	
	
	public boolean before()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.before(dzisiaj);
		
		return wynik;
	}
	
	
	public boolean inDuration(Date innaData)
	{
		boolean warunek1, warunek2, wynik;
		Date koniec;
		
		warunek1 = innaData.after(this.getStart());
		koniec = this.getEnd();
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
	
	
	public boolean inDuration()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.inDuration(dzisiaj);
		
		return wynik;
	}
	
	public boolean isEnded(Date innaData)
	{
		boolean wynik;
		Date koniec;
		
		koniec = this.getEnd();
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
	
	public boolean isEnded()
	{
		Date dzisiaj;
		boolean wynik;
		
		dzisiaj = new Date();
		wynik = this.isEnded(dzisiaj);
		
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
		poczatek = this.getStart();
		wynik += "(";
		wynik += formater.format(poczatek);
		koniec = this.getEnd();
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
	
	/*public List<Date> range()
	{
		List<Date> wynik;
		Date poczatek, koniec;
		
		koniec = this.getEnd();
		if (koniec == null)
		{
			wynik = new ArrayList<>();
		}
		else
		{
			poczatek = this.getStart();
			wynik = Zakres.zakres(poczatek, koniec, gd);
		}
		
		return wynik;
	}*/
}
