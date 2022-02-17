package myjarfiles.array;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Class to dynamic extending standard arrays
 * @author Marek
 *
 * @param <T> type array
 */

@SuppressWarnings("unchecked")
public class DynamicArray<T> 
{
	private T[] array;
	private Class<T> cls;
	
	/**
	 * constructor of DynamicArray with empty array of object given class
	 * @param cls class
	 */
	public DynamicArray(Class<T> cls)
	{
		this.cls = cls;
		this.array = (T[]) Array.newInstance(this.cls, 0);
	}
	
	/**
	 * recreated array from given array
	 * @param array given array
	 */
	public DynamicArray(T[] array)
	{
		this.cls = (Class<T>) array.getClass();
		this.array = array;
	}
	
	/**
	 * adds item at the end of array
	 * @param item added item
	 */
	public void pushBack(T item)
	{
		T[] newArray = (T[]) Array.newInstance(this.cls, this.array.length+1);
		for (int i = 0; i < this.array.length; i++)
		{
			newArray[i] = this.array[i];
		}
		newArray[this.array.length] = item;
		this.array = newArray;
	}
	
	/**
	 * add item at the front of array
	 * @param item added item
	 */
	public void pushFront(T item)
	{
		T[] newArray;
		
		newArray = (T[]) Array.newInstance(this.cls, this.array.length+1);
		newArray[0] = item;
		for (int i = 1; i <= this.array.length; i++)
		{
			newArray[i] = this.array[i];
		}
		this.array = newArray;
	}
	
	/**
	 * delete item from start of array
	 * @return T removed item
	 */
	public T popFront()
	{
		T[] newArray;
		T result;
		
		newArray = (T[]) Array.newInstance(this.cls, this.array.length-1);
		for (int i = 0, j = 1; i < this.array.length; i++, j++)
		{
			newArray[i] = this.array[j];
		}
		result = this.array[0];
		this.array = newArray;
		
		return result;
	}
	
	/**
	 * delete item from end of array
	 * @return removed item
	 */
	public T popBack()
	{
		T[] newArray;
		T result;
		
		newArray = (T[]) Array.newInstance(this.cls, this.array.length-1);
		for (int i = 0; i < this.array.length-1; i++)
		{
			newArray[i] = this.array[i];
		}
		result = this.array[this.array.length-1];
		this.array = newArray;
		
		return result;
	}
	
	/**
	 * add items on the end of array
	 * @param items added items
	 */
	public void pushBack(T[] items)
	{
		for (T item : items)
		{
			this.pushBack(item);
		}
	}
	
	/**
	 * add items on the start of array
	 * @param items added items
	 */
	public void pushFront(T[] items)
	{
		for (T item : items)
		{
			this.pushFront(item);
		}
	}
	
	/**
	 * get array
	 * @return array
	 */
	public T[] getArray()
	{
		return this.array;
	}
	
	/**
	 * show array representation
	 */
	public String toString()
	{
		return Arrays.toString(this.array);
	}
}
