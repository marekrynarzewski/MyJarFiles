package model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public final class PredykatMetodyUzyskujacej implements Predicate<Method>
{
	private final String nazwaMetodyUzyskujacej;

	public PredykatMetodyUzyskujacej(String nazwaMetodyUzyskujacej)
	{
		this.nazwaMetodyUzyskujacej = nazwaMetodyUzyskujacej;
	}
	@Override
	public boolean test(Method metoda)
	{
		boolean czyPubliczna, zwracajacaWynik, zawierajaca, bezArgumentowa, wynik;
		
		czyPubliczna = this.jestPubliczna(metoda);
		zwracajacaWynik = this.czyZwracaWynik(metoda);
		zawierajaca = czyZawieraUzykiwanie(metoda);
		bezArgumentowa = this.czyBezArgumentowa(metoda);
		wynik = czyPubliczna && zawierajaca && zwracajacaWynik && bezArgumentowa;
		
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

	private final boolean czyZawieraUzykiwanie(Method metoda)
	{
		String nazwaMetody;
		boolean wynik;
		
		nazwaMetody = metoda.getName();
		wynik = nazwaMetody.startsWith(nazwaMetodyUzyskujacej);
		
		return wynik;
	}

	private final boolean czyBezArgumentowa(Method metoda)
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
	
	public final String uzyskajNazweMetodyUzyskujacej()
	{
		return this.nazwaMetodyUzyskujacej;
	}
}
