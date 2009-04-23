package edu.umn.cs.llebowski.server;

import java.sql.Connection;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

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
			insertLocation(l, location, time, requestAttribute);
		}		
		return ret;
	}

	
	private LinkedList<Alert> insertLocation(Location l, String address, Date time, String requestAttribute){
		//this needs to look up the uid from the requestAttribute... not sure how to work with fb4j for this
		LinkedList<Alert> ret = new LinkedList<Alert>();;
		int uid = 12345; //TODO: this needs to be pulled from fb
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-groupf", "lebowskiSEKKRIT55");
		if(conn==null){
			return ret;
		}
		try{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO places (uid, lon, lat, place, time) VALUES " +
					"(?,?,?,?,?)");
			pstmt.setLong(1, uid);
			pstmt.setDouble(2, l.getLon());
			pstmt.setDouble(3, l.getLat());
			pstmt.setString(4, address);
			pstmt.setTime(5, new Time(time.getTime()));
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


}
