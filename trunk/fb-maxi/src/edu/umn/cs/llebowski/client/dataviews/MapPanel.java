package edu.umn.cs.llebowski.client.dataviews;

import java.util.LinkedList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.maps.client.*;
import com.google.gwt.maps.client.control.*;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.Polyline;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.geom.LatLng;

import edu.umn.cs.llebowski.client.datamodels.*;

public class MapPanel extends SimplePanel {

	private final MapWidget mapWidget = new MapWidget();
	
	private static final int REFRESH_RATE = 15*1000;
	private static final String LINE_COLOR = "#52FF52";
	private static final int LINE_WIDTH = 4;
	private static final double LINE_OPACITY = 1;
	
	public MapPanel(){
		super();
		final MapPanel mp = this;
		this.setTitle("Friend Map");
		this.setSize("100%", "100%");

		mapWidget.setCenter(LatLng.newInstance(38.548165,-95.361328), 3);  
		mapWidget.addControl(new SmallMapControl());  
		mapWidget.addControl(new MapTypeControl()); 
		mapWidget.setSize("100%", "100%");
		this.add(mapWidget);

		//set zoom based off of some user presets....
		//mapWidget.setCenter(LatLng.newInstance(map.getCenter_lat(), map.getCenter_lon()), map.getZoom());  

		//mapWidget.addControl(new SmallMapControl(), new ControlPosition(ControlAnchor.TOP_RIGHT, 10, 10));  
		mapWidget.checkResizeAndCenter();	

		//mp.scheduleTimer();	
	}

	public void setHeight(int height){
		this.setHeight(height);
		mapWidget.setHeight(height+"px");
	}

//	private void scheduleTimer(){
//		Timer t = new Timer() {
//			public void run() {
//				refreshMap();
//			}
//		};
//		t.scheduleRepeating(REFRESH_RATE);
//	}



	public void onLoad(){
		super.onLoad();
		checkResizeAndCenter();	
	}

	public void onAttach(){
		super.onAttach();
		checkResizeAndCenter();	
	}

//	public void refreshMap(){
//		//LinkedList<Split> splits = mtrack.getSplits(runnerPid);
////		if(splits==null){
////		splits = new LinkedList<Split>();
////		}
//		drawMap();
//	}

	public void checkResizeAndCenter(){
		if(mapWidget!=null){
			mapWidget.checkResizeAndCenter();
		}
	}

//	private void drawMap(){
//		//mapWidget.clearOverlays(); //or just check which ones need to be redrawn
//
//		//do drawing stuff
//	}

	public void addPersonPoint(Person p, PersonLocation l){
		String infoText = "";
		MarkerOptions options = MarkerOptions.newInstance();
		options.setTitle(infoText.replaceAll("<br>", ""));

		final Marker sm = new Marker(LatLng.newInstance(l.getLat(),l.getLon()), options);
		mapWidget.addOverlay(sm);

		String imageUrl = p.getProfilePic();
		sm.setImage(imageUrl);

		final String infoTextf = infoText;

		sm.addMarkerClickHandler(new MarkerClickHandler() {
			public void	onClick(MarkerClickHandler.MarkerClickEvent event){
				InfoWindow iw = mapWidget.getInfoWindow();
				iw.open(sm, new InfoWindowContent(infoTextf));
			}
		});
	}

	public void addPersonTrack(Person p){
		LinkedList<LatLng> points = new LinkedList<LatLng>();

		for(PersonLocation l: p.getLocations()){
			points.add(LatLng.newInstance(l.getLat(), l.getLon()));
			addPersonPoint(p,l);
		}

		Polyline line = new Polyline(points.toArray(new LatLng[]{}), LINE_COLOR, LINE_WIDTH, LINE_OPACITY);
		mapWidget.addOverlay(line);
	}

}
