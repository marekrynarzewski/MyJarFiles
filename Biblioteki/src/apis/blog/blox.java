package apis.blog;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

import cfg.Config;

public class blox 
{
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<String> getEntryTagsForEntry(String postId) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, postId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		Object oresult = BlogApi.client.execute("blox.getEntryTagsForEntry", params);
		
		return objectToVector(oresult);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Vector<String> setEntryTagsForEntry(List<String> tags, String postId) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, tags);
		params.add(1, postId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		
		Object oresult = BlogApi.client.execute("blox.setEntryTagsForEntry", params);
		
		return objectToVector(oresult);
	}
	
	static Vector<String> getAllEntryTags(String blogId) throws XmlRpcException
	{
		Vector<String> params = new Vector<String>();
		
		params.add(0, String.valueOf(blogId));
		params.add(1, Config.USER);
		params.add(2, Config.PASS);

		Object oresult = BlogApi.client.execute("blox.getAllEntryTags", params);
		
		return objectToVector(oresult);
	}
	
	private static Vector<String> objectToVector(Object o)
	{
		Object[] aresult = (Object[]) o;
		Vector<String> result = new Vector<String>();
		for (Object tag : aresult)
		{
			result.add((String) tag);
		}
		return result;
	}
}
