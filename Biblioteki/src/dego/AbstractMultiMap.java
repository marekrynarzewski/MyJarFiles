package dego;

import java.util.Map;
import java.util.Set;
import java.util.List;


public interface AbstractMultiMap<T1, T2> extends Map<T1, T2>
{
	Set<Entry<T1, T2>> portionEntrySet(T1 odKlucza, T1 doKlucza);
	List<T2> getElements(T1 key);
	List<T2> removeElements(T1 key);
	boolean contains(T1 t1, T2 t2);
}
