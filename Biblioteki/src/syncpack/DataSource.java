package syncpack;

import java.util.Collection;
import java.util.Map;

public abstract class DataSource
{
	private static long nextId = 1;
	public final long id = getId();
	/**
	 * usuwa rekordy z tabeli spe³niaj¹ce warunek
	 * @param tableName nazwa tabeli
	 * @param selection warunek 
	 * @return liczba usuniêtych rekordów
	 */
	abstract long delete(String tableName, String selection);
	private long getId()
	{
		long id = ++nextId;
		return id;
	}
	/**
	 * usuwa rekordy z tabeli spe³niaj¹ce warunek
	 * @param tableName nazwa tabeli
	 * @param selection warunek  
	 * @param params parametry jeœli selection<br/>
	 *  zawiera ? to tam zostan¹ wstawione <br/>
	 *  kolejne elementy tablicy
	 * @return liczba usuniêtych rekordów
	 */
	abstract long delete(String tableName, String selection, String[] params);
	/**
	 * aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa wartoœæ rekordu
	 * @param selection warunek który musz¹ spe³niaæ<br/>
	 * rekordy, by zostac zmodyfikowane
	 * @param params parametry jeœli selection<br/>
	 *  zawiera ? to tam zostan¹ wstawione <br/>
	 *  kolejne elementy tablicy
	 * @return liczba zaktualizowanych elementów
	 */
	abstract long update(String tableName, Map<String, Object> value, String selection, String[] params);
	/**
	* aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa wartoœæ rekordu
	 * @param selection warunek który musz¹ spe³niaæ<br/>
	 * rekordy, by zostac zmodyfikowane
	 * @return liczba zaktualizowanych elementów
	 */
	abstract long update(String tableName, Map<String, Object> value, String selection);
	/**
	 * aktualizuje rekordy
	 * @param tableName nazwa tabeli
	 * @param value nowa wartoœæ rekordu
	 * @return liczba zaktualizowanych elementów
	 */
	abstract long update(String tableName, Map<String, Object> value);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value pojedyncza wartoœæ rekordu
	 * @return id nowa wstawionego rekordu 
	 */
	abstract Object insert(String tableName, Map<String, Object> value);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value kolekcja rekordów do wstawienia
	 * @return id ostatniego wstawionego rekordu
	 */
	abstract Object insert(String tableName, Collection<Map<String, Object>> values);
	/**
	 * wstawia elementy do tabeli
	 * @param tableName nazwa tabeli
	 * @param value kolekcja rekordów do wstawienia
	 * @return tablica kluczy wstawionych rekordu
	 */
	abstract Object[] insertAll(String tableName, Collection<Map<String, Object>> values);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe³niaj¹cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @param groupBy grupowanie rekordów
	 * @param having warunek dla zgrupowanych rekordów
	 * @param orderBy porz¹dek rekordów
	 * @return
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having, String orderBy);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe³niaj¹cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @param groupBy grupowanie rekordów
	 * @param having warunek dla zgrupowanych rekordów
	 * @return
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params, String groupBy, String having);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe³niaj¹cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @param params parametru warunku
	 * @return 
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection, 
			String[] params);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe³niaj¹cy selection z params po grupowaniu i 
	 * @param tableName nazwa tabeli
	 * @param columns tablicy nazw kolumn
	 * @param selection warunek
	 * @return 
	 */
	abstract Map<String, Object> queryOne(String tableName, String[] columns, String selection);
	/**
	 * uzyskuje pierwszy posortowany rekord z tabeli spe³niaj¹cy selection z params po grupowaniu i 
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
