package com.l33tsp33k.client;

import java.util.HashMap;

import com.l33tsp33k.client.datamodels.FlickrPhoto;
import com.l33tsp33k.client.datamodels.TechnoratiItem;
import com.l33tsp33k.client.datamodels.TwitterItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFavoritesAsync {

	void addTwitterItem(TwitterItem ti, String cookie, AsyncCallback<?> callback);
	void addTechnoratiItem(TechnoratiItem ti, String cookie, AsyncCallback<?> callback);
	void addFlickrItem(FlickrPhoto p, String cookie, AsyncCallback<?> callback);
	void getFavorites(String cookie, AsyncCallback<HashMap<Object, String>> callback);

}
