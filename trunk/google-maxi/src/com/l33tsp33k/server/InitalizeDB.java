package com.l33tsp33k.server;

import java.sql.*;

public class InitalizeDB {	

	static public Connection connectToMySqlDatabase(String dbLocation, String userName, String password){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://"+dbLocation;
			Connection con = DriverManager.getConnection(url, userName, password);
			if(con.getWarnings()!=null){
				System.out.println(con.getWarnings());
			}
			return con;
		}
		catch(SQLException sql) {
			sql.printStackTrace();
			return null;
		}
		catch(ClassNotFoundException c){
			c.printStackTrace();
			System.err.println("MISSING MYSQL DRIVER CLASS!");
			System.exit(-1);
			return null;
		}
	}

}
