package com.l33tsp33k.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.l33tsp33k.client.datamodels.*;

public interface GetTechnoratiData extends RemoteService {

	TechnoratiItemList getFeedItems(String tag);
	
	public static class App {
		private static GetTechnoratiDataAsync app = null;

		
		// get an instance of the feed service for client side use		
		public static synchronized GetTechnoratiDataAsync getInstance() {
			if (app == null) {
				
				// set up a client side asynchronous class that connects with 
				// the FeedTempService service we've defined in GwtMini.gwt.xml 
				
				app = (GetTechnoratiDataAsync) GWT.create(GetTechnoratiData.class);
				((ServiceDefTarget) app).setServiceEntryPoint(GWT.getModuleBaseURL() + "/FeedTempService");
			}
			return app;
		}
	}
}	
