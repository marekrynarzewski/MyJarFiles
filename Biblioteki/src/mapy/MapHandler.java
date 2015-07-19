package mapy;

import java.util.Map;

public interface MapHandler<K, V>
{
	void onChangeValue(Map<K, V> thisMap, K key, V value);
}
