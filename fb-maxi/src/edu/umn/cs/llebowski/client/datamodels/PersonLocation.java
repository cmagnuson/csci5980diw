package edu.umn.cs.llebowski.client.datamodels;

import com.google.gwt.user.client.rpc.*;

public class PersonLocation implements IsSerializable {

	private long lon, lat, time;
	private String address;
	
	public PersonLocation(){} //empty constuctor for serialization
	
	public PersonLocation(long lon, long lat, long time, String address){
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



	public long getLon() {
		return lon;
	}

	public void setLon(long lon) {
		this.lon = lon;
	}

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	
}
