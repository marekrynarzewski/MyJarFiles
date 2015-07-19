package arrays;

import java.lang.reflect.Array;
import java.util.Arrays;

//import org.ini4j.jdk14.edu.emory.mathcs.backport.java.util.Arrays;

/**
 * Klasa sluzaca do dynamicznego rozszerzania standardowych tablic w Javie
 * @author Marek
 *
 * @param <T> typ tablicy
 */
public class DynamicArray<T>
{
	private T[] array;
	private Class<T> cls;
	/**
	 * konstruuje pusta tablice obiektów danej klasy
	 * @param cls klasa
	 */
	public DynamicArray(Class<T> cls)
	{
		this.cls = cls;
		this.array = (T[]) Array.newInstance(this.cls, 0);
	}
	
	/**
	 * odtwarza tablice z podanej tablicy
	 * @param array dana tablica
	 */
	public DynamicArray(T[] array)
	{
		this.cls = (Class<T>) array.getClass();
		this.array = array;
	}
	
	/**
	 * dodaje element item na koniec tablicy
	 * @param item element dodawany
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
	 * dodaje element item na poczatek tablicy
	 * @param item element dodawany
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
	 * usuwa element z poczatku tablicy
	 * @return usuniety element
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
	 * usuwa element z konca tablicy
	 * @return usuniety element
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

	/*public T[] popFront(int count)
	{
		T[] newArray;
		T result;
		
		newArray = (T[]) Array.newInstance(this.cls, this.array.length-count);
		for (int i = 0, j = 1; i < this.array.length; i++, j++)
		{
			newArray[i] = this.array[j];
		}
		result = this.array[0];
		this.array = newArray;
		
		T[] result;
		return result;
	}*/
	
	/*public T[] popBack(int count)
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
	}*/
	
	/**
	 * dodaje elementy na koniec tablicy
	 * @param items dodawane elementy
	 */
	public void pushBack(T[] items)
	{
		for (T item : items)
		{
			this.pushBack(item);
		}
	}
	
	/**
	 * dodaje elementy na poczatek tablicy
	 * @param items dodawane elementy
	 */
	public void pushFront(T[] items)
	{
		for (T item : items)
		{
			this.pushFront(item);
		}
	}
	
	/**
	 * uzyskuje tablice
	 * @return tablica
	 */
	public T[] getArray()
	{
		return this.array;
	}
	
	/**
	 * wyswietla reprezentacje tablicy
	 */
	public String toString()
	{
		String result;
		
		result = Arrays.toString(this.array);
		
		return result;
	}
}
