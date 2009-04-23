package edu.umn.cs.llebowski.client;

import edu.umn.cs.llebowski.client.datamodels.*;
import java.util.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFriendsAsync {

	void getFriends(String requestAttribute, AsyncCallback<LinkedList<Person>> callback);

}
