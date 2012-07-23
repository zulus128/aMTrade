package com.vkassin.mtrade;

import java.io.Serializable;

import com.vkassin.mtrade.Common.item_type;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 22L;
	
	public String title;
	public String description;
	
	public RSSItem(item_type t) {
		
		this.title = "";
		this.description = "";
	}
		
	public String getShortContent() {
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

}
