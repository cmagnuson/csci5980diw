package com.l33tsp33k.client.datamodels;

public class TwitterMarker extends com.google.gwt.maps.client.overlay.Marker {

	private TwitterItem tweet;
	
	public TwitterMarker(com.google.gwt.maps.client.geom.LatLng l, TwitterItem t){
		super(l);
		tweet = t;
	}
	
	public String getDescr(){
		return tweet.descr;
	}
	public String getLink(){
		return tweet.link;
	}
	public String getName(){
		return tweet.name;
	}
	
}
