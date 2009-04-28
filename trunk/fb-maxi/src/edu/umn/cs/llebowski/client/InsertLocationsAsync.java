package edu.umn.cs.llebowski.client;

import java.util.LinkedList;

import edu.umn.cs.llebowski.client.datamodels.FacebookCredentials;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import java.util.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsertLocationsAsync {

	void insertUserAddedLocation(String location, FacebookCredentials credentials, AsyncCallback<LinkedList<Alert>> callback);
	void insertEventLocations(FacebookCredentials credentials, AsyncCallback<LinkedList<Alert>> callback);

}
