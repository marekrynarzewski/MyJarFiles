package liczby;

import java.util.List;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Przedzial
{
	private boolean lewostronnieDomkniêty;
	private Number lewaStrona;
	private Number prawaStrona;
	private boolean prawostronnieDomnkiety;
	
	public Przedzial(boolean lewostronnieDomkniety, Number lewaStrona, 
			Number prawaStrona, boolean prawostronnieDomnkiety) throws Exception
	{
		if (lewaStrona.doubleValue() > prawaStrona.doubleValue())
		{
			throw new Exception();
		}
		this.lewostronnieDomkniêty = lewostronnieDomkniety;
		this.lewaStrona = lewaStrona;
		this.prawaStrona = prawaStrona;
		this.prawostronnieDomnkiety = prawostronnieDomnkiety;
	}
	
	public Przedzial(Number lewaStrona, Number prawaStrona, boolean prawostronnieDomnkiêty) throws Exception
	{
		this(true, lewaStrona, prawaStrona, prawostronnieDomnkiêty);
	}
	
	public Przedzial(boolean lewostronnieDomkniety, Number lewaStrona, Number prawaStrona) throws Exception
	{
		this(lewostronnieDomkniety, lewaStrona, prawaStrona, false);
	}
	
	public Przedzial(Number lewaStrona, Number prawaStrona) throws Exception
	{
		this(true, lewaStrona, prawaStrona);
	}
	
	public String toString()
	{
		String wynik;
		
		wynik = "";
		if (this.lewostronnieDomkniêty)
		{
			wynik += "[";
		}
		else
		{
			wynik += "(";
		}
		wynik += this.lewaStrona+", "+this.prawaStrona;
		if (this.prawostronnieDomnkiety)
		{
			wynik += "]";
		}
		else
		{
			wynik += ")";
		}
		
		return wynik;
	}
	
	public boolean miedzy(Przedzial p)
	{
		boolean warunek1, warunek2, wynik;

		warunek1 = (p.lewaStrona.doubleValue() < this.lewaStrona.doubleValue());
		warunek2 = (p.prawaStrona.doubleValue() > this.prawaStrona.doubleValue());
		wynik = warunek1 && warunek2;
		
		return wynik;
	}
}
