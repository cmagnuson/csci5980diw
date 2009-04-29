package edu.umn.cs.llebowski.client;

import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import edu.umn.cs.llebowski.client.datamodels.*;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.AccordionLayout;
import java.util.*;
import edu.umn.cs.llebowski.client.datamodels.alerts.*;

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

	public static LinkedList<Person> friends = new LinkedList<Person>();
	public static Person you = new Person();

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
		navPanel.setWidth("300px");
		navPanel.setHeight("400px");
		navPanel.add( getNav() );

		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.getElement().setId("footer");
		footerPanel.setWidth("960px");
		footerPanel.add(new HTML("Copyright &copy; 2009 Little Lebowski Urban Achievers"));


		//All Panel
		allPanel.add(mapPanel,0,0);
		allPanel.add(headerPanel,50,0);
		allPanel.add(navPanel,Window.getClientWidth()-330,120);
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
		navWidget.setSize("300px", "500px");
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

		final HTML h1 = new HTML("<b>Your current location:</b>");
		final HTML h2 = new HTML( you.getLocations().getFirst().getAddress());
		final HTML h3 = new HTML("<b>Last updated:</b>");
		final HTML h4 = new HTML(""+new Date(you.getLocations().getFirst().getTime()));
		final HTML h5 = new HTML("<b>Update your location:");
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

		hPanel.add(tb);
		hPanel.add(b);

		vPanel.add(h1);
		vPanel.add(h2);
		vPanel.add(h3);
		vPanel.add(h4);
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
		h4.setHTML(""+new Date(System.currentTimeMillis()/1000));
		tb.setText("");
	}

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

	private VerticalPanel getFriendsPanel()
	{
		final VerticalPanel vPanel = new VerticalPanel();

		int i = 0;
		FlexTable t = new FlexTable();

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

			// first column (pic/name)
			t.setWidget( i, 0, vp );

			// second column (distance)
			t.setText( i, 1, "1.88 mi" );

			// third column (last updated);
			t.setText( i, 2, "April 20, 2009" );

			// fourth column (history)
			t.setWidget( i, 3, b );

			i++;
		}
		vPanel.add(t);
		return vPanel;
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
	
	public static LinkedList<Person> getFriends(){
		return friends;
	}
}
