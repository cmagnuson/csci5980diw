package edu.umn.cs.llebowski.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import edu.umn.cs.llebowski.client.datamodels.*;
import java.util.*;

@RemoteServiceRelativePath("GetFriends")
public interface GetFriends extends RemoteService {

	LinkedList<Person> getFriends(FacebookCredentials credentials);
	LinkedList<Person> getAllFriends(FacebookCredentials credentials);
	Boolean sendInvite(Person p, FacebookCredentials credentials);
	
	public static class Util {

		public static GetFriendsAsync getInstance() {

			return GWT.create(GetFriends.class);
		}
	}

}
