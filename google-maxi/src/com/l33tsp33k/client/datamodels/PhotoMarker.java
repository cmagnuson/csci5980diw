package com.l33tsp33k.client.datamodels;

public class PhotoMarker extends com.google.gwt.maps.client.overlay.Marker {

	private FlickrPhoto photo;
	
	public PhotoMarker(com.google.gwt.maps.client.geom.LatLng l, FlickrPhoto p){
		super(l);
		photo = p;
	}
	
	public FlickrPhoto getPhoto(){
		return photo;
	}
}
