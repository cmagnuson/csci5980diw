package edu.umn.cs.llebowski.client.datamodels;

import java.util.*;
import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Person implements IsSerializable {

	private long fbUid = 1;
	private String profilePic = "";
	private String name = "";
	private double distance = 0;
	private LinkedList<PersonLocation> locations = new LinkedList<PersonLocation>(); //list of where has been in chronologically descending order
	
	public Person(){} //No args constructor is needed for GWT serialization
	
	public Person(long fbUid, String profilePic, String name, LinkedList<PersonLocation> locations) {
		this.fbUid = fbUid;
		this.profilePic = profilePic;
		this.name = name;
		this.locations = locations;
	}

	
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public long getFbUid() {
		return fbUid;
	}

	public void setFbUid(long fbUid) {
		this.fbUid = fbUid;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<PersonLocation> getLocations() {
		return locations;
	}

	public void setLocations(LinkedList<PersonLocation> locations) {
		this.locations = locations;
	}

	
	
}
