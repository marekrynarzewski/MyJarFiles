package text;

import java.util.Arrays;
import java.util.Map.Entry;

import pomoc.Para;

public class StringNumber
{
	public StringNumber(String input)
	{
		this(input.toCharArray());
	}
	
	public StringNumber(char[] chars)
	{
		this.chars = chars;
	}
	
	private char[] chars;
	
	public void add(int count)
	{
		int li = this.lastIndex();
		if (li == -1)
		{
			chars = new char[]{'0'};
		}
		this.add(li, count);
	}
	
	private int lastIndex()
	{
		return lastIndex(chars);
	}
	
	public static int lastIndex(char[] chars)
	{
		int len = chars.length;
		len--;
		return len;
	}
	
	public static int lastIndex(String s)
	{
		char[] chars = s.toCharArray();
		return lastIndex(chars);
	}
	private void add(int index, int count)
	{
		if (count > 0 && index >= 0)
		{
			char c = chars[index];
			Entry<Character, Integer> e = this.updateEntry(c, count);
			chars[index] = e.getKey();
			add(index-1, e.getValue());
		}
	}
	
	private Entry<Character, Integer> updateEntry(char c, int count)
	{
		int pos = Arrays.binarySearch(numbers, c);
		int sum = count+pos;
		int rest = sum % 10;
		char output = numbers[rest];
		Entry<Character, Integer> e = new Para<Character, Integer>(output, sum/10);
		return e;
	}
	
	public static final char[] numbers = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public static boolean isMultiplyOf10(char[] chars)
	{
		return isMultiply(chars, 10);
	}
	
	public static boolean isMultiply(char[] chars, int number)
	{
		int sum = 0;
		for (char c : chars)
		{
			sum += Character.getNumericValue(c);
		}
		return (sum % number == 0);
	}
	
	public String toString()
	{
		String s = "";
		for (char c : chars)
		{
			s += Character.getNumericValue(c);
		}
		return s;
	}
	
	public String value()
	{
		return toString();
	}
	
	public void inc()
	{
		add(1);
	}
	
	public void setChars(char[] c)
	{
		this.chars = c;
	}
	
	public char[] getChars()
	{
		return chars;
	}
}
