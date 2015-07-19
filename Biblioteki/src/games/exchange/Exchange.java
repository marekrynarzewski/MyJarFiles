package games.exchange;

import io.Ekran;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import json.JSONObject;
import firebase4j.error.FirebaseException;
import zrodla_danych.json.JSONBaza;

public class Exchange
{
	private static final int	BRAK_INTERNETU	= 6;

	public static void main(String[] args)
	{
		runGame();
	}
	
	private static void runGame()
	{
		try
		{
			init();
			save();
		}
		catch (FirebaseException e)
		{
			Ekran.blad("Gra nie dziala bez Internetu", BRAK_INTERNETU);
		}
		
	}
	
	private static void save() throws FirebaseException
	{
		saveSettings();
	}
	
	private static void saveSettings() throws FirebaseException
	{
		base.zapisz(path+"settings/today", String.valueOf(today));
	}

	private static void init() throws FirebaseException
	{
		base = new JSONBaza(baseName);
		readDeals();
		readSettings();
	}
	static final String path = "exchange/";
	private static void readSettings()
	{
		JSONObject settings;
		try
		{
			settings = base.obiektJSON(path+"settings");
			today = settings.optInt("today", 1);
		}
		catch (FirebaseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static JSONBaza base;
	public static final String baseName = "bankinternetowynatasy";
	
	@SuppressWarnings ("unchecked")
	private static void readDeals() throws FirebaseException
	{
		JSONObject data = base.obiektJSON(path+"deals");
		System.out.println(data.toString(2));
		Set<Object> set = data.keySet();
		if (set != null)
		{
			System.out.println(set);
			for (Object okey : set)
			{
				String skey = (String) okey;
				JSONObject subData = data.getJSONObject(skey);
				System.out.println(subData);
				Deal deal = new Deal(subData);
				deals.add(deal);
			}
			System.out.println(deals);
		}
	}
	private static List<Deal> deals = new ArrayList<Deal>();

	static Integer today;
}

