package edu.umn.cs.llebowski.client.datamodels;

import java.util.*;

public class Person {

	private long fbUid;
	private String profilePic;
	private String name;
	private LinkedList<Location> locations; //list of where has been in chronologically descending order
	
	public Person(long fbUid, String profilePic, String name, LinkedList<Location> locations) {
		this.fbUid = fbUid;
		this.profilePic = profilePic;
		this.name = name;
		this.locations = locations;
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

	public LinkedList<Location> getLocations() {
		return locations;
	}

	public void setLocations(LinkedList<Location> locations) {
		this.locations = locations;
	}

	
	
}
