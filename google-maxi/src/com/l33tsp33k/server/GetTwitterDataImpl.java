package com.l33tsp33k.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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

	private final String feedProviderUrl = "http://search.twitter.com/search.atom?rpp=100&lang=en&q=";

	public ArrayList<TwitterItem> getGeoreferencedFeedItems(ArrayList<TwitterItem> items){
		ArrayList<TwitterItem> ret = new ArrayList<TwitterItem>();
		try{
			for(int i=0; i<20 && i<items.size(); i++){
				String feedURI = "http://twitter.com/users/show/"+items.get(i).getUsername()+".xml";
				URL u = new URL(feedURI);
				HttpURLConnection hu = (HttpURLConnection)u.openConnection();

				BufferedReader br;
				try{br = new BufferedReader(new InputStreamReader(hu.getInputStream()));}
				catch(IOException e){
					continue;
				}
				String input="";
				while((input=br.readLine())!=null){
					if(input.contains("location")){
						String loc = input.substring(input.indexOf("location")+9, input.lastIndexOf("location")-2);
						Location l = AddressToCord.convertToCord(loc);
						if(l.accuracy>3){
							TwitterItem ti = items.get(i);
							ti.setLocation(loc);
							ti.setCoords(l.getLon(), l.getLat());
							ret.add(ti);
						}
					}
				}
			}

		} 
		catch (Exception e) {
			e.printStackTrace(); 
			return ret;
		}	    
		return ret;
	}

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
				String uri = entry.getLink();
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
