package zrodla_danych.mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import model.eng.Database;
import model.eng.DatabaseException;
import zrodla_danych.BazaDanych;
import zrodla_danych.BazaDanych.WyjatekBazyDanych;

public class MySQL implements BazaDanych, Database
{
	private Connection polaczenie;
	
	private String host;
	private int port;
	private String user;
	private String pass;
	private String name;
	
	public MySQL(String host, int port, String user, String pass, String name)
    {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;
    }
        
    public MySQL(String host, String user, String pass, String name)
    {
        this(host, 3306, user, pass, name);
    }
    
    public MySQL(String user, String pass, String name)
    {
        this("localhost", user, pass, name);
    }
     
    @Override
	public boolean testujPolaczenie()
	{
        try
        {
            this.polaczenie = DriverManager.getConnection(this.url(), user, pass);
            return true;
        }
        catch (SQLException e)
        {
        	e.printStackTrace();
        }
        return false;
	}
	
	public String url()
	{
		return "jdbc:mysql://"+host+":"+port+"/"+name;
	}
	
	public ResultSet uzyskajZbiorWynikow(String zapytanie) throws SQLException
	{
		Statement zadanie = this.polaczenie.createStatement();
		return zadanie.executeQuery(zapytanie);
	}
	
    @Override
	public Map<String, String> uzyskajMape(String zapytanie) throws WyjatekBazyDanych
	{
		ResultSet zbiorWynikow;
		try
		{
			zbiorWynikow = this.uzyskajZbiorWynikow(zapytanie);
			List<Map<String, String>> wektor = zbiorWynikowDoWektora(zbiorWynikow);
			return wektor.get(0);
		}
		catch (SQLException e)
		{
			throw new WyjatekBazyDanych(e);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new WyjatekBazyDanych("Nie wystarczajaca liczba wierszy");
		}
	}
	
	private List<Map<String, String>> uzyskajWektor(String zapytanie) throws SQLException
	{
		ResultSet zbiorWynikow = this.uzyskajZbiorWynikow(zapytanie);
		return zbiorWynikowDoWektora(zbiorWynikow);
	}
	
	@Override
	public List<Map<String, String>> uzyskajListe(String zapytanie) throws WyjatekBazyDanych
	{
		try
		{
			ResultSet rs = uzyskajZbiorWynikow(zapytanie);
			List<Map<String, String>> result = this.zbiorWynikowDoWektora(rs);
			return result;
		}
		catch (SQLException e)
		{
			throw new WyjatekBazyDanych(e);
		}
	}
	
	@Override
	public String wstaw(String tabela, Map<String, String> nazwyKolumnIWartosciPol) throws WyjatekBazyDanych
	{
		this.testujPolaczenie();
		String zapytanie = Zapytania.wstawianie(tabela, nazwyKolumnIWartosciPol);
		try
		{
			Statement stmt = this.polaczenie.createStatement();
			stmt.executeUpdate(zapytanie);
			zapytanie = "SELECT LAST_INSERT_ID() AS last_insert_id";
			ResultSet rs = uzyskajZbiorWynikow(zapytanie);
			List<Map<String, String>> s = this.zbiorWynikowDoWektora(rs);
			Map<String, String> x = s.get(0);
			return x.get("last_insert_id");
		}
		catch (SQLException e)
		{
			throw new WyjatekBazyDanych(e);
		}
	}
	
	
	@Override
	public String wstaw(String tabela, List<Map<String, String>> wartosci) throws WyjatekBazyDanych
	{
		String result = null;
		for (Map<String, String> mapa : wartosci)
		{
			result = wstaw(tabela, mapa);
		}
		return result;
	}
	
	@Override
	public long zmien(String tabela, Map<String, String> wartosci, String warunek) throws WyjatekBazyDanych
	{
		String zapytanie = Zapytania.aktualizacja(tabela, wartosci, warunek);
		try
		{
			Statement stmt = this.polaczenie.createStatement();
			return stmt.executeUpdate(zapytanie);
		}
		catch (SQLException e)
		{
			throw new WyjatekBazyDanych(e);
		}
	}
	@Override
	public long usun(String tabela, String warunek) throws WyjatekBazyDanych
	{
		String zapytanie = Zapytania.usuwanie(tabela, warunek);
		try
		{
			Statement stmt = this.polaczenie.createStatement();
			return stmt.executeUpdate(zapytanie);
		}
		catch (SQLException e)
		{
			throw new WyjatekBazyDanych(e);
		}
	}

    private List<Map<String, String>> zbiorWynikowDoWektora(ResultSet rs) throws SQLException
	{
		List<Map<String, String>> wynik = new ArrayList<>();
		Collection<String> nazwyKolumn = nazwyKolumn(rs);
		while (rs.next())
		{
			Map<String, String> mapa = new TreeMap<>();
			for (String name : nazwyKolumn)
			{
				String value = rs.getString(name);
				mapa.put(name, value);
			}
			wynik.add(mapa);
		}
		return wynik;
	}
        
	private static Collection<String> nazwyKolumn(ResultSet rs) throws SQLException
	{
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Collection<String> wynik = new ArrayList<>();
		for (int i = 1; i < columnCount + 1; i++ ) 
		{
			String name = rsmd.getColumnName(i);
			wynik.add(name);
		}
		return wynik;
	}

	@Override
	public boolean testConnection()
	{
		boolean result;
		
		result = this.testujPolaczenie();
		
		return result;
	}

	@Override
	public List<Map<String, String>> getList(String query)
			throws DatabaseException
	{
		List<Map<String, String>> result;
		
		try
		{
			result = this.uzyskajListe(query);
		}
		catch (WyjatekBazyDanych e)
		{
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public Map<String, String> getMap(String query) throws DatabaseException
	{
		Map<String, String> result;
		
		try
		{
			result = this.uzyskajMape(query);
		}
		catch (WyjatekBazyDanych e)
		{
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public String insert(String table, Map<String, String> values)
			throws DatabaseException
	{
		String result;
		
		try
		{
			result = this.wstaw(table, values);
		}
		catch (WyjatekBazyDanych e)
		{
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public String insert(String table, List<Map<String, String>> values)
			throws DatabaseException
	{
		String result = null;
		
		for (Map<String, String> map : values)
		{
			result = this.insert(table, map);
		}
		return result;
	}

	@Override
	public long update(String table, Map<String, String> values,
			String condition) throws DatabaseException
	{
		long result;
		
		try
		{
			result = this.zmien(table, values, condition);
		}
		catch (WyjatekBazyDanych e)
		{
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public long delete(String table, String condition) throws DatabaseException
	{
		long result;
		
		try
		{
			result = this.usun(table, condition);
		}
		catch (WyjatekBazyDanych e)
		{
			throw new DatabaseException(e);
		}
		
		return result;
	}

	@Override
	public String getLastInsertId() throws DatabaseException
	{
		String zapytanie;
		ResultSet rs;
		List<Map<String, String>> s;
		Map<String, String> x;
		zapytanie = "SELECT LAST_INSERT_ID() AS last_insert_id";
		try
		{
			rs = uzyskajZbiorWynikow(zapytanie);
			s = this.zbiorWynikowDoWektora(rs);
			x = s.get(0);
			return x.get("last_insert_id");
		}
		catch (SQLException e)
		{
			throw new DatabaseException(e);
		}
		
	}
	
	

	
	
	
}
