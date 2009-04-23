package edu.umn.cs.llebowski.client.datamodels.alerts;

public class BadLocationAlert implements Alert {

	public String getAlertText() {
		return "I dunno what to put here...";
	}

	public AlertType getAlertType() {
		return AlertType.NEAR_FRIEND;
	}

}
