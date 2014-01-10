package cfg;

//import io.Plik;

import java.io.IOException;
import java.util.Vector;


public class Config
{
	public static String USER = "marekrynarzewski";
	public static String PASS = "m4r3czeqoZ0B";
	public static String XmlRpc = "http://www.blox.pl/xmlrpc";
	
	/*public void read(String nazwaPliku) throws IOException
	{
		Vector<String> zawartosc = Plik.plikDoTablicy(nazwaPliku);
		for (String linia : zawartosc)
		{
			String[] niw = linia.split("=");
			switch(niw[0])
			{
				case "user":
					USER = niw[1];
					break;
				case "pass":
					PASS = niw[1];
					break;
				case "xmlrpc":
					XmlRpc = niw[1];
					break;
			}
		}
	}*/
}
