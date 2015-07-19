package model;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Predicate;

import kolekcje.Kolekcje;
import pomoc.Tablice;
import mapy.Maper;
import mapy.Mapy;
import zrodla_danych.BazaDanych;
import zrodla_danych.BazaDanych.WyjatekBazyDanych;
import zrodla_danych.mysql.Zapytania;

public abstract class Model
{
	/**
	 * uuid nowego obiektu
	 */
	public final UUID uuid;
	/**
	 * ustawia modelowi obiekt implementujacy interfejs bazy danych
	 * @param bazaDanych obiekt implementujacy baze danych
	 */
	public final static void ustawBazeDanych(BazaDanych bazaDanych)
	{
		Model.bazaDanych = bazaDanych;
	}

	/**
	 * tworzy nowy model. Model jest nowy
	 */
	public Model()
	{
		this.uuid = UUID.randomUUID();
		this.nowy = true;
	}

	/**
	 * odtwarza istniejacy model z bazy danych na podstawie uuid. Model jest stary
	 * @param uuid
	 */
	public Model(UUID uuid) throws WyjatekBazyDanych
	{
		this.uuid = uuid;
		this.nowy = false;
		this.daneZBazyDanych = bazaDanych.uzyskajMape(this.uzyskajZapytanie());
		this.wczytaj();
	}

	public final void zapisz() throws WyjatekBazyDanych
	{
		if (this.predykatMetodyUzyskujacej == null)
		{
			String nazwaMetodyUzyskujacej = this.uzyskajNazweMetodyUzyskujacej();
			this.predykatMetodyUzyskujacej = new PredykatMetodyUzyskujacej(nazwaMetodyUzyskujacej);
		}
		this.przygotujDoZapisu();
		if (this.nowy)
		{
			this.wykonajJesliNowy();
			this.nowy = false;
		}
		else if (this.czyZmieniony())
		{
			this.wykonajJesliZmieniony();
			this.nazwyPolOZmienionejWartosci.clear();
			this.zmienionePolaIWartosci.clear();
		}
	}

	public final long usun() throws WyjatekBazyDanych
	{
		String tabela, warunek;
		long wynik;
		
		tabela = this.uzyskajTabele();
		warunek = uzyskajWarunek();
		wynik = bazaDanych.usun(tabela, warunek);
		
		return wynik;
	}

	/**
	 * zmienia zapewniaj¹ca dostêp do bazy danych modelowi i metodom statycznym
	 */
	protected static BazaDanych bazaDanych;
	protected static String uzyskajNazweMetodyUzyskujacej()
	{
		return nazwaMetodyUzyskujacej;
	}

	/**
	 * Zawiera pola, ktore zostaly zmodyfikowane.<br />
	 * Funkcje typu settery powinny dodawaæ nazwê zmienionego pola
	 */
	protected final Collection<String> nazwyPolOZmienionejWartosci = new HashSet<>();
	/**
	 * Zawiera pola, ktore zostaly zmodyfikowane.<br />
	 * Funkcje typu settery powinny dodawaæ nazwê zmienionego pola
	 */
	protected final Collection<String> nazwyMetodOZmienionejWartosci = new HashSet<>();
	/**
	 * Zawiera pola i ich wartosci, ktore zostaly zmodyfikowane.<br />
	 * Funkcje typu settery powinny dodawaæ nazwê i wartoœæ zmienionego pola
	 */
	protected final Map<String, Object> zmienionePolaIWartosci = new HashMap<>();
	/**
	 * ustawia polom wartoœci
	 * @param dane zrodlo nazw pol i wartosci
	 * @return siebie
	 * @throws NoSuchFieldException rzuca jesli nie ma pola o takiej nazwie
	 * @throws SecurityException rzuca w przypadku naruszenia bezpieczenstwa
	 * @throws IllegalArgumentException rzuca jesli dane pole jest innego typu niz podana wartosc
	 * @throws IllegalAccessException rzuca jesli do pola nie ma dostepu
	 */
	protected final Model ustawPola(Map<String, Object> dane)
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException
	{
		String nazwaPola;
		Object wartoscPola;
	
		for (Entry<String, Object> kluczWartosc : dane.entrySet())
		{
			nazwaPola = kluczWartosc.getKey();
			wartoscPola = kluczWartosc.getValue();
			this.ustawPole(nazwaPola, wartoscPola);
		}
		
		return this;
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
	protected final Model ustawPole(String nazwa, Object wartosc) 
			throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException
	{
		Collection<Field> pola;
		Map<String, Field> mapa;
		Field pole;
		
		pola = this.uzyskajPola();
		mapa = Mapy.mapujPrzez(pola, maperPol);
		pole = mapa.get(nazwa);
		if (pole == null)
		{
			throw new NoSuchFieldException(nazwa);
		}
		pole.set(this, wartosc);
		
		return this;
	}

	protected final Model ustawMetode(String nazwa, Object wartosc) 
			throws SecurityException, 
			IllegalArgumentException, IllegalAccessException, 
			NoSuchMethodException, InvocationTargetException
	{
		Collection<Method> metody;
		Map<String, Method> mapa;
		Method metoda;
		
		metody = this.uzyskajMetody();
		mapa = Mapy.mapujPrzez(metody, maperMetod);
		metoda = mapa.get(nazwa);
		if (metoda == null)
		{
			throw new NoSuchMethodException(nazwa);
		}
		metoda.invoke(this, wartosc);
		
		return this;
	}

	protected final Model ustawMetody(Map<String, Object> dane) 
			throws SecurityException,
			IllegalArgumentException, IllegalAccessException, 
			NoSuchMethodException, InvocationTargetException
	{
		String nazwaPola;
		Object wartoscPola;
	
		for (Entry<String, Object> kluczWartosc : dane.entrySet())
		{
			nazwaPola = kluczWartosc.getKey();
			wartoscPola = kluczWartosc.getValue();
			this.ustawMetode(nazwaPola, wartoscPola);
		}
		
		return this;
	}

	protected final boolean czyNowy()
	{
		return this.nowy;
	}

	protected final Map<String, String> uzyskajZmienioneDane()
	{
		Map<String, Object> suroweWartosciZNazwPol, suroweWartosciZNazwIWartosciPol, polaczenie;
		Map<String, String> wynik;
		
		suroweWartosciZNazwPol = this.uzyskajMapeZeZmienionychNazwPol();
		suroweWartosciZNazwIWartosciPol = this.uzyskajMapeZeZmienionychPolIWartosci();
		polaczenie = Mapy.polacz(suroweWartosciZNazwIWartosciPol, suroweWartosciZNazwPol);
		wynik = konwertujDoFormatuBazyDanych(polaczenie);
	
		return wynik;
	}

	private final Map<String, Object> uzyskajMapeZeZmienionychPolIWartosci()
	{
		Set<Entry<String, Object>> zbiorZmienionychPolIWartosci;
		Map<String, Object> wynik;
		String nazwaPola;
		Object wartoscPola;
		
		zbiorZmienionychPolIWartosci = this.zmienionePolaIWartosci.entrySet();
		wynik = new HashMap<>();
		for (Entry<String, Object> nazwaPolaIWartosc : zbiorZmienionychPolIWartosci)
		{
			nazwaPola = nazwaPolaIWartosc.getKey();
			wartoscPola = nazwaPolaIWartosc.getValue();
			wynik.put(nazwaPola, wartoscPola);
		}
		
		return wynik;
		
	}

	private final Map<String, Object> uzyskajMapeZeZmienionychNazwPol()
	{
		Map<String, Object> wynik;
		Object wartoscPola;
		
		wynik = new HashMap<>();
		for (String nazwaPola : this.nazwyPolOZmienionejWartosci)
		{
			try
			{
				wartoscPola = this.uzyskajSurowaWartoscMetody(nazwaPola);
				wynik.put(nazwaPola, wartoscPola);
			}
			catch (NoSuchFieldException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e)
			{
			}
			
		}
		
		return wynik;
	}

	private Object uzyskajSurowaWartoscMetody(String nazwaPola) 
			throws NoSuchFieldException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException
	{
		Collection<Method> metody;
		Map<String, Method> mapa;
		Method metoda;
		String nazwaMetody;
		Object wartoscPola;
		
		metody = this.uzyskajMetody();
		mapa = Mapy.mapujPrzez(metody, maperMetod);
		nazwaMetody = this.uzyskajNazweMetodyUzyskujacej(nazwaPola);
		metoda = mapa.get(nazwaMetody);
		if (metoda == null)
		{
			throw new NoSuchFieldException(nazwaMetody);
		}
		wartoscPola = metoda.invoke(this);
		
		return wartoscPola;
	}

	private String uzyskajNazweMetodyUzyskujacej(String nazwaPola)
	{
		String nazwaPolaWModelu, wynik;
		
		nazwaPolaWModelu = this.mapujPoleDoModelu(nazwaPola);
		wynik = nazwaMetodyUzyskujacej + nazwaPolaWModelu;
		
		return wynik;
	}

	protected final Map<String, String> uzyskajNoweDane()
	{
		Map<String, Object> suroweWartosci, filtrowaneWartosci;
		Collection<String> wykluczonePola;
		Map<String, String> wynik;
	
		suroweWartosci = this.uzyskajSuroweWartosciPolIWartosciMetodUzyskujacych();
		wykluczonePola = this.uzyskajWykluczonePola();
		filtrowaneWartosci = Mapy.wykluczKlucze(suroweWartosci, wykluczonePola);
		wynik = konwertujDoFormatuBazyDanych(filtrowaneWartosci);
		this.zmienNowePola(wynik);
		
		return wynik;
	}

	/**
	 * dane z bazy danych
	 */
	protected Map<String, String> daneZBazyDanych;
	protected String uzyskajZapytanie()
	{
		return Zapytania.uzyskajZaznaczenie(this.uzyskajTabele(), this.uuid);
	}

	protected void wykonajJesliZmieniony() throws WyjatekBazyDanych
	{
		String tabela;
		Map<String, String> dane;
		String warunek;
		
		tabela = this.uzyskajTabele();
		dane = uzyskajZmienioneDane();
		warunek = Zapytania.warunek("uuid", this.uuid.toString());
		bazaDanych.zmien(tabela, dane, warunek);
		
	}

	/**
	 * wykonuje te funkcje jesli ma wykonac jakies niestandardowe operacje przy wstawieniu do bazy
	 * @throws WyjatekBazyDanych rzuca w wyniku jakiegokolwiek bledu bazy
	 */
	protected void wykonajJesliNowy() throws WyjatekBazyDanych
	{
		String tabela;
		Map<String, String> dane;
		
		tabela = this.uzyskajTabele();
		dane = this.uzyskajNoweDane();
		bazaDanych.wstaw(tabela, dane);
	}

	protected Collection<String> uzyskajWykluczonePola()
	{
		Collection<String> wynik;
		
		wynik = new ArrayList<>();
		
		return wynik;
	}

	protected String uzyskajWarunek()
	{
		return Zapytania.warunek("uuid", this.uuid);
	}

	protected Map<String, String> uzyskajMapeMapowaniaDoBazyDanych()
	{
		Map<String, String> wynik;
		
		wynik = new HashMap<>();
		
		return wynik;
	}

	protected abstract void wczytaj();

	protected abstract String uzyskajTabele();

	protected abstract String mapujPoleDoBazyDanych(String nazwaPola);

	protected abstract String mapujPoleDoModelu(String nazwaPola);

	protected abstract void przygotujDoZapisu();

	protected abstract void zmienNowePola(Map<String, String> obecnePola);

	protected abstract void zmienZmienionePola(Map<String, String> obecnePola);

	private boolean nowy;
	private static final String nazwaMetodyUzyskujacej = "uzyskaj";
	private static final String nazwaMetodyUstawiajacej = "ustaw";
	private static PredykatMetodyUzyskujacej predykatMetodyUzyskujacej;
	
	private static PredykatMetodyUstawiajacej predykatMetodyUstawiajacej;
	
	private final static PredykatPublicznegoPola predykatPol = new PredykatPublicznegoPola();
	
	private final static Maper<String, Field> maperPol = new Maper<String, Field>(){
		@Override
		public String mapuj(Field obiekt)
		{
			return obiekt.getName();
		}
	};
	
	

	private final Maper<String, Method> maperMetod = new Maper<String, Method>(){
		@Override
		public String mapuj(Method metoda)
		{
			return uzyskajNazwePolaZMetodyUzyskujacej(metoda);
		}
	};
	
	private final Predicate<Entry<String, String>> predykat = new Predicate<Entry<String, String>>(){
		@Override
		public boolean test(Entry<String, String> wpis)
		{
			String nazwaPola;
			boolean wynik;
			
			nazwaPola = wpis.getKey();
			wynik = nazwyPolOZmienionejWartosci.contains(nazwaPola);
			
			return wynik;
		}
		
	};
	
	private static final Map<String, String> konwertujDoFormatuBazyDanych(Map<String, Object> mapaSurowychWartosci)
	{
		Map<String, String> mapaDocelowa;
		Set<Entry<String, Object>> zbior;
		String klucz, swartosc;
		Object wartosc;
		
		mapaDocelowa = new HashMap<>();
		zbior = mapaSurowychWartosci.entrySet();
		for (Entry<String, Object> wpis : zbior)
		{
			klucz = wpis.getKey();
			wartosc = wpis.getValue();
			swartosc = wartosc.toString();
			mapaDocelowa.put(klucz, swartosc);
		}
		
		return mapaDocelowa;
	}

	private static String uzyskajNazwePolaZMetodyUzyskujacej(Method metoda)
	{
		String nazwaMetody, nazwaMetodyUzyskujacej, wynik;
		int pozycja;
		
		nazwaMetody = metoda.getName();
		nazwaMetodyUzyskujacej = predykatMetodyUzyskujacej.uzyskajNazweMetodyUzyskujacej();
		pozycja = nazwaMetody.lastIndexOf(nazwaMetodyUzyskujacej);
		wynik = nazwaMetody.substring(0, pozycja);
		
		return wynik;
		
	}

	private final boolean czyZmieniony()
	{
		boolean zmienionoPola, zmienionoPolaIWartosci, wynik;
		
		zmienionoPola = !this.nazwyPolOZmienionejWartosci.isEmpty();
		zmienionoPolaIWartosci = !this.zmienionePolaIWartosci.isEmpty();
		wynik = zmienionoPola && zmienionoPolaIWartosci;
		
		return wynik;
	}

	private final Map<String, Object> uzyskajSuroweWartosciPolIWartosciMetodUzyskujacych()
	{
		Map<String, Object> mapaSurowychWartosciPol, mapaSurowychWartosciMetodUzyskujacych, wynik;
		
		mapaSurowychWartosciPol = this.uzyskajSuroweWartosciPublicznychPol();
		mapaSurowychWartosciMetodUzyskujacych = this.uzyskajSuroweWartosciMetodUzyskujacych();
		wynik = new HashMap<>();
		wynik.putAll(mapaSurowychWartosciPol);
		wynik.putAll(mapaSurowychWartosciMetodUzyskujacych);
		
		return wynik;
	}

	private final Map<String, Object> uzyskajSuroweWartosciMetodUzyskujacych()
	{
		Map<String, Object> wynik;
		Collection<Method> metody;
		String nazwaMetody;
		Object wartoscMetody;
		
		wynik = new HashMap<>();
		metody = this.uzyskajMetody();
		metody = Kolekcje.filtruj(metody, this.predykatMetodyUzyskujacej);
		for (Method metoda : metody)
		{
			nazwaMetody = uzyskajNazwePolaZMetodyUzyskujacej(metoda);
			try
			{
				wartoscMetody = this.uzyskajSurowaWartoscMetody(metoda);
				wynik.put(nazwaMetody, wartoscMetody);
			}
			catch (IllegalArgumentException | IllegalAccessException
				| SecurityException | InvocationTargetException e)
			{
			}
		}
		
		return wynik;
	}

	private final Map<String, Object> uzyskajSuroweWartosciPublicznychPol()
	{
		Map<String, Object> dane;
		Collection<Field> pola;
		String nazwaPola;
		Object wartoscPola;
		
		dane = new HashMap<>();
		pola = this.uzyskajPola();
		for (Field pole : pola)
		{
			nazwaPola = pole.getName();
			try
			{
				wartoscPola = this.uzyskajSurowaWartoscPola(pole);
				dane.put(nazwaPola, wartoscPola);
			}
			catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e)
			{
			}
		}
		
		return dane;
	}

	private final Object uzyskajSurowaWartoscPola(Field pole)
			throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException
	{
		Object wartoscPola;
		
		wartoscPola = pole.get(this);
		
		return wartoscPola;
	}

	private final Collection<Field> uzyskajPola()
	{
		Field[] pola;
		Collection<Field> wynik;
		Class<?> klasa = this.getClass();
		
		pola = klasa.getFields();
		wynik = Kolekcje.toList(pola);
		wynik = Kolekcje.filtruj(wynik, predykatPol);
		
		return wynik;
	}

	private final Collection<Method> uzyskajMetody()
	{
		Method[] metody;
		Collection<Method> wynik;
		Class<?> klasa;
		
		klasa= this.getClass();
		metody = klasa.getMethods();
		wynik = Kolekcje.toList(metody);
		wynik = Kolekcje.filtruj(wynik, predykatMetodyUzyskujacej);
		
		return wynik;
	}

	private final Object uzyskajSurowaWartoscMetody(Method metoda) 
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException
	{
		Object wartoscMetody;
		
		wartoscMetody = metoda.invoke(this);
		
		return wartoscMetody;
	}
	
}
