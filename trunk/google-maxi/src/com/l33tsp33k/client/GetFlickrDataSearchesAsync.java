package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFlickrDataSearchesAsync {

	void getCachedSearches(AsyncCallback<CachedSearchList> callback);

}
