package logika;

public class Logika
{
	public static boolean not(boolean b)
	{
		return !b;
	}
	
	public static boolean and(boolean w1, boolean w2)
	{
		return w1 && w2;
	}
	
	public static boolean or(boolean w1, boolean w2)
	{
		return w1 || w2;
	}
	
	public static boolean imply(boolean w1, boolean w2)
	{
		boolean result;
		
		result = true;
		if (and(w1, not(w2)))
		{
			result = false;
		}
		
		return result;
	}
	
}