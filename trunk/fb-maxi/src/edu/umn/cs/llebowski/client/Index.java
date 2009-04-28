package edu.umn.cs.llebowski.client;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;
import com.google.gwt.user.client.*;
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
	private Panel youPanel;
	private Panel friendsPanel;
	private Panel invitePanel;
	
	private MultiWordSuggestOracle autoComplete;
	
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
		navWidget = new Panel("Options");
		navWidget.setLayout(new AccordionLayout(true));
		navWidget.setSize("300px", "500px");
		navWidget.setCls("opaque");
		
		youPanel = new Panel("You");
		youPanel.setCls("opaque");
		youPanel.add( getYouPanel() );
		navWidget.add(youPanel);
		
		friendsPanel = new Panel("Friends");
		friendsPanel.setCtCls("opaque");
		//friendsPanel.add( getFriendsPanel() );
		navWidget.add(friendsPanel);
		
		invitePanel = new Panel("Invite");
		invitePanel.setCls("opaque");
		invitePanel.add( getInvitePanel() );
		navWidget.add(invitePanel);
		
		return navWidget;
	}
	
	public void onWindowResized(int width, int height) {
		allPanel.setHeight(height+"px");
		mapPanel.setHeight(height);
	}
	
	private VerticalPanel getYouPanel()
	{
		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		
		HTML h1 = new HTML("<b>Your current location:</b>");
		//HTML h2 = new HTML( getCurrentLocation() );
		HTML h3 = new HTML("<b>Last updated:</b>");
		//HTML h4 = new HTML( getLastUpdated() );
		HTML h5 = new HTML("<b>Update your location:");
		final TextBox tb = new TextBox();
		tb.addKeyboardListener(new KeyboardListener(){

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if( keyCode == KeyboardListener.KEY_ENTER )
					updateLocation( tb.getText() );
				else if( keyCode == KeyboardListener.KEY_ESCAPE )
					tb.setText("");
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}
			
		});
		Button b = new Button("Update");
		b.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				updateLocation( tb.getText() );
			}
			
		});
		
		hPanel.add(tb);
		hPanel.add(b);
		
		vPanel.add(h1);
		//vPanel.add(h2);
		vPanel.add(h3);
		//vPanel.add(h4);
		vPanel.add(h5);
		vPanel.add(hPanel);
		
		return vPanel;
	}

	private void updateLocation( String location )
	{
		
	}
	
	private VerticalPanel getInvitePanel()
	{
		VerticalPanel inviteVPanel = new VerticalPanel();
		HorizontalPanel inviteHPanel = new HorizontalPanel();
		
		HTML h = new HTML("<b>Invite your Facebook friends to join Friend Mapper!</b><br />");
		HTML h2 = new HTML("Name: ");
		autoComplete = getAutoComplete();
		final SuggestBox sb = new SuggestBox(autoComplete);
		sb.addKeyboardListener(new KeyboardListener() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if( keyCode == KeyboardListener.KEY_ENTER )
					inviteFriend( sb.getText() );
				else if( keyCode == KeyboardListener.KEY_ESCAPE )
					sb.setText("");
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub
				
			}	
		});
		
		Button b = new Button("Invite");
		b.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				inviteFriend( sb.getText() );
			}			
		});
		
		inviteHPanel.add(h2);
		inviteHPanel.add(sb);
		inviteHPanel.add(b);
		
		inviteVPanel.add(h);
		inviteVPanel.add(inviteHPanel);
		
		return inviteVPanel;		
	}
	
	private MultiWordSuggestOracle getAutoComplete()
	{
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		
		// add FB friends that are have NOT joined Friend Mapper
		
		return oracle;
	}
	
	private void inviteFriend( String friend )
	{
		// invite a friend to use Friend Mapper
	}
}
