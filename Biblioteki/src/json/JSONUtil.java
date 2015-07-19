package json;

import json.JSONArray;

public class JSONUtil
{
	public static boolean hasElement(JSONArray arr, Object o)
	{
		boolean result = false;
		for (int i = 0; i < arr.length(); i++)
		{
			Object current = arr.get(i);
			if (o.equals(current))
			{
				result = true;
				break;
			}
		}
		return result;
	}
}
