package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import json.JSONObject;

public class Klient 
{
	public final String url;
	public Metoda metoda = Metoda.GET;
	private HttpURLConnection polaczenie;
	private int	kodOdpowiedzi;
	public JSONObject wejscie;
	private String	parametry;
	
	public Klient(String url)
	{
		this.url = url;
	}
	
	public int kodOdpowiedzi()
	{
		return this.kodOdpowiedzi;
	}
	
	private URL budujURL() throws MalformedURLException
	{
		return new URL(this.url);
	}
	
	private void otworzPolaczenie(URL url) throws IOException
	{
		this.polaczenie = (HttpURLConnection) url.openConnection();
	}
	
	private void ustawMetode() throws ProtocolException
	{
		this.polaczenie.setRequestMethod(this.metoda.toString());
		this.polaczenie.setDoOutput(true);
		this.polaczenie.setRequestProperty("Content-Type", "application/json");
		this.polaczenie.setRequestProperty("Accept", "application/json");
		
	}
	
	private void sprawdzStanPolaczenia() throws IOException
	{
		this.kodOdpowiedzi = this.polaczenie.getResponseCode();
		if (this.kodOdpowiedzi != 200) 
		{
			throw new RuntimeException("Failed : HTTP error code : "+ this.kodOdpowiedzi);
		}
	}
	
	private String czytaj() throws IOException
	{
		InputStream wejscie = this.polaczenie.getInputStream();
		InputStreamReader czytnik = new InputStreamReader(wejscie);
		BufferedReader br = new BufferedReader(czytnik);
		String linijka, wynik;
		wynik = "";
		while ((linijka = br.readLine()) != null) 
		{
			wynik += linijka;
		}
		br.close();
		czytnik.close();
		wejscie.close();
		return wynik;
	}
	
	public String odbierz() throws IOException
	{
		URL url = this.budujURL();
		this.otworzPolaczenie(url);
		this.ustawMetode();
		this.pisz();
		this.sprawdzStanPolaczenia();
		return this.czytaj();
	}
	
	private void pisz() throws IOException
	{
		if (this.wejscie != null)
		{
			String input = this.wejscie.toString();
			 
			OutputStream os = this.polaczenie.getOutputStream();
			os.write(input.getBytes());
			os.flush();
		}
	}
	public static void main(String args[])
	{
		String url = "http://localhost:7747/BankInternetowy/htdocs/index.php/rest";
		Klient klient = new Klient(url);
		klient.metoda = Metoda.POST;
		klient.parametry = "";
		try
		{
			String wynik = klient.odbierz();
			System.out.println(wynik);
			System.out.println(klient.kodOdpowiedzi());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		//obj.
	}
	
	
}
