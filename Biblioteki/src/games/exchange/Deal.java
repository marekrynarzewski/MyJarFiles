package games.exchange;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import firebase4j.error.FirebaseException;
import pomoc.Serializacja;
public class Deal implements Serializacja.JSONable
{
	public final String name;
	private Map<Integer, Double> prices = new HashMap<Integer, Double>();
	public Deal(String name)
	{
		this.name = name;
	}
	public Deal(String name, Map<Integer, Double> prices)
	{
		this.name = name;
		this.prices = prices;
	}
	@SuppressWarnings ("unchecked")
	public Deal(JSONObject data)
	{
		this.name = data.getString("name");
		JSONArray prices = data.getJSONArray("prices");
		readDays(prices);
	}
	private void readDays(JSONArray prices)
	{
		for (Integer day = 1; day < prices.length(); day++)
		{
			Double price = prices.getDouble(day);
			this.prices.put(day, price);
		}
		
	}
	public double todayPrice()
	{
		return price(Exchange.today);
	}
	public double price(Integer day)
	{
		return prices.get(day);
	}
	public void addPrice(Integer day, Double price)
	{
		prices.put(day, price);
	}
	@Override
	public JSONObject toJSON() throws JSONException
	{
		JSONObject result = new JSONObject();
		result.put("name", name);
		result.put("prices", prices);
		return result;
	}
	@Override
	public List<String> polaJSONowe()
	{
		// TODO Auto-generated method stub
		return null;
	}
	public static Deal getForKey(String key, JSONObject data)
	{
		// TODO Auto-generated method stub
		return null;
	}
	public void save() throws FirebaseException
	{
		JSONObject data = toJSON();
		String path = Exchange.path+"deals";
		Exchange.base.dolacz(path, data);
	}
	public String toString()
	{
		JSONObject data = toJSON();
		String result = data.toString();
		return result;
	}
}
