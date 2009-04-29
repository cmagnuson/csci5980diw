package edu.umn.cs.llebowski.client.datamodels.alerts;

import com.google.gwt.user.client.rpc.IsSerializable;


public class NearbyFriendAlert implements Alert, IsSerializable {

	public NearbyFriendAlert(){};
	
	public String getAlertText() {
		return "You are near a friend...";
	}

	public AlertType getAlertType() {
		return AlertType.NEAR_FRIEND;
	}

}
