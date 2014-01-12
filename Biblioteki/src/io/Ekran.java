package io;

import java.util.Scanner;

public class Ekran 
{
	static Scanner wejscie = new Scanner(System.in);
	public static void wypisz(Object o)
	{
		System.out.print(o);
	}
	
	public static void wypiszZLinia(Object o)
	{
		System.out.println(o);
	}
	
	public static void info(Object msg)
	{
		wypiszZLinia("[INFO] "+msg.toString());
	}
	
	public static void ostrzez(Object msg)
	{
		wypiszZLinia("[!!!!] "+msg.toString());
	}
	
	public static void blad(Object msg, int status)
	{
		wypiszZLinia("[BLAD] "+msg.toString());
		System.exit(status);
	}
	
	public static String czytaj()
	{
		return wejscie.next();
	}
	
	public static String czytajLinie()
	{
		return wejscie.nextLine();
	}
}
