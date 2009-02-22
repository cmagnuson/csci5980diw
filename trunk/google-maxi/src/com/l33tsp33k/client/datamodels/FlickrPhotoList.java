package com.l33tsp33k.client.datamodels;

import java.util.ArrayList;
import java.util.Iterator;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FlickrPhotoList implements IsSerializable {
		
		private ArrayList<FlickrPhoto> photoList;

		public FlickrPhotoList(){
		  photoList = new ArrayList<FlickrPhoto>();
		}

		public void addPhoto(FlickrPhoto photo) {
		  photoList.add(photo);
		}

		public int getSize(){
		  return this.photoList.size();
		}
		
		public Iterator<FlickrPhoto> iterator() {
			return this.photoList.iterator();
		}

		public FlickrPhoto getPhoto(int index){
		  return this.photoList.get(index); 
		}

		public String toString(){
		  String retval;

		  retval = "list.size="+this.getSize()+"\n";

		  for(int i=0; i<getSize(); i++){
		    retval += this.getPhoto(i) + "\n";
		  }

		  return retval;
		}
	}