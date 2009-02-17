package umn.cs.diw.gwt.mini.server;

import java.net.URL;
import java.util.Iterator;

import umn.cs.diw.gwt.mini.client.GetFeedService;
import umn.cs.diw.gwt.mini.client.datamodel.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class GetFeedServiceImpl extends RemoteServiceServlet implements GetFeedService {
	
	// we'll use this url in a bit for our tag syndication service
	private final String feedProviderUrl = "http://feeds.technorati.com/tag/";
	
	// append this string to the end of a technorati query for better results	
	private final String feedQuery = "?authority=a4&language=en";
	
	public FeedItemList getFeedItems(String tag) {
	    FeedItemList list = new FeedItemList();
	    list.addFeedItem("Google is a search engine", "htp://www.google.com",
			     "Larry", "Google is a search engine");
	    return list;
	}
}
