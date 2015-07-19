package mapy;

import java.util.Comparator;
import java.util.Map;

public class MapComparator<K, V> implements Comparator<K>
{
	private final Map<K, V> mapa;
	private final Comparator<V> comparator;
	
	public MapComparator(Map<K, V> mapa, Comparator<V> comparator)
	{
		this.mapa = mapa;
		this.comparator = comparator;
	}
	
	public int compare(K a, K b) 
    {
		V valA, valB;
    	int result;
   
    	valA = this.mapa.get(a);
    	valB = this.mapa.get(b);
    	result = this.comparator.compare(valA, valB);
        
        return result;
    }
	

}
