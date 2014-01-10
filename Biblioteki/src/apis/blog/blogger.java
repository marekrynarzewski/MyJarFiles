package apis.blog;

import java.util.HashMap;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;

import cfg.Config;

public class blogger 
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static String newPost(String blogId, String content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();

		params.add(0, "");
		params.add(1, blogId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		params.add(4, content);
		params.add(5, publish);
		
		return (String) BlogApi.client.execute("blogger.newPost", params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static boolean editPost(String postId, String content, boolean publish) throws XmlRpcException
	{
		Vector params = new Vector();

		params.add(0, "");
		params.add(1, postId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		params.add(4, content);
		params.add(5, publish);
		
		return (boolean) BlogApi.client.execute("blogger.editPost", params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static HashMap getPost(String postId) throws XmlRpcException
	{
		Vector params = new Vector();

		params.add(0, "");
		params.add(1, postId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		
		Object oresult = BlogApi.client.execute("blogger.getPost", params);
		HashMap hmresult = (HashMap) oresult;
		return hmresult;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<HashMap> getRecentPosts(String blogId, int numberOfPosts) throws XmlRpcException
	{
		Vector params = new Vector();
		params.add(0, "");
		params.add(1, blogId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		params.add(4, numberOfPosts);
		
		Object oresult = BlogApi.client.execute("blogger.getRecentPosts", params);
		Object[] aresult = (Object[]) oresult;
		Vector result = new Vector();
		for (Object post : aresult)
		{
			HashMap hmpost = (HashMap) post;
			result.add(hmpost);
		}
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static boolean deletePost(String postId) throws XmlRpcException
	{
		Vector params = new Vector();

		params.add(0, "");
		params.add(1, postId);
		params.add(2, Config.USER);
		params.add(3, Config.PASS);
		params.add(4, false);
		
		return (boolean) BlogApi.client.execute("blogger.deletePost", params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Vector<HashMap> getUsersBlogs() throws XmlRpcException
	{
		Vector<HashMap> result = new Vector<HashMap>();
		Vector params = new Vector();
		params.add(0, "");
		params.add(1, Config.USER);
		params.add(2, Config.PASS);
		
		Object oresult = BlogApi.client.execute("blogger.getUsersBlogs", params);
		Object[] aresult = (Object[]) oresult;
		
		for (Object blog : aresult)
		{
			HashMap map = (HashMap) blog;
			result.add(map);
		}
		return result;
	}
}
