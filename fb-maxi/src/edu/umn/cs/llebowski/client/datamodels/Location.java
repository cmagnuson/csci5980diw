package edu.umn.cs.llebowski.client.datamodels;

public class Location {

	private long lon, lat, time;
	
	public Location(long lon, long lat, long time){
		this.lon = lon;
		this.lat = lat;
		this.time = time;
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
