package com.l33tsp33k.server;

public class Location {

	double lon = 0;
	double lat = 0;
	double accuracy = 0;
	int statusCode = 0;
	
	Location(){}
	
	Location(int statusCode, double accuracy, double lat, double  lon){
		this.statusCode = statusCode;
		this.accuracy = accuracy;
		this.lat = lat;
		this.lon = lon;
	}
	
	public String toString(){
		return ""+statusCode+", "+accuracy+", "+lat+", "+lon;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
