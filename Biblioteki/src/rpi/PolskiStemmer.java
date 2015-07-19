package rpi;

import java.util.List;

import morfologik.stemming.PolishStemmer;
import morfologik.stemming.WordData;

public class PolskiStemmer
{
	private PolishStemmer ps = new PolishStemmer();
	
	public void imie(String imie)
	{
		List<WordData> wynik = ps.lookup(imie);
		for (WordData wd : wynik)
		{
			System.out.println(imie+" "+wd.getStem());
		}
	}
}
