package edu.umn.cs.llebowski.server;

import edu.umn.cs.llebowski.client.GetFriends;

import java.sql.*;
import java.util.*;
import edu.umn.cs.llebowski.client.datamodels.*;
import net.sf.fb4j.*;
import net.sf.fb4j.client.*;
import net.sf.fb4j.model.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class GetFriendsImpl extends RemoteServiceServlet implements GetFriends {

	public LinkedList<Person> getFriends(FacebookCredentials credentials){
		LinkedList<Person> ret = new LinkedList<Person>();

		Connection conn = InitalizeDB.connectToMySqlDatabase("c.onetendev.com:8307/google-maxi", "5980-groupf", "lebowskiSEKKRIT55");
		if(conn==null){
			return ret;
		}
		try{
			FacebookSession fs = getSession(credentials);
			long[] appFriends = fs.getAppUserFriendIds();
			for(long friendid: appFriends){
				ret.add(getFriendByUid(friendid, conn, fs));
			}
			ret.add(getFriendByUid(credentials.getUid(), conn, fs));
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
		catch(FacebookClientException fce){
			fce.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return ret;
		}
	}

	private Person getFriendByUid(long friendid, Connection conn, FacebookSession fs) throws SQLException, FacebookClientException {
		LinkedList<PersonLocation> places = new LinkedList<PersonLocation>();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM places WHERE uid=? ORDER BY time DESC");
		pstmt.setLong(1, friendid);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			PersonLocation pl = new PersonLocation(rs.getDouble("lon"), rs.getDouble("lat"), rs.getTime("time").getTime(), rs.getString("place"));
			places.add(pl);
		}
		UserInfo ui = fs.getUserInfo(friendid, UserInfo.Field.PIC_SMALL, UserInfo.Field.NAME, UserInfo.Field.UID);
		Person p = new Person(ui.getId(), ui.getPicSmall(), ui.getName(), places);
		return p;
	}
	
	public LinkedList<Person> getAllFriends(FacebookCredentials credentials){
		LinkedList<Person> ret = new LinkedList<Person>();
		try{
			FacebookSession fs = getSession(credentials);
			long[] friends = fs.getFriendIds();
			for(long friend: friends){
				UserInfo ui = fs.getUserInfo(friend, UserInfo.Field.PIC_SMALL, UserInfo.Field.NAME, UserInfo.Field.UID);
				Person p = new Person(ui.getId(), ui.getPicSmall(), ui.getName(), new LinkedList<PersonLocation>());
				ret.add(p);
			}
			return ret;
		}
		catch(FacebookClientException fce){
			fce.printStackTrace();
			return ret;
		}
	}
	
	public FacebookSession getSession(FacebookCredentials c){
		return new FacebookSession(c.getApiKey(), c.getSecretKey(), c.getSessionId(), c.getUid());
	}
}
