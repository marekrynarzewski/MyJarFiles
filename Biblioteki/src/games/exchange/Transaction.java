package games.exchange;

import json.JSONException;
import json.JSONObject;
import firebase4j.error.FirebaseException;

public class Transaction implements Comparable<Transaction>
{
	/**
	 * unikalny numer transakcji
	 */
	public String number;
	/**
	 * tytul transakcji
	 */
	private final String title;
	/**
	 * kwota transakcji
	 */
	public final double quote;
	/**
	 * data wykonania transakcji
	 */
	public final Integer day;
	/**
	 * Portfel zrodlowy transakcji
	 */
	public final Wallet source;
	/**
	 * Portfel celowy transakcji
	 */
	public final Wallet destination;
	
	/**
	 * tworzy nowa transakcja i nadaje jej unikalny numer
	 */
	Transaction(Wallet source, Wallet destination, double quote, String title)
	{
		this.day = Exchange.today;
		this.title = title;
		this.quote = quote;
		this.source = source;
		this.destination = destination;
		number = zapisz();
	}
	
	private String zapisz()
	{
		JSONObject data = toJSON();
		try
		{
			Exchange.base.dolacz("transactions", data);
			String wynik = Exchange.base.lastInsertId();
			Exchange.base.zapisz("transakcje/"+number+"/nr", wynik);
			return wynik;
		}
		catch (FirebaseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public Transaction(String nr) throws Exception
	{
		try
		{
			JSONObject dane = Exchange.base.obiektJSON("transactions/"+nr);
			this.number = nr;
			title = dane.getString("title");
			quote = dane.getDouble("quote");
			day = dane.getInt("day");
			source = new Wallet(dane.getString("source"));
			destination = new Wallet(dane.getString("destination"));
		}
		catch (FirebaseException  e)
		{
			throw new Exception();
		}
	}
	
	
	public JSONObject toJSON() throws JSONException
	{
		JSONObject result = new JSONObject();
		result.put("number", number);
		result.put("title", title);
		result.put("quote", quote);
		result.put("day", day);
		result.put("source", source.number);
		result.put("destination", destination.number);
		return result;
	}

	@Override
	public int compareTo(Transaction transaction)
	{
		int myDay = day;
		int otherDay = transaction.day;
		if (myDay == otherDay)
		{
			return 0;
		}
		else if (myDay < otherDay)
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}

	
}

