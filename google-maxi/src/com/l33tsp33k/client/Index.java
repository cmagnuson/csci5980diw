package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.maps.client.*;
import com.google.gwt.maps.client.control.MapTypeControl;  
import com.google.gwt.maps.client.control.SmallMapControl;  
import com.google.gwt.maps.client.event.MapClickHandler;  
import com.google.gwt.maps.client.geom.LatLng;  
import com.google.gwt.maps.client.overlay.Marker;  
import com.google.gwt.maps.client.overlay.Overlay;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	private VerticalPanel scrollContentPanel;
	private SimplePanel footerPanel;
	private VerticalPanel rightUtilPanel;
	private SimplePanel mapsPanel;
	private MultiWordSuggestOracle suggestOracle;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		// HEADER PANEL
		SimplePanel headerPanel = new SimplePanel();
		headerPanel.setWidth("100%");
		Image headerImage = new Image("images/l33tsp33k_header.png");
		headerImage.setTitle("Welcome to L33tSp33k.c0m");
		headerPanel.add(headerImage);

		// LEFT-HAND NAVIGATION PANEL
		SimplePanel leftNavPanel = new SimplePanel();
		leftNavPanel.setWidth("20%");
        leftNavPanel.add( getLeftNavigation() );

		// SCROLL CONTENT PANEL
		scrollContentPanel = new VerticalPanel();
		scrollContentPanel.setWidth("40%");
		HTML text2 = new HTML("Scrolling list of items here");
		scrollContentPanel.add(text2);

		// RIGHT-HAND UTILITIES PANEL
		rightUtilPanel = new VerticalPanel();
		rightUtilPanel.setWidth("40%");

		// Google Maps panel
		mapsPanel = new SimplePanel();
		mapsPanel.setWidth("100%");
		HTML text3 = new HTML("Map here");
		mapsPanel.add(text3);

		// Favorites Panel
		SimplePanel favoritesPanel = new SimplePanel();
		favoritesPanel.setWidth("100%");
		HTML text4 = new HTML("Favorites here");
		favoritesPanel.add(text4);

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
		HTML text5 = new HTML("<b>Copyright &copy; 2009 Little Lebowski Urban Achievers</b>");
		footerPanel.add(text5);



		// ALL PANEL
		VerticalPanel allPanel = new VerticalPanel();
		allPanel.setWidth("100%");
		allPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
		allPanel.add(headerPanel);
		allPanel.add(mainContentPanel);
		allPanel.add(footerPanel);

		// Add RootPanel
		RootPanel.get().add(allPanel);

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

		VerticalPanel linksPanel = new VerticalPanel();
		linksPanel.setWidth("100%");
		linksPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		linksPanel.add(omg);
		linksPanel.add(wtf);
		linksPanel.add(lol);
		linksPanel.add(ftw);
		linksPanel.add(imho);
		linksPanel.add(fail);
		linksPanel.add(pwned);
		linksPanel.add(jk);
		linksPanel.add(smileyFace);
		linksPanel.add(sadFace);
		
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
		
		// Get Twitter data
		
		// Get Technorati blog data
		GetTechnoratiData.App.getInstance().getFeedItems(tag, new AsyncCallback<TechnoratiItemList>() {
          	public void onFailure(Throwable caught) {        			
              // TODO: error handling??
          		// Shows an error message - for testing purposes only
          		scrollContentPanel.add(new HTML("Error: Failed to get blogs. " + caught.getMessage() ));
            }
            public void onSuccess(TechnoratiItemList results) {   				
              TechnoratiItem item;

              if(((TechnoratiItemList)results).getSize() > 0) {
                item = ((TechnoratiItemList) results).getFeedItem(0);
                VerticalPanel blogsPanel = new VerticalPanel();
                for(int i=0; i<((TechnoratiItemList)results).getSize(); i++){
                	TechnoratiItem fi = ((TechnoratiItemList)results).getFeedItem(i);
                	Anchor a = new Anchor(fi.name, fi.link);
                	blogsPanel.add(a);
                }
                scrollContentPanel.add(blogsPanel);
              }
              else
            	  scrollContentPanel.add(new HTML("No blog items to list."));
            }});
		
		// Get ???
		
		// Get Flickr photos data
		GetFlickrData.App.getInstance().getFlickrPhotos( tag,
				new AsyncCallback<FlickrPhotoList>() {
					public void onFailure(Throwable caught) {
						// TODO: implement error handling???
						// shows an error message - for testing purposes only
						scrollContentPanel.add(new HTML("Error: Failed to get photos. " + caught.getMessage() ));
					}

					public void onSuccess(FlickrPhotoList results) {
						
						if( results.getSize() > 0 )
						{
							VerticalPanel photosPanel = new VerticalPanel();
							for(int i=0; i<10; i++)
							{
								FlickrPhoto photo = results.getPhoto(i);
								Image img = new Image(photo.getUrl());
								HTML title = new HTML(photo.getTitle() + "<br /><br />");
								photosPanel.add(img);
								photosPanel.add(title);
							}
						
							scrollContentPanel.add(photosPanel);
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
	
	private void showMap(FlickrPhotoList photos){
		MapWidget mapWiget = new MapWidget(LatLng.newInstance(38.548165,-95.361328), 3);  
        mapWiget.setSize("350px", "350px");  
  
        mapWiget.addControl(new SmallMapControl());  
        mapWiget.addControl(new MapTypeControl()); 
        
        for(int i=0; i<photos.getSize(); i++){
          FlickrPhoto p = photos.getPhoto(i);
          if(p.hasCoordinates()){
        	  mapWiget.addOverlay(new com.google.gwt.maps.client.overlay.Marker(LatLng.newInstance(p.getLat(), p.getLong())));
          }
        }
        
        mapsPanel.clear();
        mapsPanel.add(mapWiget);
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
}
