package com.l33tsp33k.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.l33tsp33k.client.datamodels.*;
import java.util.*;

@RemoteServiceRelativePath("GetFavorites")
public interface GetFavorites extends RemoteService {

	void addTwitterItem(TwitterItem ti, String cookie);
	void addTechnoratiItem(TechnoratiItem ti, String cookie);
	void addFlickrItem(FlickrPhoto p, String cookie);
	HashMap<Object, String> getFavorites(String cookie);
	
	public static class Util {

		public static GetFavoritesAsync getInstance() {

			return GWT.create(GetFavorites.class);
		}
	}

}
