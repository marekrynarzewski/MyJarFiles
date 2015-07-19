package data;

import java.util.Calendar;
import java.util.Date;

public class Wyliczenie
{
	public static Date dodaj(Date data, int pole, int liczba)
	{
		Date wynik;
		Calendar kalendarz;
		
		kalendarz = Calendar.getInstance(); 
		kalendarz.setTime(data); 
		kalendarz.add(pole, liczba);
		wynik = kalendarz.getTime();
		
		return wynik;
	}
	
	public static Date dodajDzien(Date data, int liczba)
	{
		Date wynik;
		
		wynik = dodaj(data, Calendar.DAY_OF_MONTH, liczba);
		
		return wynik;
	}
	
	public static Date dodajDzien(Date data)
	{
		Date wynik;
		
		wynik = dodajDzien(data, 1);
		
		return wynik;
	}
}
