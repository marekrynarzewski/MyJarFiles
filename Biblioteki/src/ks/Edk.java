package ks;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Edk
{
	
	
	public static void inicjalizujPrzedmioty()
	{
		przedmioty = new HashMap<String, Cennik>();
		for (int i = 1; i <= 7; i++)
		{
			Cennik cennik = generujCennik();
			przedmioty.put("p"+i, cennik);
		}
	}
	
	private static Map<String, Cennik> przedmioty;
	
	private static class Cennik
	{
		private Map<Zakres, Integer> wyklad, cwiczenia, laboratoria, dyzur;
		public Cennik()
		{
			wyklad = new HashMap<Zakres, Integer>();
			cwiczenia = new HashMap<Zakres, Integer>();
			laboratoria = new HashMap<Zakres, Integer>();
			dyzur = new HashMap<Zakres, Integer>();
		}
		
		public void dodaj(TypZajec typ, Zakres z, Integer cena)
		{
			switch(typ)
			{
				case Cwiczenia:
					cwiczenia.put(z, cena);
					break;
				case Dyzur:
					dyzur.put(z, cena);
					break;
				case Laboratoria:
					laboratoria.put(z, cena);
					break;
				case Wyklad:
					wyklad.put(z, cena);
					break;
				default:
					break;
				
			}
		}
		
		public Map<Zakres, Integer> cena(TypZajec typ)
		{
			Map<Zakres, Integer> wynik = null;
			switch(typ)
			{
				case Cwiczenia:
					wynik = cwiczenia;
					break;
				case Dyzur:
					wynik = dyzur;
					break;
				case Laboratoria:
					wynik = laboratoria;
					break;
				case Wyklad:
					wynik = wyklad;
					break;
				default:
					break;
				
			}
			return wynik;
		}
		
		public int cena(TypZajec typ, Zakres z)
		{
			Map<Zakres, Integer> mapa = cena(typ);
			if (mapa != null)
			{
				Integer cena = mapa.get(z);
				return cena;
			}
			return 0;
		}
	}
	private static enum TypZajec
	{
		Wyklad, Cwiczenia, Laboratoria, Dyzur;
	}
	
	private static class Zakres
	{
		public int min, max;
		public String toString()
		{
			String wynik = "";
			wynik += "(";
			wynik += "min="+min+", ";
			wynik += "max="+max;
			wynik += ")";
			return wynik;
		}
	}
	
	private static Cennik generujCennik()
	{
		Cennik wynik = new Cennik();
		TypZajec[] typy = TypZajec.values();
		for (TypZajec typ : typy)
		{
			Map<Zakres, Integer> zic = generujZakresyICeny();
			for (Entry<Zakres, Integer> wpis : zic.entrySet())
			{
				Zakres z = wpis.getKey();
				Integer cena = wpis.getValue();
				wynik.dodaj(typ, z, cena);
			}
		}
		return wynik;
	}
	
	private static Map<Zakres, Integer> generujZakresyICeny()
	{
		Map<Zakres, Integer> wynik = new HashMap<Zakres, Integer>();
		Random r = new Random();
		Zakres z = new Zakres();
		z.min = 1;
		z.max = 1;
		Integer cena = r.nextInt(100);
		wynik.put(z, cena);
		return wynik;
	}
	public static int saldo;
	
	public static void wypiszPrzedmioty()
	{
		Set<Entry<String, Cennik>> zbiorPrzedmiotow = przedmioty.entrySet();
		for (Entry<String, Cennik> wpis : zbiorPrzedmiotow)
		{
			String przedmiot = wpis.getKey();
			Cennik cennik = wpis.getValue();
			wypiszCennik(przedmiot, cennik);
		}
	}
	
	private static void wypiszCennik(String przedmiot, Cennik cennik)
	{
		TypZajec[] typy = TypZajec.values();
		System.out.println("Przedmiot = "+przedmiot);
		for (TypZajec typ : typy)
		{
			wypiszTypZajec(typ, cennik.cena(typ));
		}
	}
	
	private static void wypiszTypZajec(TypZajec typ, Map<Zakres, Integer> mapa)
	{
		System.out.println("Typ zajec = "+typ);
		Set<Entry<Zakres, Integer>> zbior = mapa.entrySet();
		for (Entry<Zakres, Integer> wpis : zbior)
		{
			Zakres z = wpis.getKey();
			Integer cena = wpis.getValue();
			System.out.println(z+" = "+cena);
		}
	}
}
