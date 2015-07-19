package pomoc;

import java.util.List;
import java.util.Random;
import java.util.Vector;

public class Los
{
	public static int rand(int low, int high)
	{
		Random r = new Random();
		int R = r.nextInt(high-low) + low;
		return R;
	}
	
	public static <K> Vector<Vector<K>> uzyskajPermutacje(List<K> kolekcja, int liczbaElementow)
	{
		Vector<Vector<K>> wynik = new Vector<Vector<K>>();
		int[] iteratory = inicjujIteratory(liczbaElementow);
		while (iteratory[0] < kolekcja.size()-liczbaElementow)
		{
			Vector<K> tablica = new Vector<K>();
			for (int iterator : iteratory)
			{
				tablica.add(kolekcja.get(iterator));
			}
			wynik.add(tablica);
			iteratory = noweIteratory(iteratory, liczbaElementow, kolekcja.size(), 1);
		}
		return wynik;
	}
	
	private static int[] noweIteratory(int[] stareIteratory, int liczbaElementow, int rozmiarKolekcji, int liczba)
	{
		//System.out.println("noweIteratory("+Arrays.toString(stareIteratory)+", "+liczbaElementow+", "+rozmiarKolekcji+", "+liczba+")");
		int roznica = liczbaElementow-liczba;
		//System.out.println("roznica = "+roznica);
		if (roznica > -1)
		{
			if (stareIteratory[roznica] < rozmiarKolekcji-liczba)
			{
				//System.out.println(stareIteratory[roznica]+" < "+(rozmiarKolekcji-liczba));
				stareIteratory[roznica]++;
				//System.out.println("stareIteratory[roznica] = "+stareIteratory[roznica]);
				for (int i = roznica+1; i < stareIteratory.length; i++)
				{
					//System.out.println("i = "+i);
					int poprzednik = stareIteratory[i-1];
					//System.out.println("poprzednik = "+poprzednik);
					stareIteratory[i] = poprzednik+1;
					//System.out.println("stareIteratory[i] = "+stareIteratory[i]);
				}
			}
			else
			{
				stareIteratory = noweIteratory(stareIteratory, liczbaElementow, rozmiarKolekcji, liczba+1);
			}
		}
		return stareIteratory;
	}
	
	private static int[] inicjujIteratory(int liczbaElementow)
	{
		int[] iteratory = new int[liczbaElementow];
		for (int i = 0; i < liczbaElementow; i++)
		{
			iteratory[i] = i;
		}
		return iteratory;
	}
	
	public static <K> List<Vector<K>> uzyskajPermutacje(List<K> kolekcja, int liczbaElementow, int limit)
	{
		List<Vector<K>> wynik = uzyskajPermutacje(kolekcja, liczbaElementow);
		wynik = wynik.subList(0, limit);
		return wynik;
	}
	
	public static boolean sprawdzWartosciTablicy(boolean[] warunki, boolean b)
	{
		for (boolean warunek : warunki)
		{
			if (warunek != b)
				return false;
		}
		return true;
	}	

}
