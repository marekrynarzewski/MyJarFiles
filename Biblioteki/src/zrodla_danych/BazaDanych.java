package zrodla_danych;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Marek
 */
public interface BazaDanych
{
    boolean testujPolaczenie();
	List<Map<String, String>> uzyskajListe(String zapytanie) throws WyjatekBazyDanych;
	Map<String, String> uzyskajMape(String zapytanie)  throws WyjatekBazyDanych;
	String wstaw(String tabela, Map<String, String> wartosci) throws WyjatekBazyDanych;
	String wstaw(String tabela, List<Map<String, String>> wartosci) throws WyjatekBazyDanych;
	long zmien(String tabela, Map<String, String> wartosci, String warunek) throws WyjatekBazyDanych;
	long usun(String tabela, String warunek) throws WyjatekBazyDanych;
	
	public static class WyjatekBazyDanych extends Exception
	{
		public WyjatekBazyDanych()
		{
			
		}
		
		public WyjatekBazyDanych(String zapytanie)
		{
			super(zapytanie);
		}
		
		public WyjatekBazyDanych(Exception wyjatek)
		{
			super(wyjatek);
		}
		
		public WyjatekBazyDanych(Throwable wyjatek)
		{
			super(wyjatek);
		}
	}
}
