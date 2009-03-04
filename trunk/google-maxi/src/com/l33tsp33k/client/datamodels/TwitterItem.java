package com.l33tsp33k.client.datamodels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TwitterItem implements IsSerializable, java.io.Serializable, GeoReferencedData {

	private static final long serialVersionUID = 6992873494030300174L;
	public String name = "";
	public String link = "";
	public String author = "";
	public String descr = "";
	private boolean coordinatesAssigned = false;
	private boolean locationAssigned = false;
	private String location = "";
	private double lat = 0;
	private double lon = 0;

	public boolean hasCoordinates() {
		return coordinatesAssigned;
	}


	public boolean hasLocation() {
		return locationAssigned;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
		locationAssigned = true;
	}

	public double getLat() {
		return lat;
	}

	public double getLong() {
		return lon;
	}

	public void setCoords(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
		coordinatesAssigned = true;
	}

	public TwitterItem() {}

	public TwitterItem(String name, String link, String author, String descr) {
		this.name = name;
		this.link = link;
		this.author = author;
		this.descr = descr;
	}

	public String getUsername() {
		return author.split(" ")[0];
	}

	public String toString() {
		return "name=" + name + ", link=" + link + ", author=" + author + ", descr=" + descr;
	}
}
