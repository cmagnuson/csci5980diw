package com.l33tsp33k.client;

import com.l33tsp33k.client.datamodels.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

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
		VerticalPanel leftNavPanel = new VerticalPanel();
		leftNavPanel.setWidth("20%");

		// Preset options links panel
		SimplePanel presetOptionsPanel = new SimplePanel();
		presetOptionsPanel.add( getPresetLinks() );
		
		// AutoComplete panel
		SimplePanel autoCompletePanel = new SimplePanel();
		suggestOracle = createSuggestionsOracle();
		SuggestBox suggest = new SuggestBox(suggestOracle);
		autoCompletePanel.add(suggest);

		leftNavPanel.add(presetOptionsPanel);
		leftNavPanel.add(autoCompletePanel);

		// SCROLL CONTENT PANEL
		SimplePanel scrollContentPanel = new SimplePanel();
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

		GetFlickrData.App.getInstance().getFlickrPhotos("LOL",
				new AsyncCallback() {
					public void onFailure(Throwable caught) {
						// TODO: implement error handling???
					}

					public void onSuccess(Object response) {
						FlickrPhotoList results = (FlickrPhotoList) response;
						HTML j = new HTML(results.toString());
						rightUtilPanel.add(j);
					}
				});

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

	private VerticalPanel getPresetLinks()
	{
		Anchor omg = new Anchor("OMG", true);
		omg.addClickListener( getTagListener("omg") );
		
		Anchor wtf = new Anchor("WTF", true);
		wtf.addClickListener( getTagListener("wtf") );
		
		Anchor imho = new Anchor("IMHO", true);
		imho.addClickListener( getTagListener("imho") );
		
		Anchor smileyFace = new Anchor(":-)", true);
		smileyFace.addClickListener( getTagListener(":-)") );
		
		Anchor sadFace = new Anchor(":-(", true);
		smileyFace.addClickListener( getTagListener(":-(") );

		VerticalPanel linksPanel = new VerticalPanel();
		linksPanel.add(omg);
		linksPanel.add(wtf);
		linksPanel.add(imho);
		linksPanel.add(smileyFace);
		linksPanel.add(sadFace);
		
		return linksPanel;
	}
	
	private ClickListener getTagListener(final String string) {
		// TODO Auto-generated method stub
		// Sends requests on click
		return new ClickListener() {
			public void onClick( Widget sender ) {
				footerPanel.setWidget(new HTML("HI DUDES!! " + string));
			}
		};
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
