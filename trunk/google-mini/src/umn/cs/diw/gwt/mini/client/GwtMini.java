package umn.cs.diw.gwt.mini.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import umn.cs.diw.gwt.mini.client.datamodel.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtMini implements EntryPoint {

	private TextBox tb;
	private VerticalPanel resultsPanel;
	
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Image img = new Image("http://code.google.com/webtoolkit/logo-185x175.png");
    Button button = new Button("Click me");
    tb = new TextBox();
    
    // We can add style names
    button.addStyleName("pc-template-btn");
    // or we can set an id on a specific element for styling
    img.getElement().setId("pc-template-img");
    
    VerticalPanel vPanel = new VerticalPanel();
    vPanel.setWidth("100%");
    vPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    vPanel.add(img);
    vPanel.add(tb);
    vPanel.add(button);
    
    resultsPanel = new VerticalPanel();
    resultsPanel.setVisible(false);
    vPanel.add(resultsPanel);
    
    // Add image and button to the RootPanel
    RootPanel.get().add(vPanel);

    // Create the dialog box
    final DialogBox dialogBox = new DialogBox();
    dialogBox.setText("Welcome to GWT!");
    dialogBox.setAnimationEnabled(true);
    Button closeButton = new Button("close");
    VerticalPanel dialogVPanel = new VerticalPanel();
    dialogVPanel.setWidth("100%");
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
    dialogVPanel.add(closeButton);

    closeButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        dialogBox.hide();
      }
    });

    // Set the contents of the Widget
    dialogBox.setWidget(dialogVPanel);
    
    button.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          GetFeedService.App.getInstance().getFeedItems(tb.getText(), new AsyncCallback() {
          	public void onFailure(Throwable caught) {        			
              dialogBox.setText("Something went wrong");
              dialogBox.center();
              dialogBox.show();
            }
            public void onSuccess(Object response) {
              FeedItemList results = (FeedItemList) response;     				
              FeedItem item;

              resultsPanel.clear();
              resultsPanel.setVisible(true);

              if(((FeedItemList)results).getSize() > 0) {
                item = ((FeedItemList) results).getFeedItem(0);
                for(int i=0; i<((FeedItemList)results).getSize(); i++){
                	FeedItem fi = ((FeedItemList)results).getFeedItem(i);
                	Anchor a = new Anchor(fi.name, fi.link);
                	resultsPanel.add(a);
                }
              }
            }});
        }});
  }
}
