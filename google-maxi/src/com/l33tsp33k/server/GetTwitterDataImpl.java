package com.l33tsp33k.server;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l33tsp33k.client.GetTwitterData;
import com.l33tsp33k.client.datamodels.TwitterItem;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class GetTwitterDataImpl extends RemoteServiceServlet implements GetTwitterData {

	private static final long serialVersionUID = 1L;

	private final String feedProviderUrl = "http://search.twitter.com/search.atom?q=";

	@SuppressWarnings("unchecked")
	public ArrayList<TwitterItem> getFeedItems(String tag) {
		ArrayList<TwitterItem> list = new ArrayList<TwitterItem>();
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
				String desc = "";
				if(entry.getDescription()!=null){
					entry.getDescription().getValue();
				}
				list.add(new TwitterItem(title, uri, author, desc));
			}
		} catch (Exception e) {
			e.printStackTrace(); 
		}	    
		return list;
	}
}
