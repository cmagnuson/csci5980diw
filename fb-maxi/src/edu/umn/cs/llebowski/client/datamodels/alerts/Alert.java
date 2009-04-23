package edu.umn.cs.llebowski.client.datamodels.alerts;

public interface Alert {

	abstract public String getAlertText();
	abstract public AlertType getAlertType();
	
}
