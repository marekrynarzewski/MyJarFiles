package io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mapy.Mapy;
import pomoc.Tablice;
import io.Siec;

public class Google
{
	private static final String GlobalGoogleApiUrl = "https://www.googleapis.com/calendar/v3";
	private static final String clientId = "488680434076-48993f3fo358d4bj0ftpc6ae8pv58ihm.apps.googleusercontent.com";
	private static final String[] redirectUris = 
		new String[]{"urn:ietf:wg:oauth:2.0:oob", "http://localhost"};
	private static final String OAuthUrl = "https://accounts.google.com/o/oauth2";
	private static final String Auth = "/auth";
	private static final String secret = "qZds5Dv0jxKoxps_NIPjsIfK";
	private static final String accept_code = "4/BdiFRdErheU5gBKQ2lfFDsA8q_-x.YjH995ep5cUTOl05ti8ZT3ZFHmPGjAI";
	private static final String Token = "/token";
	public static class CalendarList
	{
		private static final String LocalApiUrl = "/users/me/calendarList/";
		public static String list() throws IOException
		{
			if (authorize())
			{
				return Siec.getString(GlobalGoogleApiUrl+LocalApiUrl);
			}
			return "";
		}
	}
	private static boolean authorize()
	{
		Map<String, String> params = buildParams();
		String url = OAuthUrl+Auth+"?"+Mapy.toString(params, "=", "&");
		try
		{
			Siec.sendGET(url, new HashMap<String, String>());
			url = OAuthUrl+Token;
			params = getToken();
			String s = Siec.sendPOST(url, new HashMap<String, String>(), params);
			System.out.println(s);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private static Map<String, String> getToken()
	{
		Map<String, String> result = new HashMap<String, String>();
		result.put("code", accept_code);
		result.put("client_id", clientId);
		result.put("client_secret", secret);
		result.put("redirect_uri", redirectUris[0]);
		result.put("grant_type", "authorization_code");
		return result;
	}
	
	private static Map<String, String> buildParams()
	{
		Map<String, String> result = new HashMap<String, String>();
		result.put("client_id", clientId);
		result.put("response_type", "code");
		result.put("scope", "https://www.googleapis.com/auth/calendar");
		result.put("access_type", "online");
		result.put("approval_prompt", "force");
		result.put("redirect_uri", redirectUris[0]);
		return result;
	}
	
	
}
