package myjarfiles.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import myjarfiles.array.Maps;

public class Network 
{
	public static String getString(String url) throws IOException
	{
		URL lacze = new URL(url);
		InputStream strumien = lacze.openStream();
		String result =  getString(strumien);
		strumien.close();
		return result;
	}
	
	public static String send(String url, String metoda, Map<String, String> requestProperty, 
			boolean doOutput, Map<String, String> postParameters) throws IOException
	{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod(metoda);
 
		//add request header
		//con.setRequestProperty("User-Agent", USER_AGENT);
		for (Entry<String, String> entry : requestProperty.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			con.setRequestProperty(key, value);
		}
		
		String serializedPostParamaters = Maps.toString(postParameters, "=", "&");
		con.setDoOutput(doOutput);
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(serializedPostParamaters);
		wr.flush();
		wr.close();
		
		InputStream is = con.getInputStream();
		String result = getString(is);
		is.close();
		return result;
	}
	
	public static String send(String url, String metoda, Map<String, String> requestProperty) throws IOException
	{
		Map<String, String> postParameters = new HashMap<String, String>();
		String result = send(url, metoda, requestProperty, false, postParameters);
		return result;
	}
	
	public static String send(String url, String metoda, Map<String, String> requestProperty, Map<String, String> postParameters) throws IOException
	{
		String result = send(url, metoda, requestProperty, true, postParameters);
		return result;
	}
	
	public static String sendGET(String url, Map<String, String> requestProperty) throws IOException
	{
		String result = send(url, "GET", requestProperty);
		return result;
	}
	
	public static String sendPOST(String url, Map<String, String> requestProperty, Map<String, String> postParameters) throws IOException
	{
		String result = send(url, "POST", requestProperty, postParameters);
		return result;
	}
	public static String getString(InputStream strumien) throws IOException
	{
		InputStreamReader isr = new InputStreamReader(strumien);
		BufferedReader in = new BufferedReader(isr);
        String inputLine, result = "";
        while ((inputLine = in.readLine()) != null)
        {
        	result += inputLine;
        }
        in.close();
        isr.close();
        return result;
	}
	
	public static final String applicationJSON = "application/json";
	
	/*public static JSONObject getJSONObject(String url) throws IOException
	{
		String tmp = getString(url);
		JSONObject result = new JSONObject(tmp);
		return result;
	}
	
	public static JSONArray getJSONArray(String url) throws IOException
	{
		String tmp = getString(url);
		JSONArray result = new JSONArray(tmp);
		return result;
	}*/
}
