package com.vkassin.mtrade;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Quote implements Comparable<Quote> {

//	private static final long serialVersionUID = 24L;
	private static final String TAG = "MTrade.Quote"; 

	public String id = "";
	
	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {
		
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}

	private Double price = Double.valueOf(0);
	public Long qtySell = Long.valueOf(0);
	public Long qtyBuy = Long.valueOf(0);
	public Long instrId = Long.valueOf(0);

	public int compareTo(Quote arg0) {

		if(this.price < arg0.price)
	    {
	      /* текущее меньше полученного */
	      return 1;
	    }   
	    else if(this.price > arg0.price)
	    {
	      /* текущее больше полученного */
	      return -1;
	    }
	    /* текущее равно полученному */
	    return 0;  
	    
	}
	
	public Quote(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, " --/ Quote created id:" + i + ", price = " + price);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "quote " + id + ", price = " + price + " updated.");
		
	}
	
	private void set(JSONObject obj){

		try{ this.price = Double.parseDouble(obj.getString("price")); }catch(JSONException e){ }
		try{ this.qtyBuy = obj.getLong("qtyBuy"); }catch(JSONException e){ }
		try{ this.qtySell = obj.getLong("qtySell"); }catch(JSONException e){ }
		try{ this.instrId = obj.getLong("instrId"); }catch(JSONException e){ }
	}
	
	public String getPriceS() {
		
		return twoDForm.format(price);
	}
}
