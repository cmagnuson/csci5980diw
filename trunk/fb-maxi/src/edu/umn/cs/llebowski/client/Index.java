package edu.umn.cs.llebowski.client;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;
import com.google.gwt.user.client.*;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;
import edu.umn.cs.llebowski.client.dataviews.*;

public class Index implements EntryPoint, WindowResizeListener {

	public static final String API_KEY = "d4dd44d81ce3c4076e4c55ae2329f13d";
	public static final String SECRET_KEY = "4803662fcf9e1bdfdc8c29d2adfe2bdd";

	private long uid = 1;
	private String sessionKey = "";
	private FacebookCredentials credentials = new FacebookCredentials();

	private SimplePanel headerPanel;
	private MapPanel mapPanel;
	private SimplePanel navPanel;
	private SimplePanel footerPanel;
	private AbsolutePanel allPanel;
	
	private Panel navWidget;
	
	public void onModuleLoad() {
		getCredentials();
		
		// HEADER PANEL
		headerPanel = new SimplePanel();
		headerPanel.setWidth("960px");
		Image headerImage = new Image("images/header.png");
		headerPanel.add(headerImage);
		
		// MAP PANEL
		mapPanel = new MapPanel();
		mapPanel.setHeight(Window.getClientHeight()+"px");
		
		// NAV PANEL
		navPanel = new SimplePanel();
		navPanel.setWidth("300px");
		navPanel.setHeight("400px");
		navPanel.add( getNav() );
		
		
		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.setWidth("960px");
		footerPanel.add(new HTML("Copyright &copy; 2009 Little Lebowski Urban Achievers"));
		
		// ALL PANEL
		allPanel = new AbsolutePanel();
		allPanel.setSize("100%", Window.getClientHeight()+"px");
		allPanel.add(mapPanel,0,0);
		allPanel.add(headerPanel,50,0);
		allPanel.add(navPanel,Window.getClientWidth()-330,120);
		allPanel.add(footerPanel,Window.getClientWidth()/2-150,Window.getClientHeight()-50);
		
		RootPanel.get().add(allPanel);
	}
	
	private void getCredentials(){
		uid = (com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_user")==null) ? 1 : Long.valueOf(com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_user"));
		sessionKey = com.google.gwt.user.client.Cookies.getCookie(API_KEY+"_session_key");
		credentials.setApiKey(API_KEY);
		credentials.setSecretKey(SECRET_KEY);
		credentials.setSessionId(sessionKey);
		credentials.setUid(uid);
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
	
	public void onWindowResized(int width, int height) {
		allPanel.setHeight(height+"px");
		mapPanel.setHeight(height);
	}

}
