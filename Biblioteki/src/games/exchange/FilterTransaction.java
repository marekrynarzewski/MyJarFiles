package games.exchange;

import org.apache.commons.collections4.Predicate;

public class FilterTransaction implements Predicate<Transaction>
{
	private Wallet p;
	FilterTransaction(Wallet p)
	{
		this.p = p;
	}
	@Override
	public boolean evaluate(Transaction t)
	{
		if (t.destination.equals(p) || t.source.equals(p))
		{
			return true;
		}
		return false;
	}

}

