package com.l33tsp33k.server;

import java.net.URL;
import java.util.ArrayList;
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
	private final String apiKey = "1c448f36f2564d9a126ae581078b818e";
	private final String feedProviderUrl = "http://api.technorati.com/search";

	// append this string to the end of a technorati query for better results	
	@SuppressWarnings("unused")
	private final String feedQuery = "?key=" + apiKey + "&format=rss&authority=a4&language=en&limit=100&query=";

	@SuppressWarnings("unchecked")
	public ArrayList<TechnoratiItem> getFeedItems(String tag) {
		ArrayList<TechnoratiItem> list = new ArrayList<TechnoratiItem>();
		try{
			String feedURI = feedProviderUrl + feedQuery + tag;
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(new URL(feedURI)));

			// Iterate through feed items

			Iterator entryIter = feed.getEntries().iterator();
			while (entryIter.hasNext()) {
				SyndEntry entry = (SyndEntry) entryIter.next();
				String title = entry.getTitle();
				String uri = entry.getUri();
				String author = entry.getAuthor();
				String desc = "";
				if(entry.getDescription()!=null){
					entry.getDescription().getValue();
				}
				list.add(new TechnoratiItem(title, uri, author, desc));
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}	    
		return list;
	}
}
