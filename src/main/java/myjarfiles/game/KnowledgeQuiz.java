package myjarfiles.game;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import myjarfiles.io.Console;

public class KnowledgeQuiz
{
	private boolean isContinued = true;
	
	private int gameNumber = 1;

	private int won = 0;

	private int rate = 0;

	private int tries = 3;

	public static void main(String[] args)
	{
		KnowledgeQuiz game;
		
		try
		{
			game = new KnowledgeQuiz();
			while (game.isContinued)
			{
				game.status();
				game.askForRate();
				game.informacjaOZachowkuISukcesie();
				game.nastepnaGra();
				game.zapytajOKontynuacje();
			}
			game.informacjeOWygranej();
		}
		catch (IOException e)
		{
			Console.error(e.getMessage(), -3);
		}		
	}

	private void status() 
	{
		Console.info("Numer gry: "+this.gameNumber );
		Console.info("Wygrana: "+this.won);		
	}
	
	private void askForRate() 
	{
		if (1 == this.gameNumber)
		{
			Console.info("Rate: "+this.rate);
			return;
		}
		int rate = Console.promptInt("Rate: ");
		boolean correctRate = (rate > 0) && (rate <= this.won);
		if (!correctRate && this.tries > 0)
		{
			this.tries -= 1;
			this.askForRate();
		}
		else if (this.tries == 0)
		{
			Console.error("Przekroczono liczbe blednych stawek!", -3);
		}
		else
		{
			this.rate = rate;
		}
	}
}