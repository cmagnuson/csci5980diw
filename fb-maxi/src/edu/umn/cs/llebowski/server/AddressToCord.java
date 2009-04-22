package edu.umn.cs.llebowski.server;

import java.net.*;
import java.io.*;

public class AddressToCord {
	
	final static String googleId = "ABQIAAAAiuPVg850W_6nz_Xq0YZbBhTMfOSgnTFSBJHlxt2tIHyOtUx0YRQHMJIX6DCPqOg61WX8TQwjzYRj1A";
	final static String dataType = "csv";
	
	@SuppressWarnings("deprecation")
	public static Location convertToCord(String address){
		Location ret = null;;
		String input = null;
		try{
			String htmlAddress = URLEncoder.encode(address);
			URL u2 = new URL("http://maps.google.com/maps/geo?q="+htmlAddress+"&output="+dataType+"&key="+googleId);
			HttpURLConnection u = (HttpURLConnection)u2.openConnection();
			u.addRequestProperty("User-Agent","Mozilla/4.76");

			BufferedReader br;
			try{br = new BufferedReader(new InputStreamReader(u.getInputStream()));}
			catch(IOException e){
				return null;
			}
			input = br.readLine();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ret = new Location(Integer.valueOf(input.split(",")[0]),Double.valueOf(input.split(",")[1]),Double.valueOf(input.split(",")[2]),Double.valueOf(input.split(",")[3]));
		return ret;
	}
}
