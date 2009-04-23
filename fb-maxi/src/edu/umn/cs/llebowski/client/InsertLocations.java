package edu.umn.cs.llebowski.client;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import java.util.*;

@RemoteServiceRelativePath("InsertLocations")
public interface InsertLocations extends RemoteService {

	LinkedList<Alert> insertUserAddedLocation(String location, Date time, String requestAttribute);
	
	public static class Util {
		

		public static InsertLocationsAsync getInstance() {

			return GWT.create(InsertLocations.class);
		}
	}

}
