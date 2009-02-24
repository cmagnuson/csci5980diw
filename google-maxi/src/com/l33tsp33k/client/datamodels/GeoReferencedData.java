package com.l33tsp33k.client.datamodels;

public interface GeoReferencedData {

	public String getLocation();
	public boolean hasLocation();
	
	public boolean hasCoordinates();
	public double getLat();
	public double getLong();
}
