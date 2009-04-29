package edu.umn.cs.llebowski.server;

import java.sql.*;
import java.util.*;
import net.sf.fb4j.FacebookException;
import net.sf.fb4j.FacebookSession;
import net.sf.fb4j.model.*;
import edu.umn.cs.llebowski.client.InsertLocations;
import edu.umn.cs.llebowski.client.datamodels.FacebookCredentials;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class InsertLocationsImpl extends RemoteServiceServlet implements InsertLocations {

	public LinkedList<Alert> insertUserAddedLocation(String location, FacebookCredentials credentials){
		java.util.Date time = Calendar.getInstance().getTime();
		LinkedList<Alert> ret = new LinkedList<Alert>();
		Location l = AddressToCord.convertToCord(location);
		System.err.println("checking loc");
		if(l.getAccuracy()<7) {//less then intersection accuracy
			ret.add(new BadLocationAlert());
		}
		else{
			ret.addAll(insertLocation(l, location, time, credentials, null));
		}		
		return ret;
	}

	public LinkedList<Alert> insertEventLocations(FacebookCredentials credentials){
		LinkedList<Alert> ret = new LinkedList<Alert>();
		for(Event e: getEvents(credentials)){
			//TODO:  this should check somewhere that this event isn't already in the db
			//OR just fall back on insert on DUPLICATE ignore... just more costly
			EventLocation el = e.getVenue();
			if(e.getLocation()==null || el.getLatitude()==null || el.getLongitude()==null) {//less then intersection accuracy
				//bad address, but will not report an error
				continue;
			}
			else{
				Location l = new Location(200,9, el.getLatitude(), el.getLongitude());
				ret.addAll(insertLocation(l, e.getLocation(), e.getStartTime(), credentials, e.getId()));
			}			
		}
		return ret;
	}


	private LinkedList<Alert> insertLocation(Location l, String address, java.util.Date time, FacebookCredentials credentials, Long eventid){
		LinkedList<Alert> ret = new LinkedList<Alert>();;
		long uid = credentials.getUid();
		Connection conn = InitalizeDB.connectToMySqlDatabase("c.onetendev.com:8306/google-maxi", "5980-groupf", "lebowskiSEKKRIT55");
		if(conn==null){
			return ret;
		}
		try{
			System.err.println("preparing stmt");
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO places (uid, lon, lat, place, time, eventid) VALUES " +
			"(?,?,?,?,TIMESTAMP(?),?)");
			pstmt.setLong(1, uid);
			pstmt.setDouble(2, l.getLon());
			pstmt.setDouble(3, l.getLat());
			pstmt.setString(4, address);
			pstmt.setTimestamp(5, new Timestamp(time.getTime()));
			if(eventid==null){
				pstmt.setString(6, null);
			}
			else{
				pstmt.setLong(6, eventid);
			}
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

	private LinkedList<Event> getEvents(FacebookCredentials c){
		FacebookSession fs = new FacebookSession(c.getApiKey(), c.getSecretKey(), c.getSessionId(), c.getUid()); 
		LinkedList<Event> ret =  new LinkedList<Event>();
		try{
			for(Event e: fs.getEvents()){
				ret.add(e);
			}
			return ret;
		}
		catch(FacebookException fe){
			fe.printStackTrace();
			return ret;
		}
	}

}
