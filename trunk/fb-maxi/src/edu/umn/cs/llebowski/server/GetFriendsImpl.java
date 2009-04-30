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
			Person me = getFriendByUid(credentials.getUid(), conn, fs);
			ret.add(me);
			PersonLocation myLoc = me.getLocations().getFirst();
			for(Person p: ret){
				PersonLocation pl = p.getLocations().getFirst();
				p.setDistance(Math.sqrt(Math.pow(myLoc.getLat()-pl.getLat(), 2) + Math.pow(myLoc.getLon()-pl.getLon(), 2))/.01824);
			}
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
			PersonLocation pl = new PersonLocation(rs.getDouble("lon"), rs.getDouble("lat"), rs.getTimestamp("time").getTime(), rs.getString("place"));
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
				try{
					UserInfo ui = fs.getUserInfo(friend, UserInfo.Field.PIC_SMALL, UserInfo.Field.NAME, UserInfo.Field.UID);
					Person p = new Person(ui.getId(), ui.getPicSmall(), ui.getName(), new LinkedList<PersonLocation>());
					ret.add(p);
				}
				catch(NullPointerException npe){
					continue;
				}
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

	public Boolean sendInvite(Person p, FacebookCredentials credentials){
		try{
			FacebookSession fs = getSession(credentials);
			fs.sendNotification(p.getName()+" you should check out this really cool app! - http://localhost:8888/5980_Facebook_Maxi/login.html", p.getFbUid());
			return true;
		}
		catch(FacebookClientException fce){
			fce.printStackTrace();
			return false;
		}
	}
}