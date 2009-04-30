package edu.umn.cs.llebowski.client;

import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;
import java.util.Date;
import edu.umn.cs.llebowski.client.dataviews.*;

public class Index implements EntryPoint, WindowResizeListener {

	public static final String API_KEY = "31d94beadd44d773aea835f67edc4979";
	public static final String SECRET_KEY = "68ecd0a09347c63014a9fc2beee036fb";

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
	private VerticalPanel inviteVPanel;

	private MultiWordSuggestOracle autoComplete;

	public static LinkedList<Person> friends = new LinkedList<Person>();
	public static LinkedList<Person> allFriends = new LinkedList<Person>();
	public static Person you = new Person();
	
	private HTML distance = new HTML("");

	public void onModuleLoad() {
		getCredentials();

		// ALL PANEL
		allPanel = new AbsolutePanel();
		allPanel.setSize("100%", Window.getClientHeight()+"px");
		RootPanel.get().add(allPanel);

		drawMap();

		GetFriends.Util.getInstance().getFriends( credentials, new AsyncCallback<LinkedList<Person>>() {

			public void onFailure(Throwable caught) {
				//HTML h = new HTML("Error retrieving list of friends.");
			}

			public void onSuccess(LinkedList<Person> result) {
				friends = result;
				for(Person p: friends){
					if(p.getFbUid()==uid){
						you = p;
					}
				}
				drawGraphics();
			}
		});

		Timer t = new Timer() {
			public void run() {
				GetFriends.Util.getInstance().getFriends( credentials, new AsyncCallback<LinkedList<Person>>() {

					public void onFailure(Throwable caught) {
						//HTML h = new HTML("Error retrieving list of friends.");
						schedule(1000);		
					}

					public void onSuccess(LinkedList<Person> result) {
						friends = result;
						for(Person p: friends){
							if(p.getFbUid()==uid){
								you = p;
							}
							mapPanel.refreshMap();
							
						}
						friendsPanel.clear();
						friendsPanel.add( getFriendsPanel() );
						schedule(1000);		
					}
				});
			}
		};
		t.schedule(3000);		
		fbinit();
	}

	private void drawMap(){
		// MAP PANEL
		mapPanel = new MapPanel();
		mapPanel.setHeight(Window.getClientHeight()+"px");
	}

	private void drawGraphics(){
		// HEADER PANEL
		headerPanel = new SimplePanel();
		headerPanel.setWidth("960px");
		Image headerImage = new Image("images/header.png");
		headerPanel.add(headerImage);

		// NAV PANEL
		navPanel = new SimplePanel();
		navPanel.setWidth("400px");
		navPanel.setHeight("500px");
		navPanel.add( getNav() );

		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.getElement().setId("footer");
		footerPanel.setWidth("960px");
		footerPanel.add(new HTML("Copyright &copy; 2009 Little Lebowski Urban Achievers"));


		//All Panel
		allPanel.add(mapPanel,0,0);
		allPanel.add(headerPanel,50,0);
		allPanel.add(navPanel,Window.getClientWidth()-430,120);
		allPanel.add(footerPanel,Window.getClientWidth()/2-150,Window.getClientHeight()-50);
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
		navWidget.setSize("400px", "500px");
		navWidget.setCls("opaque");

		youPanel = new Panel("You");
		youPanel.setCls("opaque");
		youPanel.add( getYouPanel() );
		navWidget.add(youPanel);

		friendsPanel = new Panel("Friends");
		friendsPanel.setCtCls("opaque");
		friendsPanel.add( getFriendsPanel() );
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

		final HTML name = new HTML("<h1>" + you.getName() + "</h1>");
		final Image youPic = new Image( you.getProfilePic() );
		final HTML h1 = new HTML("<br /><b>Your current location:</b>");
		final HTML h2, h4;
		if( you.getLocations().size() > 0 )
		{
			h2 = new HTML( you.getLocations().getFirst().getAddress());
			h4 = new HTML(""+new Date(you.getLocations().getFirst().getTime()));
		}
		else
		{
			h2 = new HTML( "<br />");
			h4 = new HTML( "<br />");
		}
		final HTML h3 = new HTML("<br /><b>Last updated:</b>");
		final HTML h5 = new HTML("<br /><b>Update your location:");
		final HTML h6 = new HTML("<br /><b>See your location history:");
		final TextBox tb = new TextBox();
		tb.addKeyboardListener(new KeyboardListener(){

			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				if( keyCode == KeyboardListener.KEY_ENTER ){
					handleLocationUpdate(h2, h4, tb);
				}
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
				handleLocationUpdate(h2, h4, tb);
			}
		});
		
		Button b2 = new Button("History");
		b.addClickListener(new ClickListener(){
			public void onClick(Widget w){
				mapPanel.togglePersonTrack(you);
			}
		});

		hPanel.add(tb);
		hPanel.add(b);

		vPanel.add(youPic);
		vPanel.add(name);
		vPanel.add(h1);
		vPanel.add(h2);
		vPanel.add(h3);
		vPanel.add(h4);
		vPanel.add(h6);
		vPanel.add(b2);
		vPanel.add(h5);
		vPanel.add(hPanel);

		return vPanel;
	}

	private void handleLocationUpdate(final HTML h2, final HTML h4,
			final TextBox tb) {
		if(tb.getText().equals("")){
			return;
		}
		updateLocation( tb.getText() );
		h2.setHTML(tb.getText());
		h4.setHTML(""+new Date(System.currentTimeMillis()));
		showFeedDialog(tb.getText());
		tb.setText("");
	}

	private native void showFeedDialog(String location) /*-{
        $wnd.js_showFeedDialog(location);
    }-*/;
    
	private native void fbinit() /*-{
        $wnd.js_fbinit();
    }-*/;

	private void updateLocation( String location )
	{
		InsertLocations.Util.getInstance().insertUserAddedLocation(location, credentials, new AsyncCallback<LinkedList<Alert>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}
			public void onSuccess(LinkedList<Alert> alerts) {
				for(Alert a: alerts){
					Window.alert(a.getAlertText());
				}
			}
		});

	}
	
	private ScrollPanel getFriendsPanel()
	{
		final ScrollPanel sPanel = new ScrollPanel();
		sPanel.setAlwaysShowScrollBars(true);
		
		int i = 0;
		FlexTable t = new FlexTable();
		t.setCellPadding(2);
		t.setBorderWidth(1);

		for( Person friend : friends )
		{
			if(friend.getFbUid() == uid){
				continue;
			}
			PersonLocation pl = new PersonLocation();
			if(friend.getLocations().size()>0){
				pl = friend.getLocations().getFirst();
			}

			VerticalPanel vp = new VerticalPanel();
			Image pic = new Image( friend.getProfilePic() );
			HTML name = new HTML( friend.getName() );
			vp.add(pic);
			vp.add(name);

			Button b = new Button("History");
			final Person friendF = friend;
			b.addClickListener(new ClickListener(){
				public void onClick(Widget w){
					mapPanel.togglePersonTrack(friendF);
				}
			});
			
			// first column (pic/name)
			t.setWidget( i, 0, vp );

			// second column (distance)
			String distance = friend.getDistance()+"";
			int pt = distance.indexOf(".");
			t.setText( i, 1, distance.substring(0,pt+2)+" mi" );

			// third column (last updated);
			t.setText( i, 2, ""+new Date(friend.getLocations().getFirst().getTime()) );
			
			// fourth column (history)
			t.setWidget( i, 3, b );

			i++;
		}
		sPanel.add(t);
		return sPanel;
	}

	private VerticalPanel getInvitePanel()
	{
		inviteVPanel = new VerticalPanel();
		HorizontalPanel inviteHPanel = new HorizontalPanel();

		HTML h = new HTML("<b>Invite your Facebook friends to join Friend Mapper!</b><br /><br />");
		HTML h2 = new HTML("Friend: ");
		autoComplete = getAutoComplete();
		final SuggestBox sb = new SuggestBox(autoComplete);
		sb.addKeyboardListener(new KeyboardListener() {
			public void onKeyDown(Widget sender, char keyCode, int modifiers) {
				inviteVPanel.remove(2);
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
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		
		GetFriends.Util.getInstance().getAllFriends(credentials, new AsyncCallback<LinkedList<Person>>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(LinkedList<Person> result) {
				allFriends = result;
				for( Person f : result )
					oracle.add(f.getName());				
			}
			
		});

		return oracle;
	}

	private void inviteFriend( String friend )
	{
		Person invitee = null;
		// invite a friend to use Friend Mapper
		for( Person f : allFriends )
		{
			if( f.getName() == friend )
			{
				invitee = f;
				break;
			}
		}
		
		if( invitee == null )
		{
			HTML h = new HTML("<br /><br /><span class=\"error\">Error: Your friend, " + friend + ", is not one of your friends on Facebook.</span>");
			inviteVPanel.add(h);
		}
		else
		{
			HTML h = new HTML("<br /><br />Your friend, " + friend + ", has been invited to join Friend Mapper.");
			inviteVPanel.add(h);
		
			GetFriends.Util.getInstance().sendInvite(invitee, credentials, new AsyncCallback<Boolean>() {
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub	
				}
				public void onSuccess(Boolean b) {			
				}
				
			});
		}	
	}

	public static LinkedList<Person> getFriends(){
		return friends;
	}
}
