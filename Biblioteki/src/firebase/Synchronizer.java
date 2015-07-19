package firebase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import zrodla_danych.json.JSONBaza;
import finanse.Transakcja;
import finanse.TransakcjaWyjatek;
import firebase4j.error.FirebaseException;

public class Synchronizer
{
	public static JSONBaza baza = new JSONBaza("androidmyapporganize");
	public static String deviceId;
	
	private static final String TAG = "Synchronizer";
	
	public static boolean connectTo(String deviceId, String networkName)
	{
		String path = "networks/"+networkName;
		JSONArray arr = new JSONArray();
		if (networkExists(networkName))
		{
			debug("Siec istnieje");
			try
			{
				arr = baza.tablica(path);
			}
			catch (FirebaseException e)
			{
				e.printStackTrace();
			}
		}
		arr.put(deviceId);
		try
		{
			boolean res = baza.zapisz(path, arr);
			return res;
		}
		catch (FirebaseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private static boolean networkExists(String networkName)
	{
		try
		{
			JSONObject dane = baza.obiektJSON("networks");
			System.out.println(dane);
			boolean result = (!dane.isNull(networkName));
			return result;
		}
		catch (FirebaseException e)
		{}
		return false;
	}

	public static void disconnectFrom(String networkName)
	{
		try
		{
			JSONArray networkData = baza.tablica("networks/"+networkName);
			JSONArray arr = new JSONArray();
			for (int i = 0; i < networkData.length(); i++)
			{
				String currentName = networkData.getString(i);
				if (!networkName.equals(currentName))
				{
					arr.put(currentName);
				}
			}
			baza.zapisz("network/"+networkName, arr);
			
		}
		catch (FirebaseException e)
		{}
	}
	
	public static class Czas
	{
		public static int milisekund(int sekund)
		{
			return sekund*1000;
		}
		
		public static int sekund(int minut)
		{
			return milisekund(minut*60);
		}
		
		public static int minut(int godzin)
		{
			return sekund(godzin*60);
		}
		
		public static int godzin(int dni)
		{
			return minut(dni*24);
		}
		
	}
	
	public static Collection<JSONObject> getObjects(JSONObject object)
	{
		Collection<JSONObject> result = new ArrayList<JSONObject>();
		JSONArray guids = object.names();
		if (guids != null)
		{
			for (int i = 0; i < guids.length(); i++)
			{
				String guid = guids.getString(i);
				JSONObject item = object.getJSONObject(guid);
				result.add(item);
			}
		}
		return result;
	}

	private static void debug(String msg)
	{
		System.out.println(msg);
	}
	public static Collection<JSONObject> filterByLastModifiedDates(Collection<JSONObject> coll)
	{
		Collection<JSONObject> result = new ArrayList<JSONObject>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String lastSynchronizationDate = "2014-06-09 16:10:00";
		debug("lastSynchronizationDate="+lastSynchronizationDate);
		for (JSONObject json : coll)
		{
			String sLastModifiedDate = json.getString("lastModified");
			try
			{
				Date dLastModifiedDate = sdf.parse(sLastModifiedDate);
				Date dlastSynchronizationDate = sdf.parse(lastSynchronizationDate);
				debug(sLastModifiedDate+" "+lastSynchronizationDate);
				if (dLastModifiedDate.compareTo(dlastSynchronizationDate) >= 0)
				{
					debug("dlmd("+sLastModifiedDate+") < dlsd("+lastSynchronizationDate+")");
					result.add(json);
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
}
