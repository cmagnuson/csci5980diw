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
    HTML title = new HTML("<h1>L33TSp33k.c0m</h1>");
    
    
    FocusPanel fPanel1 = new FocusPanel();
    fPanel1.setWidth("20%");
    HTML text = new HTML("Left-hand Navigation here");
    SuggestBox suggest = new SuggestBox( createSuggestionsOracle() );
    //fPanel1.add(text);
    fPanel1.add(suggest);
    
    FocusPanel fPanel2 = new FocusPanel();
    fPanel2.setWidth("40%");
    HTML text2 = new HTML("Scrolling list of items here");
    fPanel2.add(text2);

    FocusPanel fPanel3 = new FocusPanel();
    fPanel3.setWidth("40%");
    HTML text3 = new HTML("Maps and favorites here");
    fPanel3.add(text3);
    
    HorizontalPanel hPanel1 = new HorizontalPanel();
    hPanel1.setWidth("100%");
    hPanel1.add(fPanel1);
    hPanel1.add(fPanel2);
    hPanel1.add(fPanel3);
    
    // We can add style names
    button.addStyleName("pc-template-btn");
    // or we can set an id on a specific element for styling
    img.getElement().setId("pc-template-img");
    
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setWidth("100%");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
    vPanel.add(title);
    vPanel.add(hPanel1);

    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

  }

  private MultiWordSuggestOracle createSuggestionsOracle() {
	MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	oracle.add("omg");
	oracle.add("wtf");
	oracle.add("ftw");
	oracle.add("woot");
	oracle.add("w00t");
	oracle.add(":-)");
	return oracle; 
  }
}
