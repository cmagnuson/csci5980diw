package com.l33tsp33k.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.l33tsp33k.client.datamodels.*;

public interface GetFeedService extends RemoteService {

	FeedItemList getFeedItems(String tag);
	
	public static class App {
		private static GetFeedServiceAsync app = null;

		
		// get an instance of the feed service for client side use		
		public static synchronized GetFeedServiceAsync getInstance() {
			if (app == null) {
				
				// set up a client side asynchronous class that connects with 
				// the FeedTempService service we've defined in GwtMini.gwt.xml 
				
				app = (GetFeedServiceAsync) GWT.create(GetFeedService.class);
				((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "/FeedTempService");
			}
			return app;
		}
	}
}	
