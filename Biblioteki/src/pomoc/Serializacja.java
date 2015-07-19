package pomoc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import json.JSONArray;
import json.JSONException;
import json.JSONObject;
import myutils.Ekran;

public class Serializacja
{
	public static JSONObject toJSON(JSONArray wejscie)
	{
		
		JSONObject wynik = new JSONObject();
		for (int i = 0; i < wejscie.length(); i++)
		{
			wynik.put(String.valueOf(i), wejscie.get(i));
		}
		return wynik;
	}
	
	/**
	 * serializuje pola obiektu implementujacego JSONable
	 * @param obj obiekt implementujacy
	 * @return wynik w JSONObiekcie
	 */
	public static JSONObject toJSON(JSONable obj)
	{
		JSONObject wynik = new JSONObject();
		List<String> pola = obj.polaJSONowe();
		List<Field> wszystkiePola = new ArrayList<Field>();
		wszystkiePola = getAllFields(wszystkiePola, obj.getClass());
		Iterator<String> iterator = pola.iterator();
		while (iterator.hasNext())
	    {
			String name = iterator.next();
			try
			{
				Field field = inArray(wszystkiePola, name);
				if (field != null)
				{
					field.setAccessible(true);
					Object value = field.get(obj);
					wynik.put(name, value);
				}
				else
				{
					Ekran.ostrzez("Brak pola o nazwie "+name);
				}
			}
			catch (IllegalArgumentException | IllegalAccessException | SecurityException e)
			{
				Ekran.ostrzez("Brak pola o nazwie "+name);
			}
	    }
		return wynik;
	}
	public static Field inArray(List<Field> lista, String nazwa)
	{
		for (int i = 0; i < lista.size(); i++)
		{
			Field obecnePole = lista.get(i);
			String obecnaNazwa = obecnePole.getName();
			if (obecnaNazwa.equals(nazwa))
			{
				return obecnePole;
			}
		}
		return null;
	}
	/**
	 * interfejs okreslajacy zdolnosc obiektu do przeksztalcenia w JSONa
	 * @author Marek
	 *
	 */
	public static interface JSONable
	{
		/**
		 * zwraca instancje obiektu w JSONie
		 * @return JSONObject wynik serializacji
		 * @throws JSONException - rzucany jesli wystapi blad JSONowy
		 */
		JSONObject toJSON() throws JSONException;
		/**
		 * zwraca liste pol, ktore maja zostac zserializowane w funkcji {@link Serializacja.toJSON(JSONable)}
		 * @return lista stringow
		 */
		List<String> polaJSONowe();
	}

	public static interface SQLable
	{
		String SQLInsert();
		String SQLUpdate();
		String SQLDelete();
		//JSONObject SQLSelect();
	}
	/**
	 * funkcja ktora szuka w tablicy okreslonej wartosci
	 * @param tablica tablica, w ktorej szuka
	 * @param wartosc wartosc, ktora szuka
	 * @return prawda jesli {@code wartosc} jest jednym z elementow {@code tablicy}
	 */
	public static <Typ> boolean inArray(Typ[] tablica, Typ wartosc)
	{
		int wynik = Arrays.binarySearch(tablica, wartosc);
		return (wynik != -1);
	}

	/**
	 * zwraca tablice lancuchow znakow nazw klas atomowych
	 * @return tablica
	 */
	private static String[] klasyAtomowe()
	{
		String[] wynik = new String[7];
		int i = 0;
		wynik[i] = java.lang.String.class.getName();
		i++;
		wynik[i] = java.lang.Boolean.class.getName();
		i++;
		wynik[i] = java.lang.Short.class.getName();
		i++;
		wynik[i] = java.lang.Long.class.getName();
		i++;
		wynik[i] = java.lang.Integer.class.getName();
		i++;
		wynik[i] = java.lang.Character.class.getName();
		i++;
		wynik[i] = java.lang.Byte.class.getName();
		i++;
		return wynik;
	}
	
	/**
	 * serializuje pola klasa o typu atomowym, czyli integer, double, float, long, boolean, character, String
	 * @param obj
	 * @return
	 */
	public static JSONObject serializujPolaAtomowe(Object obj)
	{
		JSONObject wynik = new JSONObject();
		try
		{
			String[] klasyAtomowe = klasyAtomowe();
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field field : fields)
			{
				String typ = field.getType().getName();
				if (inArray(klasyAtomowe, typ))
				{
					String name = field.getName();
					field.setAccessible(true);
					Object value = field.get(obj);
					wynik.put(name, value);
				}
			}
		}
		catch (IllegalArgumentException | IllegalAccessException | SecurityException e)
		{
			
		}
		return wynik;
	}
	
	/**
	 * zlacza dwa JSONObiekty w jeden
	 * @param d1 pierwszy
	 * @param d2 drugi
	 * @return wynik mergu
	 */
	public static JSONObject mergeJSON(JSONObject d1, JSONObject d2)
	{
		JSONObject merged = new JSONObject(d1, JSONObject.getNames(d1));
		for(String key : JSONObject.getNames(d2))
		{
			merged.put(key, d2.get(key));
		}
		return merged;
	}
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) 
	{
	    for (Field field: type.getDeclaredFields()) {
	        fields.add(field);
	    }

	    if (type.getSuperclass() != null) {
	        fields = getAllFields(fields, type.getSuperclass());
	    }

	    return fields;
	}
}
