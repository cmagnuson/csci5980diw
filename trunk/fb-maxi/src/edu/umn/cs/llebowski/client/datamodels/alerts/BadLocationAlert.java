package edu.umn.cs.llebowski.client.datamodels.alerts;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BadLocationAlert implements Alert, IsSerializable {

	public BadLocationAlert(){}
	
	public String getAlertText() {
		return "I dunno what to put here...";
	}

	public AlertType getAlertType() {
		return AlertType.NEAR_FRIEND;
	}

}
