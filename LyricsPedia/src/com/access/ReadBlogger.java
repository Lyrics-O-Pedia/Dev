package com.access;

import java.io.IOException;
import java.net.URL;

import com.google.gdata.client.blogger.BloggerService;
import com.google.gdata.data.Category;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.Person;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.ServiceException;

public class ReadBlogger {
	private static final String METAFEED_URL = "http://www.blogger.com/feeds/default/blogs";
	private static final String FEED_URI_BASE = "http://www.blogger.com/feeds";
	private static final String POSTS_FEED_URI_SUFFIX = "/posts/default";
	private static String username = "post.lyricsopedia";
	private static String password = "xyz";
	private static String feedUri;
	private static BloggerService myService;
	private static String blogId;

	public String connectBlogger() throws IOException, ServiceException {
		myService = new BloggerService("Lop-BloggerConnect-1");
		myService.setUserCredentials(username, password);
		final URL feedUrl = new URL(METAFEED_URL);
		Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

		// If the user has a blog then return the id (which comes after 'blog-')
		if (resultFeed.getEntries().size() > 0) {
			for (Entry entry : resultFeed.getEntries()) {
//				if (entry.getHtmlLink().getHref()
//						.contains("lyricsopedia.blogspot.com")) {
//					blogId = entry.getId().split("blog-")[1];
//					break;
//				}
				if (entry.getHtmlLink().getHref()
						.contains("lyricsopedia")) {
					blogId = entry.getId().split("blog-")[1];
					break;
				}
			}
		}

		System.out.println("blogId : " + blogId);
		return blogId;
	}

	public Entry createPost(String postTitle, String postContent, boolean isHtml,
			String authorName, boolean isDraft, String postLabel) throws IOException, ServiceException {
		feedUri = FEED_URI_BASE + "/" + blogId;
		// Create the entry to insert
		Entry myEntry = new Entry();
		myEntry.setTitle(new PlainTextConstruct(postTitle));
		if(isHtml) {
			myEntry.setContent(new HtmlTextConstruct(postContent));	
		}
		else{
			myEntry.setContent(new PlainTextConstruct(postContent));	
		}
		
		
		Person author = new Person(authorName, null, username);
		myEntry.getAuthors().add(author);
		
		// Creating Label for the post
		Category category= new Category();
		category.setTerm(postLabel);
		category.setLabel(null);
		category.setScheme("http://www.blogger.com/atom/ns#");
		category.setLabelLang(null);
		
		myEntry.getCategories().add(category);
		myEntry.setDraft(isDraft);

		// Ask the service to insert the new entry
		URL postUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
		return myService.insert(postUrl, myEntry);

	}
	
	public Entry updatePost(URL postUrl,Entry myEntry) throws IOException, ServiceException {
		return myService.update(postUrl, myEntry);
	}
	
	/**
	  * Displays the titles of all the posts in a blog. First it requests the posts
	  * feed for the blogs and then is prints the results.
	  * 
	  * @param myService An authenticated GoogleService object.
	  * @throws ServiceException If the service is unable to handle the request.
	  * @throws IOException If the URL is malformed.
	  */
	 public Entry findExistingMoviePost(String blogId, String label)
	     throws ServiceException, IOException {
	   // Request the feed
		 feedUri = FEED_URI_BASE + "/" + blogId;
	   URL feedUrl = new URL(feedUri + POSTS_FEED_URI_SUFFIX);
	   Feed resultFeed = myService.getFeed(feedUrl, Feed.class);

	   // Print the results
	   for (int i = 0; i < resultFeed.getEntries().size(); i++) {
	     Entry entry = resultFeed.getEntries().get(i);
	     PlainTextConstruct ptxt = (PlainTextConstruct)entry.getTitle();
	     if(ptxt.getPlainText().equals(label)) {
	    	 return entry; 
	     }
//	     for( Category ctg : entry.getCategories()) {
//	    	 if(ctg.getTerm().equals(label)) {
//	    	
//	    	 }
//	     }
	   }
	   return null;
	 }
	 

}
