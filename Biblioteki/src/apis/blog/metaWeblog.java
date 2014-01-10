package apis.blog;
import java.util.HashMap;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

import cfg.Config;

public class metaWeblog 
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static String snewPost(String blogId, HashMap content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		addIgnoredFields(content);
		params.add(3, content);
		
		params.add(4, publish);
		
		return (String) BlogApi.client.execute("metaWeblog.newPost", params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static HashMap onewPostExt(String blogId, HashMap content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		addIgnoredFields(content);
		params.add(3, content);
		
		params.add(4, publish);
		
		Object oresult = BlogApi.client.execute("metaWeblog.newPostExt", params);
		return objectToHashMap(oresult);

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static boolean beditPost(String postId, HashMap content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, postId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		addIgnoredFields(content);
		params.add(3, content);
		
		params.add(4, publish);
		
		Object oresult = BlogApi.client.execute("metaWeblog.editPost", params);
		boolean bresult = (Boolean) oresult;
		return bresult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static HashMap oeditPost(String postId, HashMap content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, postId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		addIgnoredFields(content);
		params.add(3, content);
		
		params.add(4, publish);
		
		Object oresult = BlogApi.client.execute("metaWeblog.editPostExt", params);
		return objectToHashMap(oresult);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static HashMap getPost(String postId) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, postId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		Object oresult = BlogApi.client.execute("metaWeblog.getPost", params);
		return objectToHashMap(oresult);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<HashMap> getRecentPosts(String blogId, int numberOfPosts) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		params.add(3, numberOfPosts);
		
		Object oresult = BlogApi.client.execute("metaWeblog.getRecentPosts", params);
		Object[] aresult = (Object[]) oresult;
		Vector<HashMap> result = new Vector<HashMap>();
		for (Object recentPost : aresult)
		{
			HashMap hmresult = (HashMap) recentPost;
			result.add(hmresult);
		}
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<HashMap> getCategories(String blogId) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		Object oresult = BlogApi.client.execute("metaWeblog.getCategories", params);
		Object[] aresult = (Object[]) oresult;
		Vector<HashMap> result = new Vector<HashMap>();
		for (Object ocat: aresult)
		{
			HashMap hmcat = (HashMap) ocat;
			result.add(hmcat);
		}
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static HashMap newMediaObject(String blogId, HashMap file) throws XmlRpcException
	{
		Vector params = new Vector();
		
		params.add(0, blogId);
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		params.add(3, file);
		
		Object oresult = BlogApi.client.execute("metaWeblog.newMediaObject", params);
		return objectToHashMap(oresult);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addIgnoredFields(HashMap content)
	{
		content.put("flNotOnHomePage", "");
		content.put("pubDate", "");
		content.put("guid", "");
		content.put("author", "");
	}
	
	@SuppressWarnings("rawtypes")
	private static HashMap objectToHashMap(Object o)
	{
		return (HashMap) o;
	}
}
