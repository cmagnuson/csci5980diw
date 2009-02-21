package com.l33tsp33k.client.datamodels;

import java.util.ArrayList;
import java.util.Iterator;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CachedSearchList implements IsSerializable {
	
	private ArrayList<String> searchList;

	public CachedSearchList(){
	  searchList = new ArrayList<String>();
	}

	public void addSearchItem(String searchTerm) {
	  searchList.add(searchTerm);
	}

	public int getSize(){
	  return this.searchList.size();
	}
	
	public Iterator<String> iterator() {
		return this.searchList.iterator();
	}

	public String getSearchItem(int index){
	  return this.searchList.get(index); 
	}

	public String toString(){
	  String retval;

	  retval = "list.size="+this.getSize()+"\n";

	  for(int i=0; i<getSize(); i++){
	    retval += this.getSearchItem(i) + "\n";
	  }

	  return retval;
	}
}