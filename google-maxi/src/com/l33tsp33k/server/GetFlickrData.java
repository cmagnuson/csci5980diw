package com.l33tsp33k.server;

import javax.xml.parsers.ParserConfigurationException;

import com.l33tsp33k.client.datamodels.*;
import com.aetrion.flickr.*;
import com.aetrion.flickr.photos.*;

/**
 * Servlet implementation class for Servlet: GetFlickrData
 *
 */
public class GetFlickrData extends com.google.gwt.user.server.rpc.RemoteServiceServlet implements javax.servlet.Servlet, com.l33tsp33k.client.GetFlickrData {
	static final long serialVersionUID = 1L;
	static final String API_KEY = "6799581c2e4e36d34ec4056711f5a644";
	static final String SECRET = "60e44a7c30dbfaef";

	public FlickrPhotoList getFlickrPhotos(String tag){
		FlickrPhotoList fpl = new FlickrPhotoList();
		try{
			Flickr f = new Flickr(API_KEY, SECRET, new REST());
			PhotosInterface pi = f.getPhotosInterface();
			SearchParameters sp = new SearchParameters();
			sp.setTags(new String[] {tag});
			PhotoList pl = pi.search(sp, 500, 1);
			for(Object o: pl){
				Photo p = (Photo) o;
				FlickrPhoto fp = new FlickrPhoto();
				fp.setUrl(p.getSmallUrl());
				fp.setTitle(p.getTitle());
				if(p.getGeoData()!=null){
					fp.setCoordinates(p.getGeoData().getLongitude(), p.getGeoData().getLatitude());
				}
				fpl.addPhoto(fp);
			}
			return fpl;
		}
		catch(Exception e){
			e.printStackTrace();
			return fpl;
		}
	}

}