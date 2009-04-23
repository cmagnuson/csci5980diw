package edu.umn.cs.llebowski.client;

import java.util.LinkedList;

import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import java.util.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InsertLocationsAsync {

	void insertUserAddedLocation(String location, Date time, String requestAttribute, AsyncCallback<LinkedList<Alert>> callback);

}
