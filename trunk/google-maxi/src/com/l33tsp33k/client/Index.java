package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

	private VerticalPanel scrollContentPanel;
	private SimplePanel footerPanel;
	private VerticalPanel rightUtilPanel;
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
		SimplePanel mapsPanel = new SimplePanel();
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
		Anchor omg = new Anchor("OMG", true);
		omg.addClickListener( getTagListener("omg") );
		
		Anchor wtf = new Anchor("WTF", true);
		wtf.addClickListener( getTagListener("wtf") );
		
		Anchor lol = new Anchor("LOL", true);
		lol.addClickListener( getTagListener("lol") );
		
		Anchor ftw = new Anchor("FTW", true);
		ftw.addClickListener( getTagListener("ftw") );
		
		Anchor imho = new Anchor("IMHO", true);
		imho.addClickListener( getTagListener("imho") );

		Anchor fail = new Anchor("FAIL", true);
		fail.addClickListener( getTagListener("fail") );
		
		Anchor pwned = new Anchor("pwned", true);
		pwned.addClickListener( getTagListener("pwned") );
		
		Anchor jk = new Anchor("JK", true);
		jk.addClickListener( getTagListener("jk") );
		
		Anchor smileyFace = new Anchor(":-)", true);
		smileyFace.addClickListener( getTagListener(":-)") );
		
		Anchor sadFace = new Anchor(":-(", true);
		smileyFace.addClickListener( getTagListener(":-(") );

		VerticalPanel linksPanel = new VerticalPanel();
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
		GetTechnoratiData.App.getInstance().getFeedItems(tag, new AsyncCallback() {
          	public void onFailure(Throwable caught) {        			
              // TODO: error handling??
          		// Shows an error message - for testing purposes only
          		scrollContentPanel.add(new HTML("Error: Failed to get blogs. " + caught.getMessage() ));
            }
            public void onSuccess(Object response) {
              TechnoratiItemList results = (TechnoratiItemList) response;     				
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
				new AsyncCallback() {
					public void onFailure(Throwable caught) {
						// TODO: implement error handling???
						// shows an error message - for testing purposes only
						scrollContentPanel.add(new HTML("Error: Failed to get photos. " + caught.getMessage() ));
					}

					public void onSuccess(Object response) {
						FlickrPhotoList results = (FlickrPhotoList) response;
						
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
						}
						else
							scrollContentPanel.add(new HTML("No photos to list"));
					}
		});
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
				new AsyncCallback() {
					public void onFailure(Throwable caught) {
						// TODO: implement error handling???
					}

					public void onSuccess(Object response) {
						CachedSearchList results = (CachedSearchList) response;
						for (int i = 0; i < results.getSize(); i++) {
							suggestOracle.add(results.getSearchItem(i));
						}
					}
				});

		return oracle;
	}
}
