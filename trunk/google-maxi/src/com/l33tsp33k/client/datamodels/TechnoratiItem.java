package com.l33tsp33k.client.datamodels;
import com.google.gwt.user.client.rpc.*;

/**
 * Here we define a simple FeedItem class 
 * feel free to extend (or get rid of) it
 * as you like
 * 
 * @author jordan
 *
 */

public class TechnoratiItem implements IsSerializable, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6204374867908960813L;
	public String name = "";
	public String link = "";
	public String author = "";
	public String descr = "";

	public TechnoratiItem() {}

	public TechnoratiItem(String name, String link, String author, String descr) {
		this.name = name;
		this.link = link;
		this.author = author;
		this.descr = descr;
	}

	public String toString() {
		return "name=" + name + ", link=" + link + ", author=" + author + ", descr=" + descr;
	}
}
