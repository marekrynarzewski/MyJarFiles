package data;

import java.util.Calendar;
import java.util.Date;

import liczby.Generator;

public class GeneratorDat implements Generator<Date>
{

	@Override
	public Date next(Date current)
	{
		return this.next(current, 1);
	}
	
	public Date next(Date current, int count)
	{
		return this.next(current, count, Calendar.DAY_OF_MONTH);
	}
	
	public Date next(Date current, int count, int field)
	{
		Calendar kalendarz;
		Date wynik;
		
		kalendarz = Calendar.getInstance();
		kalendarz.setTime(current);
		kalendarz.add(field, count);
		wynik = kalendarz.getTime();
		
		return wynik;
		
	}

	@Override
	public boolean test(Date current, Date end)
	{
		boolean cond1, cond2, result;
		
		cond1 = current.before(end);
		cond2 = current.equals(end);
		result = cond1 || cond2;
		
		return result;
	}

}
