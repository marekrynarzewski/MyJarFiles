package html;

import java.util.Vector;

public class Html {
	public static String stripTags(String inp)
	{
		Vector<String> znaczniki = new Vector<String>();
		String HTMLTag = "";
		boolean intag = false;
		String outp = "";
		Character prevChar = new Character(' ');
		for (int i=0; i < inp.length(); ++i)
		{
		    if (!intag && inp.charAt(i) == '<')
	        {
	            intag = true;
	            prevChar = inp.charAt(i);
	            continue;
	        }
		    if (intag && inp.charAt(i) == '/' && prevChar == '<')
		    {
		    	
		    }
	        if (intag && inp.charAt(i) == '>')
	        {
	            intag = false;
	            continue;
	        }
	        if (!intag)
	        {
	        	znaczniki.add(HTMLTag);
	            outp = outp + inp.charAt(i);
	            HTMLTag = "";
	        }
	        else
	        {
	        	HTMLTag += inp.charAt(i);
	        }
		}
		return outp;
	}
}
