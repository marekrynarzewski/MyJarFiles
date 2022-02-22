package myjarfiles.io;

import java.util.Scanner;

public class Console 
{
	static Scanner input = new Scanner(System.in);
	
	public static void writeOut(Object o)
	{
		System.out.print(o);
	}
	
	public static void writeOutLine(Object o)
	{
		System.out.println(o);
	}
	
	public static void info(Object msg)
	{
		writeOutLine("[INFO] "+msg.toString());
	}
	
	public static void warn(Object msg)
	{
		writeOutLine("[!!!!] "+msg.toString());
	}
	
	public static void error(Object msg, int status)
	{
		writeOutLine("[BLAD] "+msg.toString());
		System.exit(status);
	}
	
	public static String readString()
	{
		return input.next();
	}
	
	public static String readLine()
	{
		return input.nextLine();
	}

	public static int promptInt(String ask) 
	{
		return input.nextInt();
	}

}
