package model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public class PredykatPublicznegoPola implements Predicate<Field>
{
	@Override
	public boolean test(Field pole)
	{
		int modyfikatory;
		boolean czyPubliczne;
		
		modyfikatory =  pole.getModifiers();
		czyPubliczne = Modifier.isPublic(modyfikatory);
		
		return czyPubliczne;
	}
		
}
