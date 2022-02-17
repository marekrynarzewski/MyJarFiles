package myjarfiles.datetime;

import java.util.Calendar;
import java.util.Date;

public class DateAdder 
{
	public static Date add(Date date, int field, int number)
	{
		Date wynik;
		Calendar kalendarz;
		
		kalendarz = Calendar.getInstance(); 
		kalendarz.setTime(date); 
		kalendarz.add(field, number);
		wynik = kalendarz.getTime();
		
		return wynik;
	}
	
	public static Date add(Date data, int liczba)
	{
		Date wynik;
		
		wynik = add(data, Calendar.DAY_OF_MONTH, liczba);
		
		return wynik;
	}
	
	public static Date add(Date data)
	{
		Date wynik;
		
		wynik = add(data, 1);
		
		return wynik;
	}
}
