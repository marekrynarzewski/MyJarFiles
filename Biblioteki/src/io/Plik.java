package io;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Plik
{
	public static String zaladujPlik(String nazwa) throws FileNotFoundException, IOException
	{
		String wynik = "";
		FileReader fr;
		fr = new FileReader(nazwa);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null)
		{
			wynik = wynik+s+"\n";
		}
		br.close();
		fr.close();
		return wynik;
	}
	
	public static Vector<String> plikDoTablicy(String nazwa) throws IOException
	{
		Vector<String> result = new Vector<String>();
		FileReader fr;
		fr = new FileReader(nazwa);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null)
		{
			result.add(s);
		}
		br.close();
		fr.close();
		return result;
	}
	
	public static String pobierzZUrla(String path) throws IOException
	{
		URL url = new URL(path);
		URLConnection connection = url.openConnection();
		connection.connect();
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new  InputStreamReader(is);
		BufferedReader in = new BufferedReader(isr);
		String result = new String("");
		String inputLine;
		while ((inputLine = in.readLine()) != null)
		{
			result = result.concat(inputLine);
		}
		in.close();
		isr.close();
		is.close();
		return result;
	}

	public static boolean istnieje(String string) {
		File plik = new File(string);
		return plik.exists();
	}

	public static long zapisz(String nazwa, Vector<String> plik) throws IOException
	{
		FileWriter fw;
		fw = new FileWriter(nazwa);
		BufferedWriter bw = new BufferedWriter(fw);
		for (String str: plik)
		{
			bw.write(str);
			bw.newLine();
		}
		bw.close();
		fw.close();
		return new File(nazwa).length();
	}
	
	
}
