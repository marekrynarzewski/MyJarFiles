package model.eng;

public class DatabaseException extends Exception
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4825705911226606481L;

	public DatabaseException()
	{
		
	}
	
	public DatabaseException(String query)
	{
		super(query);
	}
	
	public DatabaseException(Exception exception)
	{
		super(exception);
	}
	
	public DatabaseException(Throwable throwable)
	{
		super(throwable);
	}
}
