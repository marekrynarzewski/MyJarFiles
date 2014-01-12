/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package apis.blog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import cfg.Config;

/**
 *
 * @author Marek
 */
public class BlogApi {
    static XmlRpcClient client = new XmlRpcClient();
    private XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    
    public BlogApi()
    {
    	try {
			this.config.setServerURL(new URL(Config.XmlRpc));
			client.setConfig(this.config);
		} catch (MalformedURLException e) {}
    }
    
    @SuppressWarnings("rawtypes")
	public Vector<HashMap> getRecentPostTitles(int blogid, int numberOfPosts) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return APIMovableType.getRecentPostsTitles(blogId, numberOfPosts);
    }
    
    public Vector<String> getEntryTagsForEntry(int postid) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return blox.getEntryTagsForEntry(postId);
    }
    
    public Vector<String> setEntryTagsForEntry(List<String> tags, int postid) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return blox.setEntryTagsForEntry(tags, postId);
    }
    
    public Vector<String> getAllEntryTags(int blogid) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return blox.getAllEntryTags(blogId);
    }
    
    public int newSimplePost(int blogid, String content, boolean publish) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	String postId = blogger.newPost(blogId, content, publish);
    	return Integer.valueOf(postId);
    }
    
    public boolean editSimplePost(int postid, String content, boolean publish) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return blogger.editPost(postId, content, publish);
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap getSimplePost(int postid) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return blogger.getPost(postId);
    }
    
    @SuppressWarnings("rawtypes")
	public Vector<HashMap> getRecentSimplePost(int blogid, int numberOfPosts) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return blogger.getRecentPosts(blogId, numberOfPosts);
    }
    
    public boolean deletePost(int postid) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return blogger.deletePost(postId);
    }
    
    @SuppressWarnings("rawtypes")
	public Vector<HashMap> getUsersBlogs() throws XmlRpcException
    {
    	return blogger.getUsersBlogs();
    }
    
    @SuppressWarnings("rawtypes")
	public int inewAdvancedPost(int blogid, HashMap content, boolean publish) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	String postId = metaWeblog.snewPost(blogId, content, publish);
    	return Integer.valueOf(postId);
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap newAdvancedPost(int blogid, HashMap content, boolean publish) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return metaWeblog.onewPostExt(blogId, content, publish);
    }
    
    @SuppressWarnings("rawtypes")
	public boolean beditAdvancedPost(int postid, HashMap content, boolean publish) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return metaWeblog.beditPost(postId, content, publish);
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap editAdvancedPost(int postid, HashMap content, boolean publish) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return metaWeblog.oeditPost(postId, content, publish);
    }
    
    @SuppressWarnings("rawtypes")
	public HashMap getAdvancedPost(int postid) throws XmlRpcException
    {
    	String postId = String.valueOf(postid);
    	return metaWeblog.getPost(postId);
    }
    
    @SuppressWarnings("rawtypes")
	public Vector<HashMap> getRecentAdvancedPost(int blogid, int numberOfPosts) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return metaWeblog.getRecentPosts(blogId, numberOfPosts);
    }
    
    @SuppressWarnings("rawtypes")
	public Vector<HashMap> getCategories(int blogid) throws XmlRpcException
    {
    	String blogId = String.valueOf(blogid);
    	return metaWeblog.getCategories(blogId);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap newMediaObject(int blogid, String sourcePath, String destinationName) throws IOException, XmlRpcException
    {
    	HashMap file = new HashMap();
    	
    	Path path = Paths.get(sourcePath);
    	byte[] bits = Files.readAllBytes(path);
    	
    	String mime = Files.probeContentType(path);
    	file.put("name", destinationName);
    	file.put("type", mime);
    	file.put("bits", bits);
    	String blogId = String.valueOf(blogid);
    	return metaWeblog.newMediaObject(blogId, file);
    }
    
    @SuppressWarnings({ "rawtypes" })
	public HashMap newMediaObject(int blogid, String sourcePath) throws IOException, XmlRpcException
    {
    	File f = new File(sourcePath);
    	String destinationName = f.getName();
    	return newMediaObject(blogid, sourcePath, destinationName);
    }
}
