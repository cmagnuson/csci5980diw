package edu.umn.cs.llebowski.client.datamodels;

import com.google.gwt.user.client.rpc.*;

public class PersonLocation implements IsSerializable {

	private double lon, lat;
	private long time;
	private String address;
	
	public PersonLocation(){} //empty constuctor for serialization
	
	public PersonLocation(double lon, double lat, long time, String address){
		this.lon = lon;
		this.lat = lat;
		this.time = time;
		this.address = address;
	}
	
	

	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
