package com.l33tsp33k.server;

import java.sql.*;
import java.util.*;

import com.l33tsp33k.client.GetFavorites;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l33tsp33k.client.datamodels.*;

public class GetFavoritesImpl extends RemoteServiceServlet implements GetFavorites {

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
		}
		return photos.toArray(new FlickrPhoto[]{});
	}

	public TwitterItem[] getFavoriteTwitters(String cookie){
		LinkedList<TwitterItem> items = new LinkedList<TwitterItem>();
		Object[] os = getFavorites(cookie, "twitter");
		for(Object o: os){
			items.add((TwitterItem)o);
		}
		return items.toArray(new TwitterItem[]{});
	}

	public TechnoratiItem[] getFavoriteFeeds(String cookie){
		LinkedList<TechnoratiItem> photos = new LinkedList<TechnoratiItem>();
		Object[] os = getFavorites(cookie, "technorati");
		for(Object o: os){
			photos.add((TechnoratiItem)o);
		}
		return photos.toArray(new TechnoratiItem[]{});
	}
	
	public void removeItem(FlickrPhoto p, String cookie){
		removeItemP(p, cookie);
	}
	public void removeItem(TechnoratiItem i, String cookie){
		removeItemP(i, cookie);
	}
	public void removeItem(TwitterItem i, String cookie){
		removeItemP(i, cookie);
	}

	private void removeItemP(Object o, String cookie){
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		if(conn==null){
			return;
		}
		try{
			PreparedStatement pstmt = conn.prepareStatement("DELETE FROM user_favorites WHERE cookie=? AND object=?");
			pstmt.setString(1, cookie);
			pstmt.setObject(2, o.getClass().cast(o));
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

	public void addTwitterItem(TwitterItem ti, String cookie){
		addItem(ti, "twitter", cookie);
	}

	private Object[] getFavorites(String cookie, String type){
		LinkedList<Object> favs = new LinkedList<Object>();
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		if(conn==null){
			return new Object[]{};
		}
		try{
			PreparedStatement pstmt = conn.prepareStatement("SELECT object FROM user_favorites " +
			"WHERE cookie=? AND object_type=?");
			pstmt.setString(1, cookie);
			pstmt.setString(2, type);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				Object o = toObject((byte[])rs.getObject("object"));
				if(o!=null){
					favs.add(o);
				}
			}
			rs.close();
			pstmt.close();
			conn.close();
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

	public Object toObject(byte[] bytes){
		try{
			return new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes)).readObject();
		}
		catch(java.io.IOException ioe){
			return null;
		}
		catch(java.lang.ClassNotFoundException cnfe){
			return null;
		}
	}

	private void addItem(Object o, String type, String cookie) {
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		if(conn==null){
			return;
		}
		try{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user_favorites (cookie,object,object_type) VALUES (?,?,?)");
			pstmt.setString(1, cookie);
			pstmt.setObject(2, o.getClass().cast(o));
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
