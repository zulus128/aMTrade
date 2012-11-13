package com.vkassin.mtrade;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Position implements Serializable {

	private static final long serialVersionUID = 29L;
	private static final String TAG = "MTrade.Position"; 

	public String id = "";
	public String symbol = "";
	public String acc_code = "";
	public Double currPos = Double.valueOf(0);
	public Double buyPlanPos = Double.valueOf(0);
	public Double sellPlanPos = Double.valueOf(0);
	public Double restPos = Double.valueOf(0);
	public Double inPos = Double.valueOf(0);


	public Position(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Position created id:" + i);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "position " + id + " updated.");
		
	}
	
	private void set(JSONObject obj) {

		try{ this.symbol = obj.getString("instrum"); }catch(JSONException e){ }
		try{ this.acc_code = obj.getString("code"); }catch(JSONException e){ }
		try{ this.currPos = obj.getDouble("currPos"); }catch(JSONException e){ }
		try{ this.buyPlanPos = obj.getDouble("buyPlanPos"); }catch(JSONException e){ }
		try{ this.sellPlanPos = obj.getDouble("sellPlanPos"); }catch(JSONException e){ }
		try{ this.restPos = obj.getDouble("restPos"); }catch(JSONException e){ }
		try{ this.inPos = obj.getDouble("inPos"); }catch(JSONException e){ }

	}

}
