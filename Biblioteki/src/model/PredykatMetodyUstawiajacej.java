package model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public final class PredykatMetodyUstawiajacej implements Predicate<Method>
{
	private final String nazwaMetodyUstawiajacej;

	public PredykatMetodyUstawiajacej(String nazwaMetodyUzyskujacej)
	{
		this.nazwaMetodyUstawiajacej = nazwaMetodyUzyskujacej;
	}
	@Override
	public boolean test(Method metoda)
	{
		boolean czyPubliczna, zawierajaca, jednoArgumentowa, wynik;
		
		czyPubliczna = this.jestPubliczna(metoda);
		zawierajaca = this.czyZawieraUstawianie(metoda);
		jednoArgumentowa = this.czyJestJednoArgumentowa(metoda);
		wynik = czyPubliczna && zawierajaca && jednoArgumentowa;
		
		return czyPubliczna;
	}
	
	private final boolean czyZwracaWynik(Method metoda)
	{
		Class<?> typZwracany;
		boolean wynik;
		
		typZwracany = metoda.getReturnType();
		wynik = !typZwracany.equals(Void.TYPE);
		
		return wynik;
	}

	private final boolean czyZawieraUstawianie(Method metoda)
	{
		String nazwaMetody;
		boolean wynik;
		
		nazwaMetody = metoda.getName();
		wynik = nazwaMetody.startsWith(nazwaMetodyUstawiajacej);
		
		return wynik;
	}

	private final boolean czyJestJednoArgumentowa(Method metoda)
	{
		int liczbaArgumentow;
		boolean wynik;
		
		liczbaArgumentow = metoda.getParameterCount();
		wynik = (liczbaArgumentow == 0);
		
		return wynik;
	}

	private final boolean jestPubliczna(Method metoda)
	{
		int modyfikatory;
		boolean czyPubliczna;
		
		modyfikatory =  metoda.getModifiers();
		czyPubliczna = Modifier.isPublic(modyfikatory);
		
		return czyPubliczna;
	}
	
	public final String uzyskajNazweMetodyUstawiajacej()
	{
		return this.nazwaMetodyUstawiajacej;
	}
}
