package myjarfiles.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;
import myjarfiles.array.Maps;

public class Google
{
	private static final String GlobalGoogleApiUrl = "https://www.googleapis.com/calendar/v3";
	private static String clientId = null;
	private static final String[] redirectUris = 
		new String[]{"urn:ietf:wg:oauth:2.0:oob", "http://localhost"};
	private static final String OAuthUrl = "https://accounts.google.com/o/oauth2";
	private static final String Auth = "/auth";
	private static String secret = null;
	private static String accept_code = null;
	private static final String Token = "/token";
	
	public static void loadEnv()
	{
		Dotenv dotenv = Dotenv.load();
		Google.accept_code = dotenv.get("GOOGLE_CALENDAR_ACCEPTCODE");
		Google.secret = dotenv.get("GOOGLE_CALENDAR_SECRET");
		Google.clientId = dotenv.get("GOOGLE_CALENDAR_CLIENTID");
	}
	public static class CalendarList
	{
		private static final String LocalApiUrl = "/users/me/calendarList/";
		public static String list() throws IOException
		{
			Google.loadEnv();
			if (authorize())
			{
				return Network.getString(GlobalGoogleApiUrl+LocalApiUrl);
			}
			return "";
		}
	}
	private static boolean authorize()
	{
		Map<String, String> params = buildParams();
		String url = OAuthUrl+Auth+"?"+Maps.toString(params, "=", "&");
		try
		{
			Network.sendGET(url, new HashMap<String, String>());
			url = OAuthUrl+Token;
			params = getToken();
			String s = Network.sendPOST(url, new HashMap<String, String>(), params);
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

