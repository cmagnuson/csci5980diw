package com.l33tsp33k.client.datamodels;

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

public class TechnoratiItemList implements IsSerializable {
	
	private ArrayList<TechnoratiItem> feedItemList;

	public TechnoratiItemList(){
	  feedItemList = new ArrayList<TechnoratiItem>();
	}

	public void addFeedItem(String name, String link, String author, String value) {
	  feedItemList.add(new TechnoratiItem(name, link, author, value));
	}

	public int getSize(){
	  return this.feedItemList.size();
	}
	
	public Iterator<TechnoratiItem> iterator() {
		return this.feedItemList.iterator();
	}

	public TechnoratiItem getFeedItem(int index){
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
