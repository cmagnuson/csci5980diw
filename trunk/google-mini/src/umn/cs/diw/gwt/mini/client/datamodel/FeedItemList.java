package umn.cs.diw.gwt.mini.client.datamodel;

import com.google.gwt.user.client.rpc.*;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Here we define a simple FeedItemList class 
 * feel free to extend (or get rid of) it
 * as you like
 * 
 * @author jordan
 *
 */

public class FeedItemList implements IsSerializable {
	
	private ArrayList<FeedItem> feedItemList;

	public FeedItemList(){
	  feedItemList = new ArrayList<FeedItem>();
	}

	public void addFeedItem(String name, String link, String author, String value) {
	  feedItemList.add(new FeedItem(name, link, author, value));
	}

	public int getSize(){
	  return this.feedItemList.size();
	}
	
	public Iterator<FeedItem> iterator() {
		return this.feedItemList.iterator();
	}

	public FeedItem getFeedItem(int index){
	  return this.feedItemList.get(index); 
	}

	public String toString(){
	  String retval;

	  retval = "list.size="+this.getSize()+"\n";

	  for(int i=0; i<getSize(); i++){
	    retval += this.getFeedItem(i) + "\n";
	  }

	  return retval;
	}
}
