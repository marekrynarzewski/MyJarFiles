package games.parity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class Parity 
{
	public static void main(String[] args)
	{
		runGame();
	}
	
	private static void runGame()
	{
		setSource();
		body();
		in.close();
	}
	
	private static void setSource()
	{
		if (fromFile)
		{
			setSourceFromFile();
		}
		else
		{
			setSourceFromIn();
		}
	}
	
	private static final boolean fromFile = false;
	
	private static void setSourceFromFile()
	{
		File file = new File(fileName);
		try
		{
			in = new Scanner(file);
		}
		catch(FileNotFoundException e)
		{
			System.exit(0);
		}
	}
	
	private static Scanner in;
	
	private static final String fileName = "parity.txt";
	
	private static void setSourceFromIn()
	{
		in = new Scanner(System.in);
	}
	
	private static void body()
	{
		int size = promptInt("Rozmiar zagadki: ");
		randomize(size);
		int maxCost = promptInt("Koszt maksymalny podpowiedzi: ");
		if (!generateCosts(maxCost))
		{
			prompt("Zbyt ma³y koszt podpowiedzi");
			System.exit(1);
		}
		currentCost = 0;
		showMenu();
		int state = promptInt("Wybór: ");
		while (state != 0)
		{
			showElement(state);
			showMenu();
			state = promptInt("Wybór: ");
		}
		finishGame();
	}
	
	private static void prompt(String msg)
	{
		System.out.print(msg);
	}
	
	private static void promptln(String msg)
	{
		prompt(msg+"\n");
	}
	
	private static int promptInt(String msg)
	{
		prompt(msg);
		int result = in.nextInt();
		return result;
	}
	private static boolean[] riddle;
	private static void randomize(int size) 
	{
		riddle = new boolean[size];
		boolean[] tf = new boolean[]{false, true};
		Random gen = new Random();
		for (int i = 0; i < size; i++)
		{
			int index = gen.nextInt(2);
			boolean elem = tf[index];
			riddle[i] = elem;
		}
	}
	
	private static boolean generateCosts(int maxCost)
	{
		if (maxCost < 1)
		{
			return false;
		}
		costs =  new TreeMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Integer> subCost;
		Random gen = new Random();
		for (int i = 0; i < riddle.length; i++)
		{
			subCost = new TreeMap<Integer, Integer>();
			for (int j = i; j < riddle.length; j++)
			{
				Integer value = gen.nextInt(maxCost)+1;
				subCost.put(j, value);
			}
			costs.put(i, subCost);
		}
		return true;
	}

	private static Map<Integer, Map<Integer, Integer>> costs;
	private static int currentCost;
	private static void showCosts()
	{
		promptln("Koszty podpowiedzi: ");
		for (Entry<Integer, Map<Integer, Integer>> entry : costs.entrySet())
		{
			for (Entry<Integer, Integer> subEntry : entry.getValue().entrySet())
			{
				Integer from = entry.getKey();
				Integer to = subEntry.getKey();
				Integer cost = subEntry.getValue();
				promptln("Podpowiedz parzystosci dla "+from+" do "+to+" wynosi "+cost);
			}
		}
	}
	private static void showMenu()
	{
		promptln("Menu:");
		promptln("0 - zakoñcz grê.");
		promptln("1 - zgadnij pod którymi kubkami jest kulka");
		promptln("2 - poproœ o podpowiedŸ");
	}
	private static void showElement(int position)
	{
		switch(position)
		{
			case 1:
				guessRiddle();
				break;
			case 2:
				askHint();
				break;
			default:
				break;
		}

	}
	

	private static void guessRiddle()
	{
		for (int i = 0; i < riddle.length; i++)
		{
			int ianswer = promptInt("Pod kubkiem "+i+" jest kulka (1-TAK): ");
			boolean banswer = ianswer == 1;
			if (riddle[i] != banswer)
			{
				promptln("Nie poprawna odpowiedz!");
				break;
			}
		}
		finishGame();
	}

	private static void askHint() 
	{
		showCosts();
		Integer from = promptInt("Indeks pierwszego kubka: ");
		Integer to = promptInt("Indeks drugiego kubka: ");
		try 
		{
			Integer cost = cost(from, to);
			promptln("Koszt tej podpowiedzi to: "+cost);
			currentCost += cost;
			int parity = parity(from, to);
			removeFromCosts(from, to);
			promptln("Parzystoœæ wyst¹pieñ kulek w zakresie "+from+", "+to+" wynosi: "+parity);
		}
		catch (InvalidRange e) 
		{
			promptln("Podany zakres jest bledny!");
		}
		catch(NullPointerException e)
		{
			promptln("Ju¿ wykorzysta³eœ podpowiedz dla tego zakresu");
		}
	}
	private static void removeFromCosts(Integer from, Integer to) throws InvalidRange 
	{
		if (0 <= from && from <= to && to < riddle.length)
		{
			Map<Integer, Integer> subCost;
			subCost = costs.get(from);
			subCost.remove(to);
			//return result;
		}
		else
		{
			throw new InvalidRange(from, to);
		}
		
	}

	private static int parity(int from, int to) throws InvalidRange
	{
		if (0 <= from && from <= to && to < riddle.length)
		{
			int sum = 0;
			for (int i = from; i <= to; i++)
			{
				boolean current = riddle[i];
				if (current)
				{
					sum ++;
				}
			}
			int result = sum % 2;
			return result;
		}
		else
		{
			throw new InvalidRange(from, to);
		}
	}
	
	private static void finishGame() 
	{
		String sriddle = Arrays.toString(riddle);
		promptln("Wynik to "+sriddle);
		promptln("Zakoñczy³eœ grê z kosztem "+currentCost);
		System.exit(0);
	}

	private static int cost(int from, int to) throws InvalidRange
	{
		if (0 <= from && from <= to && to < riddle.length)
		{
			Map<Integer, Integer> subCost;
			subCost = costs.get(from);
			int result = subCost.get(to);
			return result;
		}
		else
		{
			throw new InvalidRange(from, to);
		}
		
	}
	
	@SuppressWarnings("serial")
	private static class InvalidRange extends Exception
	{
		public final int from, to;
		InvalidRange(int from, int to)
		{
			this.from = from;
			this.to = to;
		}
		
		public String getMessage()
		{
			String result = "InvalidRange";
			result += "(from="+from+",";
			result += "to="+to+")";
			return result;
		}
		
		public String toString()
		{
			return "from="+from+" to="+to;
		}
	}
}

