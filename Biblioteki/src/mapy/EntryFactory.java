package mapy;

import java.util.Map.Entry;

/**
 * interface to produce object which <br />
 * implements interface Entry&lt;K, V&gt;  <br />
 * for CustomEntryMap&lt;K, V&gt;
 * @author Marek
 *
 * @param <K> key of Entry&lt;K, V&gt;
 * @param <V> value of Entry&lt;K, V&gt;
 */
public interface EntryFactory<K, V>
{
	/**
	 * produce a instantiation of Entry&lt;K, V&gt;
	 * @param key a key of Entry&lt;K, V&gt;
	 * @param value a value of Entry&lt;K, V&gt;
	 * @return a instantiation of Entry&lt;K, V&gt;
	 */
	Entry<K, V> produce(K key, V value);
}
