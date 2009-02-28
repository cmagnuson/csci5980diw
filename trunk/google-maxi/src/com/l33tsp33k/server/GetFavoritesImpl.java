package com.l33tsp33k.server;

import java.sql.*;
import java.util.*;

import com.l33tsp33k.client.GetFavorites;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.l33tsp33k.client.datamodels.*;

public class GetFavoritesImpl extends RemoteServiceServlet implements GetFavorites {

	public void addFlickrItem(FlickrPhoto p, String cookie){
		addItem(p, "flickr", cookie);
	}

	public void addTechnoratiItem(TechnoratiItem ti, String cookie){
		addItem(ti, "technorati", cookie);
	}

	public HashMap<Object,String> getFavorites(String cookie){
		HashMap<Object,String> favs = new HashMap<Object,String>();
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		try{
			PreparedStatement pstmt = conn.prepareStatement("SELECT object, object_type FROM user_favorites " +
			"WHERE cookie=?");
			pstmt.setString(1, cookie);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				favs.put(rs.getObject("object"), rs.getString("object_type"));
			}
			rs.close();
			conn.close();
			return favs;
		}
		catch(SQLException sql){
			sql.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return favs;
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
