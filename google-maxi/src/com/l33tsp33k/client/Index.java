package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.maps.client.*;
import com.google.gwt.maps.client.control.MapTypeControl;  
import com.google.gwt.maps.client.control.SmallMapControl;  
import com.google.gwt.maps.client.geom.*;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import java.util.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	private static final String COOKIE = "l33tsp33k";
	private MapWidget mapWidget;
	private VerticalPanel scrollContentPanel;
	private VerticalPanel scrollPlaceHolderPanel;
	private SimplePanel footerPanel;
	private VerticalPanel rightUtilPanel;
	private SimplePanel mapsPanel;
	private ScrollPanel scrollFavoritesPanel;
	private VerticalPanel scrollItemsPanel;
	private MultiWordSuggestOracle suggestOracle;
	private String cookie = null;
	private ArrayList<ItemPanel> queue;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//Retrieve or set new cookie
		cookie = Cookies.getCookie(COOKIE);
		if(cookie==null){
			cookie = ""+System.currentTimeMillis();
			Cookies.setCookie(COOKIE, cookie);
		}

		// HEADER PANEL
		//SimplePanel headerPanel = new SimplePanel();
		//headerPanel.setWidth("100%");
		//Image headerImage = new Image("images/l33tsp33k_header.png");
		//headerImage.setTitle("Welcome to L33tSp33k.c0m");
		//headerPanel.add(headerImage);

		// LEFT-HAND NAVIGATION PANEL
		SimplePanel leftNavPanel = new SimplePanel();
		leftNavPanel.add( getLeftNavigation() );

		// SCROLL CONTENT PANEL
		scrollContentPanel = new VerticalPanel();
		scrollContentPanel.setWidth("441px");
		scrollContentPanel.setSpacing(10);
	    Element scp = scrollContentPanel.getElement();
		DOM.setStyleAttribute(scp, "position", "absolute");
		scp.getStyle().setPropertyPx("top", 600);

		// SCROLL PLACEHOLDER PANEL
		scrollPlaceHolderPanel = new VerticalPanel();
		scrollPlaceHolderPanel.setWidth("441px");

		// RIGHT-HAND UTILITIES PANEL
		rightUtilPanel = new VerticalPanel();

		// Google Maps panel
		mapsPanel = new SimplePanel();
		mapsPanel.setWidth("100%");
		mapsPanel.add( getMap() );

		// Scroll Items Panel
		scrollItemsPanel = new VerticalPanel();

		// Scroll Favorites Panel
		scrollFavoritesPanel = new ScrollPanel();
		scrollFavoritesPanel.setSize("398px","350px");
		scrollFavoritesPanel.setAlwaysShowScrollBars(true);
		scrollFavoritesPanel.getElement().setId("scrollFavorites");
		scrollFavoritesPanel.add(scrollItemsPanel);

		// Favorites Panel
		VerticalPanel favoritesPanel = new VerticalPanel();
		favoritesPanel.setWidth("100%");
		favoritesPanel.add(new HTML("<h2>Favorites:</h2>"));
		favoritesPanel.add(scrollFavoritesPanel);

		rightUtilPanel.add(mapsPanel);
		rightUtilPanel.add(favoritesPanel);

		// MAIN CONTENT PANEL
		HorizontalPanel mainContentPanel = new HorizontalPanel();
		mainContentPanel.setWidth("100%");
		mainContentPanel.add(leftNavPanel);
		mainContentPanel.add(scrollContentPanel);
		mainContentPanel.add(scrollPlaceHolderPanel);
		mainContentPanel.add(rightUtilPanel);

		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.setWidth("100%");
		footerPanel.getElement().setId("footer");
		HTML footerText = new HTML("<br /><br /><br /><b>Copyright &copy; 2009 Little Lebowski Urban Achievers</b>");
		footerPanel.add(footerText);

		// ALL PANEL
		VerticalPanel allPanel = new VerticalPanel();
		allPanel.setWidth("100%");
		allPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		//allPanel.add(headerPanel);
		allPanel.add(mainContentPanel);
		allPanel.add(footerPanel);

		// Add RootPanel
		RootPanel.get().add(allPanel);


		//scrollFavoritesPanel.
		//Add saved favorites
		GetFavorites.Util.getInstance().getFavoritePhotos(cookie,
				new AsyncCallback<FlickrPhoto[]>() {
			public void onFailure(Throwable caught) {
				// TODO: implement error handling???
			}

			public void onSuccess(FlickrPhoto[] favs) {
				for(FlickrPhoto o: favs){
					Window.alert("Added "+o.getUrl());
					addToFavorites(o);
				}
			}
		});					

		queue = new ArrayList<ItemPanel>();
		Timer scrollTimer = new Timer() {
			public void run() {
				if (queue.size() < 1) return;
			    ItemPanel item = queue.remove(Random.nextInt(queue.size()));
			    scrollNewItem(item);
			}

			private void scrollNewItem(ItemPanel item) {
				scrollContentPanel.add(item);
				int spacing = scrollContentPanel.getSpacing();
				int height = item.getOffsetHeight();
			    Element elem = scrollContentPanel.getElement();
			    int top = elem.getAbsoluteTop();
			    elem.getStyle().setPropertyPx("top", top - spacing - height);
			}
		};
		scrollTimer.scheduleRepeating(4000);
		
	}

	private VerticalPanel getLeftNavigation()
	{
		// Preset options links panel
		SimplePanel presetOptionsPanel = new SimplePanel();
		presetOptionsPanel.add( getPresetLinks() );

		// AutoComplete panel
		HTML search = new HTML("<br /><br />Search:");
		HorizontalPanel autoCompletePanel = new HorizontalPanel();
		suggestOracle = createSuggestionsOracle();
		final SuggestBox suggest = new SuggestBox(suggestOracle);
		suggest.addKeyboardListener( new KeyboardListener() {
			public void onKeyDown(Widget sender, char keycode, int modifiers)
			{
				if( keycode == KeyboardListener.KEY_ENTER )
					getDataFromSources( suggest.getText() );
				else if( keycode == KeyboardListener.KEY_ESCAPE )
					suggest.setText("");
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub

			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub

			}
		});
		Button searchButton = new Button("Go");
		searchButton.addClickListener( new ClickListener() {
			public void onClick(Widget sender) {
				getDataFromSources( suggest.getText() );
			}
		});
		searchButton.addKeyboardListener( new KeyboardListener() {
			public void onKeyDown(Widget sender, char keycode, int modifiers) 
			{
				if( keycode == KeyboardListener.KEY_ENTER )
					getDataFromSources( suggest.getText() );				
				else if( keycode == KeyboardListener.KEY_ESCAPE )
					suggest.setText("");
			}

			public void onKeyPress(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub

			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) {
				// TODO Auto-generated method stub

			}

		});

		autoCompletePanel.add(suggest);
		autoCompletePanel.add(searchButton);

		VerticalPanel leftNavPanel = new VerticalPanel();
		leftNavPanel.add(presetOptionsPanel);
		leftNavPanel.add(search);
		leftNavPanel.add(autoCompletePanel);

		return leftNavPanel;
	}

	final String words[] = {
			"omg",
			"wtf",
			"lol",
			"imho",
			"fail",
			"pwned",
			"jk",
			};
	private VerticalPanel getPresetLinks()
	{
		VerticalPanel linksPanel = new VerticalPanel();
		linksPanel.setWidth("100%");
		linksPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		for(int i=0; i<words.length; i++) {
			final String word = words[i];
			final Image omg = new Image("images/" + word + ".png");
			omg.addClickListener( getTagListener(word) );
			omg.addMouseListener( new MouseListener() {
				public void onMouseDown(Widget sender, int x, int y) {
				}
				public void onMouseEnter(Widget sender) {
					omg.setUrl("images/" + word + "_highlighted.png");
				}
				public void onMouseLeave(Widget sender) {
					omg.setUrl("images/" + word + ".png");
				}
				public void onMouseMove(Widget sender, int x, int y) {
				}
				public void onMouseUp(Widget sender, int x, int y) {
				}			
			});
			linksPanel.add(omg);
		}

		return linksPanel;
	}

	private ClickListener getTagListener(final String tag) {
		// TODO Auto-generated method stub
		// Sends requests on click
		return new ClickListener() {
			public void onClick( Widget sender ) {
				getDataFromSources( tag );
			}
		};
	}

	private void getDataFromSources( String tag )
	{
		// Clear previous content
		scrollContentPanel.clear();
	    Element scp = scrollContentPanel.getElement();
		scp.getStyle().setPropertyPx("top", 600);
		queue.clear();
		mapsPanel.clear();
		mapsPanel.add( getMap() );

		//Add tag to oracle
		suggestOracle.add(tag);

		//Get Flickr photos data
		GetFlickrData.App.getInstance().getFlickrPhotos( tag,
				new AsyncCallback<ArrayList<FlickrPhoto>>() {
			public void onFailure(Throwable caught) {
				// TODO: implement error handling???
				// shows an error message - for testing purposes only
				scrollContentPanel.add(new HTML("Error: Failed to get photos. " + caught.getMessage() ));
			}

			public void onSuccess(ArrayList<FlickrPhoto> results) {
				if( results.size() < 1 ) return;
				for(int i=0; i<10; i++)	{
					final FlickrPhoto photo = results.get(i);
					Image img = new Image(photo.getUrl());
					HTML title = new HTML(photo.getTitle() + "<br /><br />");

					VerticalPanel vp = new VerticalPanel();
					vp.setWidth("300px");
					vp.add(img);
					vp.add(title);

					ItemPanel photoPanel = new ItemPanel("images/flickr.png", vp) {
						public void onStarClick() {
							addToFavorites(photo);
							// Save to DB
							GetFavorites.Util.getInstance().addFlickrItem(photo, cookie,
									new AsyncCallback<CachedSearchList>() {
								public void onFailure(Throwable caught) {
									// TODO: implement error handling???
								}
								public void onSuccess(CachedSearchList results) {
									for (int i = 0; i < results.getSize(); i++) {
										suggestOracle.add(results.getSearchItem(i));
									}
								}
							});
						}
					};
					queue.add(photoPanel);
				}
				showMap(results);
			}
		});
		
		// Get Twitter data
		GetTwitterData.App.getInstance().getFeedItems(tag, new AsyncCallback<ArrayList<TwitterItem>>() {
			public void onFailure(Throwable caught) {        			
				// TODO: error handling??
				// Shows an error message - for testing purposes only
				scrollContentPanel.add(new HTML("Error: Failed to get blogs. " + caught.getMessage() ));
			}
			public void onSuccess(ArrayList<TwitterItem> results) {   				
				if(results.size() < 1) {
					return;
				}
				for(int i=0; i<results.size(); i++)
				{
					final TwitterItem fi = results.get(i);
					Anchor a = new Anchor(fi.name, fi.link);
					SimplePanel sp = new SimplePanel();
					sp.setWidth("300px");
					sp.add(a);

					ItemPanel tweetPanel = new ItemPanel("images/tweet.png", sp) {
						public void onStarClick() {
							addToFavorites(fi);
							// Save to DB
							GetFavorites.Util.getInstance().addTwitterItem(fi, cookie,
									new AsyncCallback<CachedSearchList>() {
								public void onFailure(Throwable caught) {
									// TODO: implement error handling???
								}
								public void onSuccess(CachedSearchList results) {
									for (int i = 0; i < results.getSize(); i++) {
										suggestOracle.add(results.getSearchItem(i));
									}
								}
							});
						}
					};
					queue.add(tweetPanel);
				}
			}
		});

		// Get Technorati blog data
		GetTechnoratiData.App.getInstance().getFeedItems(tag, new AsyncCallback<ArrayList<TechnoratiItem>>() {
			public void onFailure(Throwable caught) {        			
				// TODO: error handling??
				// Shows an error message - for testing purposes only
				scrollContentPanel.add(new HTML("Error: Failed to get blogs. " + caught.getMessage() ));
			}
			public void onSuccess(ArrayList<TechnoratiItem> results) {   				
				if(results.size() < 1) return;
				for(int i=0; i<results.size(); i++) {
					final TechnoratiItem fi = results.get(i);
					Anchor a = new Anchor(fi.name, fi.link);
					SimplePanel sp = new SimplePanel();
					sp.setWidth("300px");
					sp.add(a);

					ItemPanel blogPanel = new ItemPanel("images/blog.png", sp) {
						public void onStarClick() {
							addToFavorites(fi);
							// Save to DB
							GetFavorites.Util.getInstance().addTechnoratiItem(fi, cookie,
									new AsyncCallback<CachedSearchList>() {
								public void onFailure(Throwable caught) {
									// TODO: implement error handling???
								}
								public void onSuccess(CachedSearchList results) {
									for (int i = 0; i < results.getSize(); i++) {
										suggestOracle.add(results.getSearchItem(i));
									}
								}
							});
						}
					};
					queue.add(blogPanel);
				}
			}
		});

		//Add search tag to list of saved tags
		GetCachedSearches.App.getInstance().addCachedSearch(tag,
				new AsyncCallback<Object>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(Object response) {
			}
		});

	}

	private void showMap(ArrayList<FlickrPhoto> photos){
		//TODO: add twitter geocoding
		//TODO: favorites to/from DB

		//MapWidget mapWidget = getMap();

		for(int i=0; i<photos.size(); i++){
			FlickrPhoto p = photos.get(i);
			if(p.hasCoordinates()){
				LatLng l = LatLng.newInstance(p.getLat(), p.getLong());
				PhotoMarker m = new PhotoMarker(l,p);
				mapWidget.addOverlay(m);
				m.setImage("images/f.png");
				m.addMarkerClickHandler(new MarkerClickHandler() {
					public void	onClick(MarkerClickHandler.MarkerClickEvent event){
						//event.getSender().showMapBlowup(new InfoWindowContent("test"));
						PhotoMarker pm = (PhotoMarker)event.getSender();
						InfoWindow iw = mapWidget.getInfoWindow();
						iw.open(pm, new InfoWindowContent("<div><img src=\""+pm.getPhoto().getThumbnailUrl()+"\"><br>"+pm.getPhoto().getTitle()+"</div>"));
					}
				});
			}
		}

		mapsPanel.clear();
		mapsPanel.add(mapWidget);
	}

	private MapWidget getMap()
	{
		mapWidget = new MapWidget(LatLng.newInstance(38.548165,-95.361328), 3);  
		mapWidget.setSize("400px", "350px");  

		mapWidget.addControl(new SmallMapControl());  
		mapWidget.addControl(new MapTypeControl()); 

		return mapWidget;
	}

	private MultiWordSuggestOracle createSuggestionsOracle() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		oracle.add("omg");
		oracle.add("wtf");
		oracle.add("ftw");
		oracle.add("imo");
		oracle.add("imho");
		oracle.add("imao");
		oracle.add("sfw");
		oracle.add("nsfw");
		oracle.add("woot");
		oracle.add("w00t");
		oracle.add(":-)");
		oracle.add(":-(");
		oracle.add("l33t");

		GetCachedSearches.App.getInstance().getCachedSearches(
				new AsyncCallback<CachedSearchList>() {
					public void onFailure(Throwable caught) {
						// TODO: implement error handling???
					}

					public void onSuccess(CachedSearchList results) {
						for (int i = 0; i < results.getSize(); i++) {
							suggestOracle.add(results.getSearchItem(i));
						}
					}
				});

		return oracle;
	}
	
	private void addToFavorites(final FlickrPhoto photo) {
		// Add an item to scrollItemsPanel
		Image flickr_small = new Image("images/flickr_small.png");

		Image i = new Image(photo.getUrl());
		HTML t = new HTML(photo.getTitle() + "<br /><br />");
		
		final Image no = new Image("images/white_no.png");
		no.addMouseListener(new MouseListener() {

			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseEnter(Widget sender) {
				no.setUrl("images/red_no.png");
				
			}

			public void onMouseLeave(Widget sender) {
				no.setUrl("images/white_no.png");
				
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}
			
		});

		VerticalPanel v = new VerticalPanel();
		v.setWidth("265px");
		v.add(i);
		v.add(t);

		final HorizontalPanel favPanel = new HorizontalPanel();
		favPanel.setSpacing(10);
		favPanel.add(flickr_small);
		favPanel.add(v);
		favPanel.add(no);

		scrollItemsPanel.add(favPanel);
		
		no.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				scrollItemsPanel.remove(favPanel);
				
			}
			
		});
	}
	private void addToFavorites(final TechnoratiItem fi) {
		// Add an item to scrollItemsPanel
		Image blog_small = new Image("images/blog_small.png");

		Anchor an = new Anchor(fi.name, fi.link);

		SimplePanel s = new SimplePanel();
		s.setWidth("265px");
		s.add(an);
		
		final Image no = new Image("images/white_no.png");
		no.addMouseListener(new MouseListener() {

			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseEnter(Widget sender) {
				no.setUrl("images/red_no.png");
				
			}

			public void onMouseLeave(Widget sender) {
				no.setUrl("images/white_no.png");
				
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}
			
		});

		final HorizontalPanel favPanel = new HorizontalPanel();
		favPanel.setSpacing(10);
		favPanel.add(blog_small);
		favPanel.add(s);
		favPanel.add(no);

		scrollItemsPanel.add(favPanel);
		
		no.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				scrollItemsPanel.remove(favPanel);
				
			}
			
		});
	}
	private void addToFavorites(final TwitterItem fi) {
		// Add an item to scrollItemsPanel
		Image tweet_small = new Image("images/tweet_small.png");

		Anchor an = new Anchor(fi.name, fi.link);

		SimplePanel s = new SimplePanel();
		s.setWidth("265px");
		s.add(an);

		final Image no = new Image("images/white_no.png");
		no.addMouseListener(new MouseListener() {

			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseEnter(Widget sender) {
				no.setUrl("images/red_no.png");
				
			}

			public void onMouseLeave(Widget sender) {
				no.setUrl("images/white_no.png");
				
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		final HorizontalPanel favPanel = new HorizontalPanel();
		favPanel.setSpacing(10);
		favPanel.add(tweet_small);
		favPanel.add(s);
		favPanel.add(no);

		scrollItemsPanel.add(favPanel);
		
		no.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				scrollItemsPanel.remove(favPanel);
				
			}
			
		});
	}
}
