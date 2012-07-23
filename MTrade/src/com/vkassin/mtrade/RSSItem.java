package com.vkassin.mtrade;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vkassin.mtrade.Common.item_type;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 22L;
	private static final String TAG = "MTrade.RSSItem"; 

	private int id;
	
	public String symbol;
	public String description;
	
//	public RSSItem(item_type t) {
//		
//		this.symbol = "";
//		this.description = "";
//	}

	public RSSItem(int i, JSONObject obj) throws JSONException {
		
		this.id = i;
		this.description = obj.getString("descRu");
		this.symbol = obj.getString("symbol");
		
//		Log.i(TAG, symbol + " : " + description);
		
	}
		
	public String getShortContent() {
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

}
