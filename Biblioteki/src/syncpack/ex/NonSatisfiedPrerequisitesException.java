package syncpack.ex;

import java.util.ArrayList;
import java.util.Collection;

public class NonSatisfiedPrerequisitesException extends Exception
{
	ArrayList<String> messages;
	public NonSatisfiedPrerequisitesException(ArrayList<String> messages)
	{
		this.messages = messages;
	}
	
	public String getMessage()
	{
		String message = getMessage(0);
		return message;
	}
	private String getMessage(int index)
	{
		String message = messages.get(index);
		return message;
	}

	public Collection<String> getMessages()
	{
		return messages;
	}
}
