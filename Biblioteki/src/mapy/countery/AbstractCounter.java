package mapy.countery;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public interface AbstractCounter<K, V>
{
	Iterator<K> elements();
	List<Entry<K, V>> mostCommon();
	List<Entry<K, V>> mostCommon(int n);
	void subtract(Collection<K> collection);
	void subtract(Map<K, V> map);
	void subtract(AbstractCounter<K, V> counter);
	void update(Map<K, V> map);
	void update(Collection<K> collection);
	void update(AbstractCounter<K, V> counter);
	V getCount(K key);
	int getSize();
}
