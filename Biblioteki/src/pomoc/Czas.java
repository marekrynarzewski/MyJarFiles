package pomoc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import json.JSONObject;
import pomoc.Serializacja.JSONable;

public class Czas implements JSONable
{
	/**
	 * Reprezentuje element czasu
	 * @author Marek
	 *
	 */
	public enum Element
	{
		Rok(Calendar.YEAR),
		Kwartal,
		Miesiac(Calendar.MONTH),
		Tydzien,
		Dzien(Calendar.DAY_OF_MONTH),
		Godzina(Calendar.HOUR),
		Minuta(Calendar.MINUTE),
		Sekunda(Calendar.SECOND);
		
		/**
		 * przechowuje informacje o formacie
		 */
		public final int pole;
		
		/**
		 * konstruktor Elementu z podanym formatem
		 * @param format zadany format elementu czasu
		 */
		Element(int format)
		{
			this.pole = format;
		}
		
		Element()
		{
			pole = 0;
		}
	}
	
	/**
	 * globalny format czasu
	 */
	public static String formatGlobalny = "yyyy-MM-dd HH:mm:ss";
	private Calendar cal = Calendar.getInstance();
	public static Czas teraz = new Czas();

	public static void ustawTeraz(String czas)
	{
		teraz = new Czas(czas);
	}
	private String fdata;
	/**
	 * konstruktor obiektu czas. <br />
	 *  W momencie utworzenie zapisuje aktualny czas
	 */
	public Czas()
	{
		fdata();
	}
	
	private void fdata()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(Czas.formatGlobalny);
		Date time = cal.getTime();
		fdata = sdf.format(time);
	}
	
	/**
	 * odwtarza obiekt czas z podanym czasem
	 * @param czas podany czas
	 */
	public Czas(String czas)
	{
		fdata = czas;
	}
	/**
	 * przesuwa globalny zegar na nast�pny element
	 * @param element element czasu
	 * @param liczba liczba przesuni�cia
	 */
	public Czas nastepny(Czas.Element element, int liczba)
	{
		Czas wynik = new Czas();
		int field = element.pole;
		switch(element)
		{
			case Dzien:
			case Godzina:
			case Miesiac:
			case Minuta:
			case Rok:
			case Sekunda:
				wynik.cal.add(field, liczba);
				break;
			case Kwartal:
				wynik = wynik.nastepny(Czas.Element.Miesiac, 3*liczba);
				break;
			case Tydzien:
				wynik = wynik.nastepny(Czas.Element.Dzien, 7*liczba);
				break;		
		}
		wynik.fdata();
		return wynik;
	}
	
	/**
	 * alias do nastepny(Element, int)
	 * @param element element czasu
	 */
	public Czas nastepny(Czas.Element element)
	{
		return nastepny(element, 1);
	}

	/**
	 * reprezentacja czasu
	 */
	public String toString()
	{
		return fdata;
	}

	/**
	 * reprezentacja JSONowa
	 */
	public JSONObject toJSON()
	{
		JSONObject wynik = new JSONObject();
		wynik.put("fdata", this.fdata);
		return wynik;
	}
	
	public enum DzienTygodnia
	{
		Niedziela(0),
		Poniedzialek(1),
		Wtorek(2),
		Sroda(3),
		Czwartek(4),
		Piatek(5),
		Sobota(6);
		public final int wartosc;
		DzienTygodnia(int wartosc)
		{
			this.wartosc = wartosc;
		}
		
		public static DzienTygodnia jakoDzienTygodnia(int liczba)
		{
			DzienTygodnia[] dniTygodnia = values();
			if (liczba >= 0 && liczba < dniTygodnia.length)
			{
				return dniTygodnia[liczba];
			}
			return null;
		}
		
	}

	public boolean przed(Czas c)
	{
		Entry<Date, Date> czasy = uzyskajCzasy(c);
		Date d1 = czasy.getKey();
		Date d2 = czasy.getValue();
		return d1.before(d2);
		
	}
	
	public boolean po(Czas c)
	{
		Entry<Date, Date> czasy = uzyskajCzasy(c);
		Date d1 = czasy.getKey();
		Date d2 = czasy.getValue();
		return d1.after(d2);
	}
	
	private Entry<Date, Date> uzyskajCzasy(Czas c)
	{
		DateFormat formater = new SimpleDateFormat(Czas.formatGlobalny);
		Entry<Date, Date> czasy;
		String toString;
		try 
		{
			toString = c.toString();
			Date d1 = formater.parse(toString);
			czasy = new Para<Date, Date>(d1);
			Czas c2 = new Czas();
			toString = c2.toString();
			Date d2 = formater.parse(toString);
			czasy.setValue(d2);
		}
		catch(ParseException pe)
		{
			czasy = new Para<Date, Date>(null);
		}
		return czasy;
	}
	
	public static void nastepnyDzien()
	{
		Czas.teraz = Czas.teraz.nastepny(Czas.Element.Dzien);
	}
	
	public static void nastepnyRok()
	{
		Czas.teraz = Czas.teraz.nastepny(Czas.Element.Rok);
	}

	@Override
	public List<String> polaJSONowe()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
