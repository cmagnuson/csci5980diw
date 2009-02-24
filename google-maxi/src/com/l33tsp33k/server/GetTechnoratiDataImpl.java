package com.l33tsp33k.server;

import java.net.URL;
import java.util.Iterator;

import com.l33tsp33k.client.GetTechnoratiData;
import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class GetTechnoratiDataImpl extends RemoteServiceServlet implements GetTechnoratiData {
	
	private static final long serialVersionUID = 1L;

	// we'll use this url in a bit for our tag syndication service
	private final String feedProviderUrl = "http://feeds.technorati.com/tag/";
	
	// append this string to the end of a technorati query for better results	
	@SuppressWarnings("unused")
	private final String feedQuery = "?authority=a4&language=en";
	
	@SuppressWarnings("unchecked")
	public TechnoratiItemList getFeedItems(String tag) {
	    TechnoratiItemList list = new TechnoratiItemList();
	    try{
	    	  String feedURI = feedProviderUrl + tag;
	    	  SyndFeedInput input = new SyndFeedInput();
	    	  SyndFeed feed = input.build(new XmlReader(new URL(feedURI)));
	    		    
	    	  // Iterate through feed items
	    	  
	    	  Iterator entryIter = feed.getEntries().iterator();
	    	  while (entryIter.hasNext()) {
	    	    SyndEntry entry = (SyndEntry) entryIter.next();
	    	    String title = entry.getTitle();
	    	    String uri = entry.getUri();
	    	    String author = entry.getAuthor();
	    	    String desc = entry.getDescription().getValue();
	    	    list.addFeedItem(title, uri, author, desc);
	    	  }
	    	} catch (Exception e) {
	    	      	e.printStackTrace(); 
	    	}	    
	    return list;
	}
}
