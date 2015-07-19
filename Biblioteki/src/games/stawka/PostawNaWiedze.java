package games.stawka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import pomoc.Para;
import pomoc.Tablice;
import io.Ekran;
import io.Plik;

public class PostawNaWiedze
{
	private boolean koniecGry = false;
	private boolean kontynuacja = true;
	private boolean pierwszaGra = true;
	private final Map<String, String> pytania;
	private final List<String> doUzycia;
	private static final String sciezkaDoBazyPytan = "pytania_do_stawki.txt";
	private static final String zwyciestwa = "zwyciestwa.txt";
	private String imieGracza;
	private int stawka;
	private int prob = 3;
	private int zachowek;
	private boolean sukces;
	private int numerGry = 1;
	private int wygrana = 0;

	private PostawNaWiedze() throws IOException
	{
		int stawka = Ekran.promptInt("Pierwsza stawka: ").getKey();
		if (stawka > 0)
		{
			this.stawka = stawka;
		}
		else
		{
			this.stawka = 2;
		}
		System.out.println("Witaj w grze PostawNaWiedze");
		this.imieGracza = Ekran.promptString("Twoje imie: ").getKey();
		System.out.println("Witaj "+this.imieGracza);
		this.pytania = Plik.readByLines(sciezkaDoBazyPytan);
		this.doUzycia = Tablice.asList(this.pytania.keySet());
		Collections.shuffle(this.doUzycia);
	}
	
	private void informacjeOWygranej() throws IOException
	{
		Ekran.wypiszZLinia("Brawo "+this.imieGracza);
		String tekstDoZapisania = this.wygrana+" w "+this.numerGry+" grach";
		Ekran.wypiszZLinia("Wygra³eœ "+tekstDoZapisania);
		Plik.dopisz(zwyciestwa, this.imieGracza+" wygra³ "+tekstDoZapisania+"\n");
	}
	
	private void zapytajOKontynuacje()
	{
		if (this.koniecGry)
		{
			this.kontynuacja = false;
			return;
		}
		this.kontynuacja = Ekran.promptBoolean("Czy chcesz kontynuowac gre? ").getKey();
	}
	private void stanGry()
	{
		System.out.println("Numer gry: "+this.numerGry);
		System.out.println("Wygrana: "+this.wygrana);
	}
	
	private void zapytajOStawke()
	{
		if (this.pierwszaGra)
		{
			System.out.println("Stawka: "+this.stawka);
			this.pierwszaGra = false;
			return;
		}
		int stawka = Ekran.promptInt("Stawka: ").getKey();
		boolean w1 = (stawka > 0);
		boolean w2 = (stawka <= this.wygrana);
		boolean poprawnaStawka = w1 && w2;
		if (!poprawnaStawka && this.prob > 0)
		{
			this.prob -= 1;
			this.zapytajOStawke();
		}
		else if (this.prob == 0)
		{
			System.out.println("Przekroczono liczbe blednych stawek!");
			System.exit(-3);
		}
		else
		{
			this.stawka = stawka;
		}
	}
	
	private void informacjaOZachowkuISukcesie() 
	{
		this.obliczZachowek();
		System.out.println("Zachowek: "+this.zachowek);
		this.losujSukces();
		System.out.println("Sukces: "+this.sukces);
		this.obliczWygrana();
	}
	
	private void obliczWygrana()
	{
		if (this.sukces)
		{
			this.wygrana += this.stawka;
		}
		else
		{
			int wygrana = this.wygrana - this.stawka;
			if (wygrana <= 0)
			{
				wygrana = 0;
				this.koniecGry = true;
			}
			this.wygrana = wygrana;
		}
	}

	private void obliczZachowek()
	{
		int zachowek = this.wygrana - this.stawka;
		if (zachowek < 0)
		{
			zachowek = 0;
		}
		this.zachowek = zachowek;
	}
		
	private void losujSukces()
	{
		Entry<String, String> pytanieIOdpowiedz;
		String pytanie, poprawna, odpowiedz;
		
		pytanieIOdpowiedz = this.losujPytanie();
		if (pytanieIOdpowiedz == null)
		{
			this.koniecGry = true;
			return;
		}
		pytanie = pytanieIOdpowiedz.getKey();
		System.out.println("Pytanie: "+pytanie);
		odpowiedz = Ekran.promptString("Odpowiedz: ").getKey();
		poprawna = pytanieIOdpowiedz.getValue();
		this.sukces = poprawna.equals(odpowiedz);
		System.out.println("Poprawna odpowiedz: \""+poprawna+"\", Twoja odpowiedz: \""+odpowiedz+"\"");
	}
	
	private void nastepnaGra()
	{
		if (!this.koniecGry)
		{
			this.numerGry++;
		}
	}
	
	private Entry<String, String> losujPytanie()
	{
		String pytanie, odpowiedz;
		Entry<String, String> wynik;
		
		if (this.doUzycia.isEmpty())
		{
			this.koniecGry = true;
			return null;
		}
		pytanie = this.doUzycia.remove(0);
		odpowiedz = this.pytania.get(pytanie);
		wynik = new Para<>(pytanie, odpowiedz);
	
		return wynik;
	}
	public static void maintt(String[] args)
	{
		PostawNaWiedze gra;
		
		try
		{
			gra = new PostawNaWiedze();
			while (gra.kontynuacja)
			{
				gra.stanGry();
				gra.zapytajOStawke();
				gra.informacjaOZachowkuISukcesie();
				gra.nastepnaGra();
				gra.zapytajOKontynuacje();
			}
			gra.informacjeOWygranej();
		}
		catch (IOException e)
		{
			Ekran.blad(e.getMessage(), -3);
		}
		
	}
	
}
