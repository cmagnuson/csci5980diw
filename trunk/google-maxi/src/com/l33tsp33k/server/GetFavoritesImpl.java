package com.l33tsp33k.server;

import java.sql.*;
import java.util.*;

import com.l33tsp33k.client.GetFavorites;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l33tsp33k.client.datamodels.*;

public class GetFavoritesImpl extends RemoteServiceServlet implements GetFavorites {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2858630671954665924L;

	public void addFlickrItem(FlickrPhoto p, String cookie){
		addItem(p, "flickr", cookie);
	}

	public void addTechnoratiItem(TechnoratiItem ti, String cookie){
		addItem(ti, "technorati", cookie);
	}
	
	public FlickrPhoto[] getFavoritePhotos(String cookie){
		LinkedList<FlickrPhoto> photos = new LinkedList<FlickrPhoto>();
		Object[] os = getFavorites(cookie, "flickr");
		for(Object o: os){
			photos.add((FlickrPhoto)o);
			System.err.println("Photo requested");
		}
		return photos.toArray(new FlickrPhoto[]{});
	}
	
	public TechnoratiItem[] getFavoriteFeeds(String cookie){
		LinkedList<TechnoratiItem> photos = new LinkedList<TechnoratiItem>();
		Object[] os = getFavorites(cookie, "technorati");
		for(Object o: os){
			photos.add((TechnoratiItem)o);
		}
		return photos.toArray(new TechnoratiItem[]{});
	}
	
	public void addTwitterItem(TwitterItem ti, String cookie){
		addItem(ti, "twitter", cookie);
	}
	
	private Object[] getFavorites(String cookie, String type){
		System.err.println("Get favs called: "+type+"  "+cookie);
		LinkedList<Object> favs = new LinkedList<Object>();
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		try{
			PreparedStatement pstmt = conn.prepareStatement("SELECT object FROM user_favorites " +
			"WHERE cookie=? AND object_type=?");
			pstmt.setString(1, cookie);
			pstmt.setString(2, type);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				System.err.println("res gound");
				favs.add(rs.getObject("object"));
			}
			rs.close();
			conn.close();
			System.out.println("returning from get favcs");
			return favs.toArray();
		}
		catch(SQLException sql){
			sql.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return favs.toArray();
		}
	}

	private void addItem(Object o, String type, String cookie) {
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		try{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_favorites (cookie,object,object_type) VALUES (?,?,?)");
			pstmt.setString(1, cookie);
			pstmt.setObject(2, o);
			//pstmt.setBlob(2, new Blob(o));
			pstmt.setString(3, type);
			pstmt.executeUpdate();
			conn.close();
			return;
		}
		catch(SQLException sql){
			sql.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return;
		}
	}
}
