package com.vkassin.mtrade;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vkassin.mtrade.Common.item_type;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 22L;
	private static final String TAG = "MTrade.RSSItem"; 

	public String id;
	public String symbol;
	public String description;

	public Double min;
	public Double max;

	public RSSItem(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
//		Log.i(TAG, symbol + " : " + description);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, symbol + " updated.");
		
	}
	
	private void set(JSONObject obj){

		try{ this.description = obj.getString("descRu"); }catch(JSONException e){ }
		try{ this.symbol = obj.getString("symbol"); }catch(JSONException e){ }
		
		try{ this.min = obj.getDouble("min"); }catch(JSONException e){ }
		try{ this.max = obj.getDouble("max"); }catch(JSONException e){ }
		
	}
	
	public String getShortContent() {
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

}
