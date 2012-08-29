package com.vkassin.mtrade;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Order implements Serializable {

	private static final long serialVersionUID = 23L;
	private static final String TAG = "MTrade.Order"; 

	public String id = "";
//	public String symbol = "";

	public Double price = new Double(0);
	public Long qty = new Long(0);
	public Long instrId = new Long(0);

	public Order(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Order created id:" + i);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "order " + id + " updated.");
		
	}
	
	private void set(JSONObject obj){

//		try{ this.symbol = obj.getString("symbol"); }catch(JSONException e){ }
		try{ this.price = obj.getDouble("price"); }catch(JSONException e){ }
		try{ this.qty = obj.getLong("quantity"); }catch(JSONException e){ }
		try{ this.instrId = obj.getLong("instrId"); }catch(JSONException e){ }
	}
	
}
