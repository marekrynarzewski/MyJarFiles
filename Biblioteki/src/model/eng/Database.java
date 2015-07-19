package model.eng;

import java.util.List;
import java.util.Map;


public interface Database
{
	boolean testConnection();
	List<Map<String, String>> getList(String query) throws DatabaseException;
	Map<String, String> getMap(String query)  throws DatabaseException;
	String insert(String tabela, Map<String, String> values) throws DatabaseException;
	String insert(String table, List<Map<String, String>> values) throws DatabaseException;
	long update(String table, Map<String, String> values, String condition) throws DatabaseException;
	long delete(String table, String condition) throws DatabaseException;
	String getLastInsertId() throws DatabaseException;
}
