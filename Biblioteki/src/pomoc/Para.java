package pomoc;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import json.JSONObject;
import pomoc.Serializacja.JSONable;


public class Para<T1, T2> implements Entry<T1, T2>, JSONable, Comparable<Para<T1, T2>>
{
	private T1 pierwsza;
	private T2 druga;
	
	public Para(T1 klucz)
	{
		pierwsza = klucz;
	}
	
	public Para(T1 klucz, T2 wartosc)
	{
		pierwsza = klucz;
		druga = wartosc;
	}

	public Para()
	{
	}

	@Override
	public T1 getKey() {
		return pierwsza;
	}

	@Override
	public T2 getValue() {
		return druga;
	}

	@Override
	public T2 setValue(T2 value) 
	{
		T2 poprzedniaWartosc = druga;
		druga = value;
		return poprzedniaWartosc;
	}
	
	public String toString()
	{
		return "( "+pierwsza+" , "+druga+" )";
	}

	@Override
	public JSONObject toJSON()
	{
		return Serializacja.toJSON(this);
	}

	@Override
	public List<String> polaJSONowe()
	{
		return Arrays.asList("pierwsza", "druga");
	}

	@Override
	public int compareTo(Para<T1, T2> arg0)
	{
		boolean rowneKlucze = pierwsza == arg0.pierwsza;
		boolean rowneWartosci = druga == arg0.druga;
		if (rowneKlucze && rowneWartosci)
		{
			return 0;
		}
		return -1;
	}

}
