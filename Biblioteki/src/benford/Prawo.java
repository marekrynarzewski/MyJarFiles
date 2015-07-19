package benford;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

public class Prawo
{
	private String sciezka;
	private File[] pliki;
	private Vector<String> logi = new Vector<String>();
	private Map<File, Double> podsumowania = new TreeMap<File, Double>();
	private boolean ignorujBledy = true;
	
	public Prawo(String sciezka, String wyklucz) throws FileNotFoundException
	{
		this.sciezka(sciezka);
		this.wczytajPlikiZeSciezki(wyklucz);
	}

	private void sciezka(String sciezka)
	{
		this.sciezka = sciezka;
		this.logi.add("Wczyta³em œcie¿kê '"+this.sciezka+"'");
	}

	private void wczytajPlikiZeSciezki(String wyklucz) throws FileNotFoundException
	{
		this.logi.add("Wczytujê pliki ze œcie¿ki z wykluczaniem");
		String[] pliki = wyklucz.split(";");
		this.logi.add("Okreœlam jakie pliki mam wykluczyæ");
		File sciezka = this.wyjatek();
		this.logi.add("Filtrujê pliki ze wzorcem '"+wyklucz+"'");
		FilenameFilter ff = new MyFilenameFilter(pliki);
		this.pliki = sciezka.listFiles(ff);
		this.logi.add("Wczyta³em pliki i wykluczy³em");
	}

	private File wyjatek() throws FileNotFoundException
	{
		this.logi.add("Okreœlam jakie pliki mam wykluczyæ");
		File sciezka = new File(this.sciezka);
		if (!sciezka.exists() || !sciezka.isDirectory())
		{
			String a = "sciezka nie istnieje lub nie jest katalogiem!";
			this.logi.add(a);
			throw new FileNotFoundException(a);
		}
		return sciezka;
	}

	static class MyFilenameFilter implements FilenameFilter
	{
		private String[]	pliki;
		public MyFilenameFilter(String[] pliki)
		{
			this.pliki = pliki;
		}
		@Override
		public boolean accept(File dir, String name)
		{
			for (String s: pliki)
			{
				if (name.contains(s))
					return false;
			}
			return true;
		}
		
	}

	public void przetworz() throws FileNotFoundException, Exception
	{
		this.przetworzPliki();
	}

	private void przetworzPliki() throws Exception
	{
		this.logi.add("Przetwarzam "+this.pliki.length+" plikow.");
		if (this.pliki != null)
		{
			this.logi.add("Pliki istniej¹");
			for (File plik : this.pliki)
			{
				this.przetworzPlik(plik);
			}
		}
	}

	private int[] inicjujPrawdopodobienstwo()
	{
		this.logi.add("Inicjujê prawdopodobieñstwo");
		int[] prawdopodobienstwo = new int[10];
		for (int i = 1; i <= 9; i++)
		{
			prawdopodobienstwo[i] = 0;
		}
		this.logi.add("Zainicjowa³em prawdopodobieñstwo");
		return prawdopodobienstwo;
	}
	
	private void przetworzPlik(File plik) throws Exception
	{
		this.logi.add("Przetwarzam plik '"+plik.getName()+"'");
		int[] prawdopodobienstwo = this.inicjujPrawdopodobienstwo();
		Scanner czytelnik = new Scanner(plik);
		this.logi.add("Utworzy³em czytelnika pliku '"+plik.getName()+"'");
		while (czytelnik.hasNext())
		{
			String rozmiar = czytelnik.next();
			this.logi.add("Odczyta³em '"+rozmiar+"'");
			try
			{
				char a = rozmiar.charAt(0);
				this.logi.add("Pobra³em 1. znak");
				Character b = new Character(a);
				String c = b.toString();
				this.logi.add("Konwertujê na java.lang.String");
				int cl = Integer.parseInt(c);
				this.logi.add("Uda³o siê sparsowaæ liczbê");
				if (cl != 0)
				{
					prawdopodobienstwo[cl] ++;
				}
			}
			catch(NumberFormatException e)
			{
				String a = "Nie uda³o siê skonwertowaæ '"+rozmiar+"' na liczbê";
				this.logi.add(a);
				if (!this.ignorujBledy)
				{
					czytelnik.close();
					throw new Exception(a);
				}
			}
		}
		double[] b = this.podsumuj(prawdopodobienstwo);
		double ocena = this.wyostrz(b);
		//System.out.println(String.format("%.3f", ocena));
		this.podsumowania.put(plik, 1-ocena);
		czytelnik.close();
	}

	private double[] podsumuj(int[] prawdopodobienstwo)
	{
		int suma = 0;
		double[] wynik = new double[10];
		for (int i = 1; i <= 9; i ++)
		{
			suma += prawdopodobienstwo[i];
			
		}
		for (int i = 1; i <= 9; i ++)
		{
			wynik[i] = (double)prawdopodobienstwo[i]/(double)suma;
		}
		return wynik;
	}

	private double wyostrz(double[] b)
	{
		double suma = 0.0;
		for (int i = 1; i <= 9; i++)
		{
			double obecnaWartosc = b[i];
			double wskazanaWartosc = prawdopodobienstwo(i);
			double roznica = obecnaWartosc - wskazanaWartosc;
			roznica = Math.abs(roznica);
			suma += roznica;
		}
		return suma/9;
	}

	public static double prawdopodobienstwo(int cyfra)
	{
		double dcyfra = (double) cyfra;
		double ulamek = 1/dcyfra;
		double suma = 1+ulamek;
		double log10 = Math.log(10);
		double logSuma = Math.log(suma);
		double result = logSuma/log10;
		return result;
	}

	public long zapisz(String nazwaKatalogu) throws IOException
	{
		long zapis1 = this.zapiszLogi(nazwaKatalogu+"\\logi.log");
		long zapis2 = this.zapiszPodsumowania(nazwaKatalogu+"\\375250.csv");
		return zapis1+zapis2;
	}

	public long zapiszLogi(String plik) throws IOException
	{
		File f = new File(plik);
		FileWriter fw = new FileWriter(f);
		for (String log : this.logi)
		{
			fw.write(log+"\n");
		}
		fw.close();
		return f.length();
	}

	public long zapiszPodsumowania(String plik) throws IOException
	{
		File f = this.wymazPlik(plik);
		FileWriter fw = new FileWriter(f, true);
		TreeMap<File, Double> tm = this.sortuj();
		fw.write("nazwa;wspó³czynnik;l.p.\n");
		for (Map.Entry<File, Double> wpis: this.podsumowania.entrySet())
		{
			File klucz = wpis.getKey();
			double wartosc = wpis.getValue();
			String nazwaPliku = klucz.getName();
			long numer = myutils.Tablice.pozycjaKluczaWMapie(tm, klucz);
			String s = wartosc+";"+numer;
			String log = nazwaPliku.substring(0, nazwaPliku.lastIndexOf("."))+";"+s+"\n";
			fw.write(log);
		}
		fw.close();
		return f.length();
	}
	
	private File wymazPlik(String plik) throws IOException
	{
		File f = new File(plik);
		f.delete();
		f.createNewFile();
		return f;
	}
	
	static class ValueComparator implements Comparator<File>
	{
		Map<File, Double> map;
		public ValueComparator(Map<File, Double> podsumowania)
		{
			this.map = podsumowania;
		}
		@Override
		public int compare(File o1, File o2)
		{
			Double d1 = this.map.get(o1);
			Double d2 = this.map.get(o2);
			return d2.compareTo(d1);
	
		}
			
	}

	private TreeMap<File, Double> sortuj()
	{
		Comparator<File> vc = new ValueComparator(this.podsumowania);
		TreeMap<File, Double> tm = new TreeMap<File, Double>(vc);
		tm.putAll(this.podsumowania);
		return tm;
	}
	

	public Prawo(String sciezka) throws FileNotFoundException
	{
		this.sciezka(sciezka);
		this.wczytajPlikiZeSciezki();
	}

	private void wczytajPlikiZeSciezki() throws FileNotFoundException
	{
		File sciezka = this.wyjatek();
		this.pliki = sciezka.listFiles();
	}
	
	public Vector<String> logi()
	{
		return this.logi;
	}
	
	public Map<File, Double> podsumowania()
	{
		return this.podsumowania;
	}
	
	public boolean IgnorujBledy()
	{
		return ignorujBledy;
	}

	public void IgnorujBledy(boolean ignorujBledy)
	{
		this.ignorujBledy = ignorujBledy;
	}
}
