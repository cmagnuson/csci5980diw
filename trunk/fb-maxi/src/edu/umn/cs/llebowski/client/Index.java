package edu.umn.cs.llebowski.client;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;
import com.google.gwt.user.client.ui.*;

public class Index implements EntryPoint {

	public static final String API_KEY = "d4dd44d81ce3c4076e4c55ae2329f13d";
	public static final String SECRET_KEY = "4803662fcf9e1bdfdc8c29d2adfe2bdd";

	private long uid = 1;
	private String sessionKey = "";
	private FacebookCredentials credentials = new FacebookCredentials();

	
	public void onModuleLoad() {
		getCredentials();
		RootPanel.get().add(new HTML(credentials.toString()));
	}
	
	private void getCredentials(){
		uid = Long.valueOf(com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_user"));
		sessionKey = com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_session_key");
		credentials.setApiKey(API_KEY);
		credentials.setSecretKey(SECRET_KEY);
		credentials.setSessionId(sessionKey);
		credentials.setUid(uid);
	}

}
