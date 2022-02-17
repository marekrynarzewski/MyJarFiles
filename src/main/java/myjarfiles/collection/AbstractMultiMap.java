package myjarfiles.collection;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AbstractMultiMap<T1, T2> extends Map<T1, T2>
{
	Set<Entry<T1, T2>> portionEntrySet(T1 fromKey, T1 toKey);
	List<T2> getElements(T1 key);
	List<T2> removeElements(T1 key);
	boolean contains(T1 key, T2 value);
}
