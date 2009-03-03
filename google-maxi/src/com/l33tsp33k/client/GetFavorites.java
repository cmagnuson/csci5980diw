package com.l33tsp33k.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.l33tsp33k.client.datamodels.FlickrPhoto;
import com.l33tsp33k.client.datamodels.TechnoratiItem;
import com.l33tsp33k.client.datamodels.TwitterItem;

@RemoteServiceRelativePath("GetFavorites")
public interface GetFavorites extends RemoteService {

	void addTwitterItem(TwitterItem ti, String cookie);
	void addTechnoratiItem(TechnoratiItem ti, String cookie);
	void addFlickrItem(FlickrPhoto p, String cookie);
	void removeItem(FlickrPhoto p, String cookie);
	void removeItem(TechnoratiItem p, String cookie);
	void removeItem(TwitterItem p, String cookie);
	FlickrPhoto[] getFavoritePhotos(String cookie);
	TechnoratiItem[] getFavoriteFeeds(String cookie);
	TwitterItem[] getFavoriteTwitters(String cookie);
	
	public static class Util {

		public static GetFavoritesAsync getInstance() {

			return GWT.create(GetFavorites.class);
		}
	}

}
