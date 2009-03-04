package com.l33tsp33k.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.l33tsp33k.client.datamodels.*;

public interface GetTwitterData extends RemoteService {

	ArrayList<TwitterItem> getFeedItems(String tag);
	ArrayList<TwitterItem> getGeoreferencedFeedItems(ArrayList<TwitterItem> items);
	
	public static class App {
		private static GetTwitterDataAsync app = null;

		
		// get an instance of the feed service for client side use		
		public static synchronized GetTwitterDataAsync getInstance() {
			if (app == null) {
				
				// set up a client side asynchronous class that connects with 
				// the FeedTempService service we've defined in GwtMini.gwt.xml 
				
				app = (GetTwitterDataAsync) GWT.create(GetTwitterData.class);
				((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "/GetTwitterData");
			}
			return app;
		}
	}
}	
