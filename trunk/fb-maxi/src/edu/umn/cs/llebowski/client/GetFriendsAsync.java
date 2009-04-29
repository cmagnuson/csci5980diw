package edu.umn.cs.llebowski.client;

import edu.umn.cs.llebowski.client.datamodels.*;
import java.util.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFriendsAsync {

	void getFriends(FacebookCredentials credentials, AsyncCallback<LinkedList<Person>> callback);
	void getAllFriends(FacebookCredentials credentials, AsyncCallback<LinkedList<Person>> callback);

}
