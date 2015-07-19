package permutacje;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Permutacje
{
	public static class Permutacja<K>
	{
		private final K[] zrodlo;

		public Permutacja(K[] tablica)
		{
			this.zrodlo = tablica;
		}
		
		@SuppressWarnings ("unchecked")
		public Permutacja<K> losowaPermutacja()
		{
			K[] knoweZrodlo = Arrays.copyOf(zrodlo, count());
			List<K> list = Arrays.asList(knoweZrodlo);
			Collections.shuffle(list);
			knoweZrodlo = (K[]) list.toArray();
			Permutacja<K> wynik = new Permutacja<K>(knoweZrodlo);
			return wynik;
		}
		
		public int count()
		{
			return zrodlo.length;
		}
	}
}
