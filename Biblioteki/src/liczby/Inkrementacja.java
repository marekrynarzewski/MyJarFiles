package liczby;

/**
 * generator which increases integer
 * @author Marek
 *
 */
public class Inkrementacja implements Generator<Integer>
{
	private final int liczba;
	
	/**
	 * generator, which increases an integer having a given number of liczba
	 * @param liczba integer by which other numbers will be increased 
	 */
	public Inkrementacja(int liczba)
	{
		this.liczba = liczba;
	}
	
	/**
	 * generator which increases integer
	 */
	public Inkrementacja()
	{
		this(1);
	}
	@Override
	public Integer next(Integer current)
	{
		return current+this.liczba;
	}

	@Override
	public boolean test(Integer current, Integer end)
	{
		return current < end;
	}

}
