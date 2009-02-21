package com.l33tsp33k.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Index implements EntryPoint {

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Click me");

    // HEADER PANEL
    SimplePanel headerPanel = new SimplePanel();
    headerPanel.setWidth("100%");
    HTML title = new HTML("<h1>L33TSp33k.c0m</h1>");
    headerPanel.add(title);
    
    // LEFT-HAND NAVIGATION PANEL
    VerticalPanel leftNavPanel = new VerticalPanel();
    leftNavPanel.setWidth("20%");
    
    // Preset options links panel
    SimplePanel presetOptionsPanel = new SimplePanel();
    HTML text = new HTML("Left-hand Navigation here");
    presetOptionsPanel.add(text);
    
    // AutoComplete panel
    SimplePanel autoCompletePanel = new SimplePanel();
    SuggestBox suggest = new SuggestBox( createSuggestionsOracle() );
    autoCompletePanel.add(suggest);
    
    leftNavPanel.add(presetOptionsPanel);
    leftNavPanel.add(autoCompletePanel);
    
    // SCROLL CONTENT PANEL
    SimplePanel scrollContentPanel = new SimplePanel();
    scrollContentPanel.setWidth("40%");
    HTML text2 = new HTML("Scrolling list of items here");
    scrollContentPanel.add(text2);

    // RIGHT-HAND UTILITIES PANEL
    VerticalPanel rightUtilPanel = new VerticalPanel();
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
    SimplePanel footerPanel = new SimplePanel();
    footerPanel.setWidth("100%");
    HTML text5 = new HTML("Footer here");
    footerPanel.add(text5);
    
    // ALL PANEL
    VerticalPanel allPanel = new VerticalPanel();
    allPanel.setWidth("100%");
    allPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
    allPanel.add(headerPanel);
    allPanel.add(mainContentPanel);
    allPanel.add(footerPanel);
    
    // Add  RootPanel
    RootPanel.get().add(allPanel);
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
	return oracle; 
  }
}
