package edu.umn.cs.llebowski.client;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;

public class Index implements EntryPoint {

	public static final String API_KEY = "d4dd44d81ce3c4076e4c55ae2329f13d";
	public static final String SECRET_KEY = "4803662fcf9e1bdfdc8c29d2adfe2bdd";

	private long uid = 1;
	private String sessionKey = "";
	private FacebookCredentials credentials = new FacebookCredentials();

	private VerticalPanel allPanel;
	private SimplePanel headerPanel;
	private HorizontalPanel mainPanel;
	private SimplePanel mapPanel;
	private SimplePanel navPanel;
	private SimplePanel footerPanel;
	
	private MapWidget mapWidget;
	private Panel navWidget;
	
	public void onModuleLoad() {
		getCredentials();
		
		// HEADER PANEL
		headerPanel = new SimplePanel();
		headerPanel.setWidth("960px");
		Image headerImage = new Image("images/header.png");
		headerPanel.add(headerImage);
		
		// MAP PANEL
		mapPanel = new SimplePanel();
		mapPanel.setWidth("660px");
		mapPanel.setHeight("400px");
		//mapPanel.add(new HTML("Map here"));
		mapPanel.add( getMap() );
		
		// NAV PANEL
		navPanel = new SimplePanel();
		navPanel.setWidth("300px");
		navPanel.setHeight("400px");
		//navPanel.add(new HTML("Nav here"));
		navPanel.add( getNav() );
		
		// MAIN PANEL
		mainPanel = new HorizontalPanel();
		mainPanel.setWidth("960px");
		mainPanel.setHeight("400px");
		mainPanel.add(mapPanel);
		mainPanel.add(navPanel);
		
		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.setWidth("960px");
		footerPanel.add(new HTML("Copyright &copy; 2009 Little Lebowski Urban Achievers"));
		
		// ALL PANEL
		allPanel = new VerticalPanel();
		allPanel.setWidth("960px");
		allPanel.add(headerPanel);
		allPanel.add(mainPanel);
		allPanel.add(footerPanel);
		
		RootPanel.get().add(allPanel);
	}
	
	private void getCredentials(){
		//uid = Long.valueOf(com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_user"));
		sessionKey = com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_session_key");
		credentials.setApiKey(API_KEY);
		credentials.setSecretKey(SECRET_KEY);
		credentials.setSessionId(sessionKey);
		credentials.setUid(uid);
	}
	
	private MapWidget getMap()
	{
		mapWidget = new MapWidget(LatLng.newInstance(38.548165,-95.361328), 3);  
		mapWidget.setSize("660px", "400px");  

		mapWidget.addControl(new SmallMapControl());  
		mapWidget.addControl(new MapTypeControl()); 

		return mapWidget;
	}
	
	private Panel getNav()
	{
		navWidget = new Panel("accordion");
		navWidget.setLayout(new AccordionLayout(true));
		navWidget.setSize("300px", "400px");
		
		Panel youPanel = new Panel("you", "hi you!");
		navWidget.add(youPanel);
		
		Panel friendsPanel = new Panel("friends", "hi friends!");
		navWidget.add(friendsPanel);
		
		Panel invitePanel = new Panel("invite", "hi invite!");
		navWidget.add(invitePanel);
		
		
		
		
		return navWidget;
	}
	

}
