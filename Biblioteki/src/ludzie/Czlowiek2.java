package ludzie;

import java.util.Map;
import java.util.UUID;

import model.eng.DatabaseException;
import model.eng.Model;

public abstract class Czlowiek2 extends Model
{
	private final String surName;
	private final String forName;
	public Czlowiek2(String surName, String forName)
	{
		this.surName = surName;
		this.forName = forName;
	}
	
	public Czlowiek2(UUID uuid) throws DatabaseException
	{
		super(uuid);
		this.forName = this.data.get(this.mapFieldToDatabaseColumn("forName"));
		this.surName = this.data.get(this.mapFieldToDatabaseColumn("surName"));
	}

	@Override
	protected void load()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void prepareToSave()
	{
		// TODO Auto-generated method stub

	}
}
