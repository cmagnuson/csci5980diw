package com.l33tsp33k.client.datamodels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FlickrPhoto implements IsSerializable, GeoReferencedData, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3265280095557909969L;
	private float lon, lat;
	private String title;
	private String url;
	private String thumbnailUrl;
	private boolean geoReferenced = false;
	
	public String toString(){
		return title+" "+url+" "+lon+","+lat;
	}
	
	public void setCoordinates(float lon, float lat) {
		this.lon = lon;
		this.lat = lat;
		geoReferenced = true;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public boolean hasLocation(){
		return geoReferenced;
	}
	public boolean hasCoordinates(){
		return geoReferenced;
	}
	public String getLocation(){
		if(!geoReferenced){
			return "";
		}
		else{
			return ""+lon+","+lat;
		}
	}
	public double getLat(){
		return lat;
	}
	public double getLong(){
		return lon;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
}
