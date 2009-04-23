package edu.umn.cs.llebowski.server;

import java.sql.Connection;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;
import net.sf.fb4j.model.*;
import edu.umn.cs.llebowski.client.InsertLocations;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class InsertLocationsImpl extends RemoteServiceServlet implements InsertLocations {

	public LinkedList<Alert> insertUserAddedLocation(String location, Date time, String requestAttribute){
		LinkedList<Alert> ret = new LinkedList<Alert>();
		Location l = AddressToCord.convertToCord(location);
		if(l.getAccuracy()<7) {//less then intersection accuracy
			ret.add(new BadLocationAlert());
		}
		else{
			ret.addAll(insertLocation(l, location, time, requestAttribute, null));
		}		
		return ret;
	}

	public LinkedList<Alert> insertEventLocations(String requestAttribute){
		LinkedList<Alert> ret = new LinkedList<Alert>();
		for(Event e: getEvents(requestAttribute)){
			//TODO:  this should check somewhere that this event isn't already in the db
			//OR just fall back on insert on DUPLICATE ignore... just more costly
			EventLocation el = e.getVenue();
			if(e.getLocation()==null || el.getLatitude()==null || el.getLongitude()==null) {//less then intersection accuracy
				//bad address, but will not report an error
				continue;
			}
			else{
				Location l = new Location(200,9, el.getLatitude(), el.getLongitude());
				ret.addAll(insertLocation(l, e.getLocation(), e.getStartTime(), requestAttribute, e.getId()));
			}			
		}
		return ret;
	}


	private LinkedList<Alert> insertLocation(Location l, String address, Date time, String requestAttribute, Long eventid){
		//this needs to look up the uid from the requestAttribute... not sure how to work with fb4j for this
		LinkedList<Alert> ret = new LinkedList<Alert>();;
		int uid = 12345; //TODO: this needs to be pulled from fb
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-groupf", "lebowskiSEKKRIT55");
		if(conn==null){
			return ret;
		}
		try{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO places (uid, lon, lat, place, time, eventid) VALUES " +
			"(?,?,?,?,?, ?) ON DUPLICATE KEY UPDATE");
			pstmt.setLong(1, uid);
			pstmt.setDouble(2, l.getLon());
			pstmt.setDouble(3, l.getLat());
			pstmt.setString(4, address);
			pstmt.setTime(5, new Time(time.getTime()));
			pstmt.setLong(6, eventid);
			pstmt.executeUpdate();
			return ret;
		}
		catch(SQLException sql){
			sql.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return ret;
		}
	}

	private LinkedList<Event> getEvents(String requestAttribute){
		return new LinkedList<Event>(); //TODO:  pull this from users fb events
	}

}
