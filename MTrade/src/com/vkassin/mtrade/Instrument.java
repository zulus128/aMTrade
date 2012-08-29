package com.vkassin.mtrade;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Instrument implements Serializable {

	private static final long serialVersionUID = 22L;
	private static final String TAG = "MTrade.Instrument"; 

	public String id = "";
	public String symbol = "";
	public String description = "";

	public Double min = new Double(0);
	public Double max = new Double(0);

	public Double bid = new Double(0);
	public Double ask = new Double(0);

	public boolean favourite;
	
	public Instrument(String i, JSONObject obj) {
		
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
		
		try{ this.bid = obj.getDouble("bid"); }catch(JSONException e){ }
		try{ this.ask = obj.getDouble("ask"); }catch(JSONException e){ }
	}
	
	public String getShortContent() {
		
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

}
