package mojajava;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;



import kolekcje.Kolekcje;
import pomoc.Tablice;
import mapy.Maper;
import mapy.Mapy;

public class Refleksje
{
	/**
	 * ustawia polom warto�ci
	 * @param dane zrodlo nazw pol i wartosci
	 * @return siebie
	 * @throws NoSuchFieldException rzuca jesli nie ma pola o takiej nazwie
	 * @throws SecurityException rzuca w przypadku naruszenia bezpieczenstwa
	 * @throws IllegalArgumentException rzuca jesli dane pole jest innego typu niz podana wartosc
	 * @throws IllegalAccessException rzuca jesli do pola nie ma dostepu
	 */
	public static void ustawPola(Object obiekt, Map<String, Object> dane) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		String nazwaPola;
		Object wartoscPola;

		for (Entry<String, Object> kluczWartosc : dane.entrySet())
		{
			nazwaPola = kluczWartosc.getKey();
			wartoscPola = kluczWartosc.getValue();
			ustawPole(obiekt, nazwaPola, wartoscPola);
		}
	}
	
	/**
	 * ustawia konkretnemu polu konkretna wartosc
	 * @param nazwa nazwaPola
	 * @param wartosc wartoscPola
	 * @return siebie
	 * @throws NoSuchFieldException rzuca jesli nie ma pola o takiej nazwie
	 * @throws SecurityException rzuca w przypadku naruszenia bezpieczenstwa
	 * @throws IllegalArgumentException rzuca jesli dane pole jest innego typu niz podana wartosc
	 * @throws IllegalAccessException rzuca jesli do pola nie ma dostepu
	 */
	public static void ustawPole(Object obiekt, String nazwa, Object wartosc) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		Collection<Field> pola;
		Map<String, Field> mapa;
		Field pole;
		
		pola = uzyskajPola(obiekt);
		mapa = Mapy.mapujPrzez(pola, maperPol);
		pole = mapa.get(nazwa);
		if (pole == null)
		{
			throw new NoSuchFieldException();
		}
		pole.set(obiekt, wartosc);
	}
	
	public static Map<String, Object> uzyskajSuroweWartosciPol(Object obiekt)
	{
		Class<?> klasa;
		Map<String, Object> dane;
		Collection<Field> pola;
		String nazwaPola;
		Object wartoscPola;
		boolean dostepne;
		
		klasa = obiekt.getClass();
		dane = new HashMap<>();
		pola = uzyskajPola(klasa);
		for (Field pole : pola)
		{
			dostepne = pole.isAccessible();
			pole.setAccessible(true);
			nazwaPola = pole.getName();
			try
			{
				wartoscPola = uzyskajSurowaWartoscPola(obiekt, nazwaPola);
				dane.put(nazwaPola, wartoscPola);
				pole.setAccessible(dostepne);
			}
			catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		
		return dane;
	}
	
	/**
	 * uzyskuje wartosc pola dla pola o danej nazwie
	 * @param nazwa nazwa pola
	 * @return wartosc pola
	 * @throws NoSuchFieldException rzuca jesli nie ma pola o takiej nazwie
	 * @throws SecurityException rzuca w przypadku naruszenia bezpieczenstwa
	 * @throws IllegalArgumentException rzuca jesli dane pole jest innego typu niz podana wartosc
	 * @throws IllegalAccessException rzuca jesli do pola nie ma dostepu
	 */
	public static Object uzyskajSurowaWartoscPola(Object obiekt, String nazwa) 
			throws NoSuchFieldException, SecurityException,
					IllegalArgumentException, IllegalAccessException
	{
		Collection<Field> pola;
		Map<String, Field> mapa;
		Field pole;
		Object wartoscPola;
		
		pola = uzyskajPola(obiekt);
		mapa = Mapy.mapujPrzez(pola, maperPol);
		pole = mapa.get(nazwa);
		if (pole == null)
		{
			throw new NoSuchFieldException(nazwa);
		}
		wartoscPola = pole.get(obiekt);
		
		return wartoscPola;
	}
	
	public static Collection<Field> uzyskajPola(Object obiekt)
	{
		Class<?> klasa;
		Field[] pola;
		Collection<Field> wynik;
		
		klasa = obiekt.getClass();
		pola = klasa.getFields();
		wynik = Kolekcje.toList(pola);
		
		return wynik;
	}
	
	public static Collection<Method> uzyskajMetody(Object obiekt)
	{
		Class<?> klasa;
		Method[] metody;
		Collection<Method> wynik;
		
		klasa = obiekt.getClass();
		metody = klasa.getMethods();
		wynik = Kolekcje.toList(metody);
		
		return wynik;
	}
	
	public static Maper<String, Field> maperPol = new Maper<String, Field>(){
		@Override
		public String mapuj(Field obiekt)
		{
			return obiekt.getName();
		}
	};
	
	public static Maper<String, Method> maperMetod = new Maper<String, Method>(){
		@Override
		public String mapuj(Method obiekt)
		{
			return obiekt.getName();
		}
	};
	
	public static Map<String, Object> 
		uzyskajWartosciPolPrzezMetody(Object obiekt, String czescNazwy)
	{
		Map<String, Object> wynik;
		Collection<Method> metody;
		
		//metody = uzyskajGettery(obiekt);
		wynik = new HashMap<>();
		
		return wynik;
	}
	
	public static Collection<Method> uzyskajGettery(Object obiekt)
	{
		Collection<Method> metody;
		Class<?> wynikMetody;
		boolean czyProcedura;
		Collection<Method> wynik;
		
		wynik = new ArrayList<>();
		metody = Refleksje.uzyskajMetody(obiekt);
		for (Method metoda : metody)
		{
			wynikMetody = metoda.getReturnType();
			czyProcedura = wynikMetody.equals(Void.TYPE);
			if (czyProcedura)
			{
				wynik.add(metoda);
			}
		}
		
		return wynik;
	}
	
	public static Collection<Method> uzyskajSettery(Object obiekt)
	{
		Collection<Method> metody;
		Class<?> wynikMetody;
		boolean czyProcedura;
		Collection<Method> wynik;
		
		wynik = new ArrayList<>();
		metody = Refleksje.uzyskajMetody(obiekt);
		for (Method metoda : metody)
		{
			wynikMetody = metoda.getReturnType();
			czyProcedura = wynikMetody.equals(Void.TYPE);
			if (!czyProcedura)
			{
				wynik.add(metoda);
			}
		}
		
		return wynik;
	}
}
