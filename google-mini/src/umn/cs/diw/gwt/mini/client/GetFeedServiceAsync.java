package umn.cs.diw.gwt.mini.client;

import umn.cs.diw.gwt.mini.client.datamodel.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFeedServiceAsync {
	  void getFeedItems(String tag, AsyncCallback sync);


}	
