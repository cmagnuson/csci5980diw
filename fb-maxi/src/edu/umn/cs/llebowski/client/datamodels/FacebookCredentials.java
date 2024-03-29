package edu.umn.cs.llebowski.client.datamodels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FacebookCredentials implements IsSerializable {

	private String apiKey, sessionId, secretKey;
	private long uid;
	
	public String toString(){
		return "API: "+apiKey+"\n<br>"+
			"Session: "+sessionId+"\n<br>"+
			"Secret: "+secretKey+"\n<br>"+
			"Uid: "+uid+"\n<br>";
	}
	
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	
}
