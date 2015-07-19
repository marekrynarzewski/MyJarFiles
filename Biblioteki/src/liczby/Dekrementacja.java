package liczby;

public class Dekrementacja extends Inkrementacja
{
	public Dekrementacja(int liczba)
	{
		super(-liczba);
	}
	
	public Dekrementacja()
	{
		this(1);
	}
	
	@Override
	public boolean test(Integer current, Integer end)
	{
		return current > end;
	}
	
	
}
