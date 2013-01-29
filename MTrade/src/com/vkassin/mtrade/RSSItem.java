package com.vkassin.mtrade;

import java.io.Serializable;
import java.util.Date;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 122L;

	public String title;
	public Date pubDate;
	public String clink;

	public RSSItem() {

		this.title = "";
		this.clink = "";
		this.pubDate = new Date(Date.UTC(110, 0, 0, 0, 0, 0));
	}

	public String getShortTitle() {
//		return title.length() > 148 ? title.substring(0, 145) + "..." : title;// + "                                                                                                                                                                                                                                                                                                                                                          ";
		return title;
	}

	public Date getPubDate() {
		return pubDate == null ? new Date(Date.UTC(110, 0, 0, 0, 0, 0)) : pubDate;
	}
}