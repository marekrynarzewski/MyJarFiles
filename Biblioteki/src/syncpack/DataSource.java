package syncpack;

import java.util.Collection;
import java.util.Map;

public abstract class DataSource
{
	private static long nextId = 1;
	public final long id = getId();
	/**
	 * usuwa rekordy z tabeli spe�niaj�ce warunek
	 * @param tableName nazwa tabeli
	 * @param selection warunek 
	 * @return liczba usuni�tych rekord�w
	 */
	abstract long delete(String tableName, String selection);
	private long getId()
	{
		long id = ++nextId;
		return id;
	}
	/**
	 * usuwa rekordy z tabeli spe�niaj�ce warunek
	 * @param tableName nazwa tabeli
	 * @param selection warunek  
	 * @param params parametry je�li selection<br/>
	 *  zawiera ? to tam zostan� wstawione <br/>
	 *  kolejne elementy tablicy
	 * @return liczba usuni�tych rekord�w
	 */
	abstract long delete(String tableName, String selection, String[] params);
	/**
	 * aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa warto�� rekordu
	 * @param selection warunek kt�ry musz� spe�nia�<br/>
	 * rekordy, by zostac zmodyfikowane
	 * @param params parametry je�li selection<br/>
	 *  zawiera ? to tam zostan� wstawione <br/>
	 *  kolejne elementy tablicy
	 * @return liczba zaktualizowanych element�w
	 */
	abstract long update(String tableName, Map<String, Object> value, String selection, String[] params);
	/**
	* aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa warto�� rekordu
	 * @param selection warunek kt�ry musz� spe�nia�<br/>
	 * rekordy, by zostac zmodyfikowane
	 * @return liczba zaktualizowanych element�w
	 */
	abstract long update(String tableName, Map<String, Object> value, String selection);
	/**
	 * aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa warto�� rekordu
	 * @return liczba zaktualizowanych element�w
	 */
	abstract long update(String tableName, Map<String, Object> value);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value pojedyncza warto�� rekordu
	 * @return id nowa wstawionego rekordu 
	 */
	abstract Object insert(String tableName, Map<String, Object> value);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value kolekcja rekord�w do wstawienia
	 * @return id ostatniego wstawionego rekordu
	 */
	abstract Object insert(String tableName, Collection<Map<String, Object>> values);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value kolekcja rekord�w do wstawienia
	 * @return tablica kluczy wstawionych rekordu
	 */
	abstract Object[] insertAll(String tableName, Collection<Map<String, Object>> values);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe�niaj�cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @param groupBy grupowanie rekord�w
	 * @param having warunek dla zgrupowanych rekord�w
	 * @param orderBy porz�dek rekord�w
	 * @return
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having, String orderBy);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe�niaj�cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @param groupBy grupowanie rekord�w
	 * @param having warunek dla zgrupowanych rekord�w
	 * @return
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe�niaj�cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @return 
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe�niaj�cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @return 
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe�niaj�cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @return 
	 */
	abstract Map<String, Object> queryTop(String tableName, String[] columns);
	abstract Collection<Map<String, Object>> queryAll(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having, String orderBy);
	abstract Collection<Map<String, Object>> queryAll(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having);
	abstract Collection<Map<String, Object>> queryAll(String tableName, String[] columns, String selection, 
			String[] params);
	abstract Collection<Map<String, Object>> queryAll(String tableName, String[] columns, String selection);
	abstract Collection<Map<String, Object>> queryAll(String tableName, String[] columns);
	abstract Collection<Map<String, Object>> queryAll(String tableName);
	abstract String[] columns(String tableName);
	abstract String[] tables();
}
