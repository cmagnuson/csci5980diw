package com.l33tsp33k.client;

import java.util.ArrayList;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetTwitterDataAsync {

	void getFeedItems(String tag, AsyncCallback<ArrayList<TwitterItem>> callback);
	void getGeoreferencedFeedItems(ArrayList<TwitterItem> items, AsyncCallback<ArrayList<TwitterItem>> callback);
}	
