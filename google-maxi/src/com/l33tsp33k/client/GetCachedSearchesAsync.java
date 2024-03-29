package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetCachedSearchesAsync {

	void getCachedSearches(AsyncCallback<CachedSearchList> callback);
	
	void addCachedSearch(String tag, AsyncCallback<?> callback);

}
