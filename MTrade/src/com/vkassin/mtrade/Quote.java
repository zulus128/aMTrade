package com.vkassin.mtrade;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Quote {

//	private static final long serialVersionUID = 24L;
	private static final String TAG = "MTrade.Quote"; 

	public String id = "";

	public Double price = Double.valueOf(0);
	public Long qtySell = Long.valueOf(0);
	public Long qtyBuy = Long.valueOf(0);
	public Long instrId = Long.valueOf(0);

	public Quote(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Quote created id:" + i);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "quote " + id + " updated.");
		
	}
	
	private void set(JSONObject obj){

		try{ this.price = Double.parseDouble(obj.getString("price")); }catch(JSONException e){ }
		try{ this.qtyBuy = obj.getLong("qtyBuy"); }catch(JSONException e){ }
		try{ this.qtySell = obj.getLong("qtySell"); }catch(JSONException e){ }
		try{ this.instrId = obj.getLong("instrId"); }catch(JSONException e){ }
	}
	
}
