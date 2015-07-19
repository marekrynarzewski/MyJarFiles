package games.exchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import json.JSONArray;
import json.JSONObject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import firebase4j.error.FirebaseException;

public class Wallet
{
	public final String number;
	private static final List<Transaction> transactions = readTransactions();
	private static final JSONArray numbers = getNumbers();
	public Wallet(String number) throws Exception
	{
		if (json.JSONUtil.hasElement(numbers, number))
		{
			this.number = number;
		}
		else
		{
			throw new Exception();
		}
	}
	private static JSONArray getNumbers()
	{
		JSONObject tmp;
		JSONArray result;
		try
		{
			tmp = Exchange.base.obiektJSON(Exchange.path+"transactions");
			result = tmp.names();
		}
		catch (FirebaseException e)
		{
			result = new JSONArray();
		}
		return result;
	}
	private static List<Transaction> readTransactions()
	{
		List<Transaction> result = new ArrayList<Transaction>();
		try
		{
			JSONObject data = Exchange.base.obiektJSON(Exchange.path+"transactions");
			JSONArray klucze = data.names();
			if (klucze != null)
			{
				for (int i = 0; i < klucze.length(); i++)
				{
					String klucz = klucze.getString(i);
					Transaction t;
					try
					{
						t = new Transaction(klucz);
						result.add(t);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
				}
			}
		}
		catch (FirebaseException e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Transaction> historiaTransakcji()
	{
		List<Transaction> filtered = new ArrayList<Transaction>();
		Collections.copy(filtered, transactions);
		Predicate<Transaction> predicate = new FilterTransaction(this);
		CollectionUtils.filter(filtered, predicate);
		Collections.sort(filtered);
		return filtered;
	}

}
