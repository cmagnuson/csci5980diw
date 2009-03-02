package com.l33tsp33k.client.datamodels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TwitterItem implements IsSerializable, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6992873494030300174L;
	public String name = "";
	public String link = "";
	public String author = "";
	public String descr = "";

	public TwitterItem() {}

	public TwitterItem(String name, String link, String author, String descr) {
		this.name = name;
		this.link = link;
		this.author = author;
		this.descr = descr;
	}

	public String getUsername() {
		return author.split(" ")[0];
	}

	public String toString() {
		return "name=" + name + ", link=" + link + ", author=" + author + ", descr=" + descr;
	}
}
