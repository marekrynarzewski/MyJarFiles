package liczby;

import java.util.Date;

/**
 * interface to generate ranges
 * @author Marek
 *
 * @param <T> item of range
 */
public interface Generator<T>
{
	/**
	 * function which returns next element from range
	 * @param current the current element with relative to next element of the range
	 * @return next element from range
	 */
	T next(T current);
	
	/**
	 * checks if current item is in correct relationship to ending point
	 * @param current current item
	 * @param end the end point of the range
	 * @return
	 */
	boolean test(T current, T end);
}
