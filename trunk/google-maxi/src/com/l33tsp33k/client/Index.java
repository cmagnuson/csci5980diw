package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.core.client.*;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.maps.client.*;
import com.google.gwt.maps.client.control.MapTypeControl;  
import com.google.gwt.maps.client.control.SmallMapControl;  
import com.google.gwt.maps.client.geom.*;
import com.google.gwt.maps.client.overlay.*;  
import com.google.gwt.maps.client.event.MarkerClickHandler;
import java.util.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	private static final String COOKIE = "l33tsp33k";
	private MapWidget mapWidget;
	private VerticalPanel scrollContentPanel;
	private SimplePanel footerPanel;
	private VerticalPanel rightUtilPanel;
	private SimplePanel mapsPanel;
	private ScrollPanel scrollFavoritesPanel;
	private VerticalPanel scrollItemsPanel;
	private MultiWordSuggestOracle suggestOracle;
	private String cookie = null;

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
		SimplePanel headerPanel = new SimplePanel();
		headerPanel.setWidth("100%");
		Image headerImage = new Image("images/l33tsp33k_header.png");
		headerImage.setTitle("Welcome to L33tSp33k.c0m");
		headerPanel.add(headerImage);

		// LEFT-HAND NAVIGATION PANEL
		SimplePanel leftNavPanel = new SimplePanel();
		leftNavPanel.add( getLeftNavigation() );

		// SCROLL CONTENT PANEL
		scrollContentPanel = new VerticalPanel();
		scrollContentPanel.setWidth("441px");
		scrollContentPanel.setSpacing(10);

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
		scrollFavoritesPanel.setSize("348px","350px");
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
		mainContentPanel.add(rightUtilPanel);

		// FOOTER PANEL
		footerPanel = new SimplePanel();
		footerPanel.setWidth("100%");
		footerPanel.getElement().setId("footer");
		HTML footerText = new HTML("<b>Copyright &copy; 2009 Little Lebowski Urban Achievers</b>");
		footerPanel.add(footerText);

		// ALL PANEL
		VerticalPanel allPanel = new VerticalPanel();
		allPanel.setWidth("100%");
		allPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		allPanel.add(headerPanel);
		allPanel.add(mainContentPanel);
		allPanel.add(footerPanel);

		// Add RootPanel
		RootPanel.get().add(allPanel);


		//scrollFavoritesPanel.
		//Add saved favorites
//		GetFavorites.Util.getInstance().getFavorites(cookie,
//				new AsyncCallback<HashMap<Object, String>>() {
//			public void onFailure(Throwable caught) {
//				// TODO: implement error handling???
//			}
//
//			public void onSuccess(HashMap<Object, String> favs) {
//				for(Object o: favs.keySet()){
//					if(o instanceof FlickrPhoto){
//						addToFavorites((FlickrPhoto)o);
//					}
//					if(o instanceof TechnoratiItem){
//						addToFavorites((TechnoratiItem)o);
//					}
//				}
//			}
//		});					
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

	private VerticalPanel getPresetLinks()
	{
		final Image omg = new Image("images/omg.png");
		omg.addClickListener( getTagListener("omg") );
		omg.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				omg.setUrl("images/omg_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				omg.setUrl("images/omg.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}			
		});

		final Image wtf = new Image("images/wtf.png");
		wtf.addClickListener( getTagListener("wtf") );
		wtf.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				wtf.setUrl("images/wtf_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				wtf.setUrl("images/wtf.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}			
		});

		final Image lol = new Image("images/lol.png");
		lol.addClickListener( getTagListener("lol") );
		lol.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				lol.setUrl("images/lol_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				lol.setUrl("images/lol.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}
		});

		/*
		final Image ftw = new Image("images/ftw.png");
		ftw.addClickListener( getTagListener("ftw") );
		ftw.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				ftw.setUrl("images/ftw_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				ftw.setUrl("images/ftw.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});
		 */

		final Image imho = new Image("images/imho.png");
		imho.addClickListener( getTagListener("imho") );
		imho.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				imho.setUrl("images/imho_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				imho.setUrl("images/imho.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});

		final Image fail = new Image("images/fail.png");
		fail.addClickListener( getTagListener("fail") );
		fail.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				fail.setUrl("images/fail_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				fail.setUrl("images/fail.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});

		final Image pwned = new Image("images/pwned.png");
		pwned.addClickListener( getTagListener("pwned") );
		pwned.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				pwned.setUrl("images/pwned_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				pwned.setUrl("images/pwned.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});

		final Image jk = new Image("images/jk.png");
		jk.addClickListener( getTagListener("jk") );
		jk.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				jk.setUrl("images/jk_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				jk.setUrl("images/jk.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});

		/*
		final Image smileyFace = new Image("images/smiley.png");
		smileyFace.addClickListener( getTagListener(":-)") );
		smileyFace.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				smileyFace.setUrl("images/smiley_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				smileyFace.setUrl("images/smiley.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});

		final Image sadFace = new Image("images/sad.png");
		sadFace.addClickListener( getTagListener(":-(") );
		sadFace.addMouseListener( new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseEnter(Widget sender) {
				sadFace.setUrl("images/sad_highlighted.png");
			}

			public void onMouseLeave(Widget sender) {
				sadFace.setUrl("images/sad.png");
			}

			public void onMouseMove(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

			public void onMouseUp(Widget sender, int x, int y) {
				// TODO Auto-generated method stub

			}

		});
		 */

		VerticalPanel linksPanel = new VerticalPanel();
		linksPanel.setWidth("100%");
		linksPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		linksPanel.add(omg);
		linksPanel.add(wtf);
		linksPanel.add(lol);
		//linksPanel.add(ftw);
		linksPanel.add(imho);
		linksPanel.add(fail);
		linksPanel.add(pwned);
		linksPanel.add(jk);
		//linksPanel.add(smileyFace);
		//linksPanel.add(sadFace);

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
		mapsPanel.clear();
		mapsPanel.add( getMap() );

		//Add tag to oracle
		suggestOracle.add(tag);

		// Get Twitter data

		// Get Technorati blog data
		GetTechnoratiData.App.getInstance().getFeedItems(tag, new AsyncCallback<ArrayList<TechnoratiItem>>() {
			public void onFailure(Throwable caught) {        			
				// TODO: error handling??
				// Shows an error message - for testing purposes only
				scrollContentPanel.add(new HTML("Error: Failed to get blogs. " + caught.getMessage() ));
			}
			public void onSuccess(ArrayList<TechnoratiItem> results) {   				
				TechnoratiItem item;

				if(((ArrayList<TechnoratiItem>)results).size() > 0) 
				{
					item = ((ArrayList<TechnoratiItem>) results).get(0);
					for(int i=0; i<((ArrayList<TechnoratiItem>)results).size(); i++)
					{
						Image blog = new Image("images/blog.png");

						final TechnoratiItem fi = ((ArrayList<TechnoratiItem>)results).get(i);
						Anchor a = new Anchor(fi.name, fi.link);

						SimplePanel sp = new SimplePanel();
						sp.setWidth("300px");
						sp.add(a);

						final Image star = new Image("images/whitestar.gif");
						ClickListener starClick = createClickListener(fi, star);
						star.addClickListener( starClick );						


						star.addMouseListener(new MouseListener() {
							public void onMouseDown(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}

							public void onMouseEnter(Widget sender) {
								star.setUrl("images/yellowstar.gif");
							}

							public void onMouseLeave(Widget sender) {
								star.setUrl("images/whitestar.gif");
							}

							public void onMouseMove(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}

							public void onMouseUp(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}
						});


						HorizontalPanel blogPanel = new HorizontalPanel();
						blogPanel.setSpacing(10);
						blogPanel.add(blog);
						blogPanel.add(sp);
						blogPanel.add(star);

						scrollContentPanel.add(blogPanel);
					}
				}
				else
					scrollContentPanel.add(new HTML("No blog items to list."));
			}
			private ClickListener createClickListener(final TechnoratiItem fi,
					final Image star) {
				ClickListener starClick = new ClickListener() 
				{
					public void onClick(Widget sender) {
						addToFavorites(fi);

						// Remove the click listener to avoid repeats
						star.removeClickListener(this);

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
				return starClick;
			}});

		// Get ???

		// Get Flickr photos data
		GetFlickrData.App.getInstance().getFlickrPhotos( tag,
				new AsyncCallback<ArrayList<FlickrPhoto>>() {
			public void onFailure(Throwable caught) {
				// TODO: implement error handling???
				// shows an error message - for testing purposes only
				scrollContentPanel.add(new HTML("Error: Failed to get photos. " + caught.getMessage() ));
			}

			public void onSuccess(ArrayList<FlickrPhoto> results) {

				if( results.size() > 0 )
				{
					for(int i=0; i<10; i++)
					{
						Image flickr = new Image("images/flickr.png");

						final FlickrPhoto photo = results.get(i);
						Image img = new Image(photo.getUrl());
						HTML title = new HTML(photo.getTitle() + "<br /><br />");

						VerticalPanel vp = new VerticalPanel();
						vp.setWidth("300px");
						vp.add(img);
						vp.add(title);

						final Image star = new Image("images/whitestar.gif");
						ClickListener starClick = new ClickListener() 
						{
							public void onClick(Widget sender) {
								addToFavorites(photo);

								// Remove the click listener to avoid repeats
								star.removeClickListener(this);

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
								});							}

						};
						star.addClickListener( starClick );						

						star.addMouseListener(new MouseListener() {
							public void onMouseDown(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}

							public void onMouseEnter(Widget sender) {
								star.setUrl("images/yellowstar.gif");
							}

							public void onMouseLeave(Widget sender) {
								star.setUrl("images/whitestar.gif");
							}

							public void onMouseMove(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}

							public void onMouseUp(Widget sender, int x, int y) {
								// TODO Auto-generated method stub

							}	
						});


						HorizontalPanel photoPanel = new HorizontalPanel();
						photoPanel.setSpacing(10);
						photoPanel.add(flickr);
						photoPanel.add(vp);
						photoPanel.add(star);

						scrollContentPanel.add(photoPanel);
					}

					showMap(results);
				}
				else
					scrollContentPanel.add(new HTML("No photos to list"));
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
		mapWidget.setSize("350px", "350px");  

		mapWidget.addControl(new SmallMapControl());  
		mapWidget.addControl(new MapTypeControl()); 

		this.mapWidget = mapWidget;
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

		VerticalPanel v = new VerticalPanel();
		v.setWidth("250px");
		v.add(i);
		v.add(t);

		HorizontalPanel favPanel = new HorizontalPanel();
		favPanel.setSpacing(10);
		favPanel.add(flickr_small);
		favPanel.add(v);

		scrollItemsPanel.add(favPanel);
	}
	private void addToFavorites(final TechnoratiItem fi) {
		// Add an item to scrollItemsPanel
		Image blog_small = new Image("images/blog_small.png");

		Anchor an = new Anchor(fi.name, fi.link);

		SimplePanel s = new SimplePanel();
		s.setWidth("250px");
		s.add(an);

		HorizontalPanel favPanel = new HorizontalPanel();
		favPanel.setSpacing(10);
		favPanel.add(blog_small);
		favPanel.add(s);

		scrollItemsPanel.add(favPanel);
	}
}
