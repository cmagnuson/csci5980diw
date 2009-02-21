package com.l33tsp33k.client.datamodels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FlickrPhoto implements IsSerializable, GeoReferencedData {

	public boolean hasLocation(){
		return false;
	}
	public String getLocation(){
		return "";
	}
}
