package apis.blog;

import java.util.HashMap;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

import cfg.Config;

public class APIMovableType
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<HashMap> getRecentPostsTitles(String blogId, int numberOfPosts) throws XmlRpcException
	{
		Vector params = new Vector();

		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		params.add(3, numberOfPosts);
		
		Object oresult = BlogApi.client.execute("mt.getRecentPostTitles", params);
		Object[] aresult = (Object[]) oresult;
		Vector<HashMap> result = new Vector<HashMap>();
		for (Object postTitles : aresult)
		{
			HashMap hmpostTitles = (HashMap) postTitles;
			result.add(hmpostTitles);
		}
		return result;
	}
}
