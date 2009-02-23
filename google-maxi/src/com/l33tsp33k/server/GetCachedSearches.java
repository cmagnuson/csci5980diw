package com.l33tsp33k.server;

import com.l33tsp33k.client.datamodels.*;

import java.sql.*;

/**
 * Servlet implementation class for Servlet: GetCachedSearches
 *
 */
public class GetCachedSearches extends com.google.gwt.user.server.rpc.RemoteServiceServlet implements javax.servlet.Servlet, com.l33tsp33k.client.GetCachedSearches {
	static final long serialVersionUID = 1L;


	public CachedSearchList getCachedSearches() {
		CachedSearchList searches = new CachedSearchList();
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		try{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT search_term FROM cached_searches");
			while(rs.next()){
				searches.addSearchItem(rs.getString("search_term"));
			}
			rs.close();
			stmt.close();
			conn.close();
			return searches;
		}
		catch(SQLException sql){
			sql.printStackTrace();
			try{conn.close();}
			catch(SQLException s){
				s.printStackTrace();
				conn = null;
			}
			return searches;
		}
	}

	public void addCachedSearch(String tag) {
		Connection conn = InitalizeDB.connectToMySqlDatabase("championchipmn.com/google-maxi", "5980-group", "lebowski");
		try{
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO cached_searches (search_term) VALUES (?) " +
					"ON DUPLICATE KEY UPDATE search_time=NOW()");
			pstmt.setString(1, tag);
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