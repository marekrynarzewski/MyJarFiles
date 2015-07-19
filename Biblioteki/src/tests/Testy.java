package tests;

import io.Ekran;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;











import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import json.JSONObject;
import kolekcje.Kolekcje;
import ks.Edk;
import liczby.Zakres;
import dego.DirectoryReader;
import dego.Losowanie;
import dego.MultiMap;
import firebase.Synchronizer;
import firebase4j.error.FirebaseException;
import adnotacje.Adnotacje.KreatorAdnotacji;
import benford.Prawo;
import pomoc.Macierze;
import pomoc.Tablice;
import rpi.PolskiStemmer;
import mapy.Mapy;
import mapy.countery.CounterNumber;
import model.eng.FieldPredicates;
import model.eng.FieldPredicates.PublicPredicate;
import model.eng.FieldPredicates.StaticPredicate;
import myutils.Plik;

public class Testy
{
	public static void main(String[] args)
	{
		testStemmera();
	}
	
	public static void testStemmera()
	{
		PolskiStemmer ps = new PolskiStemmer();
		String imie = Ekran.czytajLinie();
		while (!imie.equals("0"))
		{
			ps.imie(imie);
			imie = Ekran.czytajLinie();
		}
	}
	public static void testBenforda()
	{
		String sciezka = "Benford";
		try
		{
			Prawo prawo = new benford.Prawo(sciezka, ".log;.csv");
			prawo.przetworz();
			prawo.zapisz(sciezka);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void testDirectoryReader()
	{
		Vector<File> allFilesOnDiskC = DirectoryReader.listFilesFrom(new File("c:/"));
		System.out.println(allFilesOnDiskC.size());
		
		Collections.shuffle(allFilesOnDiskC);
		MultiMap<String, Long> wynik = new MultiMap<String, Long>();
		if (allFilesOnDiskC.size() > 0)
		{
			for (int i = 1; i <= 100; i++)
			{
				File plik = allFilesOnDiskC.firstElement();
				System.out.println(plik);
				String sciezka = plik.getName();
				Long rozmiar = plik.length();
				wynik.put(sciezka, rozmiar);
				Collections.shuffle(allFilesOnDiskC);
			}
			Plik.mapaDoPliku("375250.txt", wynik);
		}
	}
	
	public static void testLosowania()
	{
		String ROOT = "C:/";
		FileVisitor<Path> fileProcessor = new Losowanie.ProcessFile();
		try
		{
			Files.walkFileTree(Paths.get(ROOT), fileProcessor);
		}
		catch (IOException e){}
		if (Losowanie.pliki.size() > 0)
		{
			System.out.println("Wszystkich plików na dysku masz "+Losowanie.pliki.size());
			Collection<String> nazwy = Losowanie.pliki.keySet();
			Vector<String> nazwyv = new Vector<String>(nazwy);
			Collections.shuffle(nazwyv);
			Map<String, Long> wynik = new HashMap<String, Long>();
			for (int i = 1; i <= 100; i++)
			{
				String plik = nazwyv.firstElement();
				wynik.put(plik, Losowanie.pliki.get(plik));
				Collections.shuffle(nazwyv);
			}
			Plik.mapaDoPliku("wynikiLosowania3.txt", wynik);
		}
	}

	public static void testSynchronizer()
	{
		JSONObject j;
		try
		{
			j = Synchronizer.baza.obiektJSON("data/tiktak/alerts");
			System.out.println(j.toString(1));
			Collection<JSONObject> coll = Synchronizer.getObjects(j);
			System.out.println(coll);
			coll = Synchronizer.filterByLastModifiedDates(coll);
			System.out.println(coll);
		}
		catch (FirebaseException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void testEDK(String[] args)
	{
		Edk.inicjalizujPrzedmioty();
		Edk.saldo = 5000;
		Edk.wypiszPrzedmioty();
	}
	
	 
	 
 	public static void testMacierzy() throws Exception
	{
		Integer[] listaz1na2 = new Integer[]{0, 1, 0, 0, 0, 0};
		Integer[] listaz1na1i3 = new Integer[]{1, 0, 1, 0, 0, 0};
		Integer[][] m = new Integer[6][6];
		m[0] = listaz1na2;
		m[5] = Macierze.przesunWPrawo(listaz1na2, 3);
		for (int i = 1; i <= 4; i++)
		{
			m[i] = listaz1na1i3;
			listaz1na1i3 = Macierze.przesunWPrawo(listaz1na1i3, 1);
		}
		m = Macierze.potega(m, 3);
		System.out.println(Macierze.toString(m));
	}
 	
 	public static void testPredykatowPol()
	{
 		FieldPredicates fp;
 		Field[] fields;
 		Collection<Field> rf;
 		
 		fp = new FieldPredicates();
 		fp.add(new PublicPredicate(true));
 		fp.add(new StaticPredicate(false));
 		fields = A.class.getFields();
 		rf = Kolekcje.toList(fields);
		rf = rf.stream().filter(fp).collect(Collectors.<Field>toList());
		System.out.println(rf);
	}
 	
 	public static class A
 	{
 		public int i;
 		protected static float j;
 		public static String k;
 		private static long s;
 	}
 	
 	public static void testKreatorAdnotacji(String[] args)
	{
 		KreatorAdnotacji ka;
 		String rodzajeAdnotacji, typyIMetodyRazem;
 		ElementType[] rodzajeElementow;
 		ElementType typElementu;
 		int j;
 		String[] rodzajAdnotacji, typyIMetody, typIMetoda;
 		Class<?> typZwracany;
 		
 		ka = new KreatorAdnotacji(args[1]);
 		ka.ustawPakiet(args[2]);
 		rodzajeAdnotacji = args[3];
 		rodzajAdnotacji = rodzajeAdnotacji.split(",");
 		rodzajeElementow = new ElementType[rodzajAdnotacji.length];
 		j = 0;
 		for (String element : rodzajAdnotacji)
 		{
 			typElementu = ElementType.valueOf(element);
  			rodzajeElementow[j] = typElementu;
 			j++;
 		}
 		ka.ustawRodzaje(rodzajeElementow);
 		typyIMetodyRazem = args[4];
 		typyIMetody = typyIMetodyRazem.split(";");
 		for (String typIMetodaRazem : typyIMetody)
 		{
 			typIMetoda = typIMetodaRazem.split(":");
 			try
			{
				typZwracany = Class.forName(typIMetoda[1]);
				ka.dodajSygnatureMetody(typZwracany, typIMetoda[1]);
			}
			catch (ClassNotFoundException e)
			{
				System.out.println(e.getMessage());
			}
 		}
 		
 		try
		{
			ka.zapisz();
		}
		catch (IOException e)
		{
			System.out.println("B³¹d zapisu."+e.getMessage());
		}
 		
	}
 	
 	public static void testEnergii()
	{
 		int energiaZycia = 100;
 		int liczbaIteracji = 1;
 		while (energiaZycia > 0 && liczbaIteracji < 100)
 		{
 			String format = "Dzieñ %d; Energia: %d%%";
 			String wypis = String.format(format, liczbaIteracji, energiaZycia);
 			System.out.println(wypis);
 			energiaZycia = losowoZmien(energiaZycia, 5);
 			liczbaIteracji++;
 		}
	}
 	
 	public static void testModeluEng()
	{
 		
	}
 	
 	public static int losowoZmien(int liczba, int krok)
 	{
 		List<Integer> oIle = Zakres.zakres(liczba-krok, liczba+krok);
 		Collections.shuffle(oIle);
 		int wynik = oIle.get(0);
 		if (wynik > 100)
 		{
 			wynik = 100;
 		}
 		if (wynik < 0)
 		{
 			wynik = 0;
 		}
 		return wynik;
 	}
 	
 	public static void testCountera()
 	{
 		Collection<String> cs = Arrays.asList("a", "a", "b", "b", "c", "e");
 		CounterNumber<String> csi = new CounterNumber<String>(cs);
 		Iterator<String> i = csi.elements();
 		while (i.hasNext())
 		{
 			System.out.println(i.next());
 		}
 		System.out.println(csi.mostCommon());
 		System.out.println(csi.mostCommon(2));
 	}
 	
 	public static void testSortowaniaMapy()
 	{
 		 HashMap<String,Double> map = new HashMap<String,Double>();
         map.put("A",99.5);
         map.put("B",67.4);
         map.put("C",67.4);
         map.put("D",67.3);

         System.out.println("unsorted map: "+map);

         Map<String, Double> sorted_map = Mapy.sortByValues(map, new Comparator<Double>(){

			@Override
			public int compare(Double arg0, Double arg1)
			{
				int comparison;
				int result;
				
				comparison =  arg0.compareTo(arg1);
				if (comparison != 0)
				{
					result = comparison;
				}
				else
				{
					result = 1;
				}
				
				return result;
			}
        	 
         });

         System.out.println("results: "+sorted_map);
 	}
  	
 	
 	
}
