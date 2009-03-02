package com.l33tsp33k.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public abstract class ItemPanel extends Composite {
	public abstract void onStarClick();
	public ItemPanel(String imgUrl, Widget content) {
		HorizontalPanel widget = new HorizontalPanel();
		initWidget(widget);
		widget.setSpacing(10);

		widget.add(new Image(imgUrl));
		widget.add(content);

		final Image star = new Image("images/whitestar.gif");
		star.addMouseListener(new MouseListener() {
			public void onMouseDown(Widget sender, int x, int y) {}
			public void onMouseEnter(Widget sender) {
				star.setUrl("images/yellowstar.gif");
			}
			public void onMouseLeave(Widget sender) {
				star.setUrl("images/whitestar.gif");
			}
			public void onMouseMove(Widget sender, int x, int y) {}
			public void onMouseUp(Widget sender, int x, int y) {}
		});
		star.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				// Remove the click listener to avoid repeats
				star.removeClickListener(this);
				onStarClick();
			};							
		});
		widget.add(star);
	}
}