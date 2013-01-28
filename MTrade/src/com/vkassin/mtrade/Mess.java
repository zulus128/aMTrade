package com.vkassin.mtrade;

import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Mess implements Serializable {

	private static final long serialVersionUID = 99L;
	private static final String TAG = "MTrade.Message"; 

	public String id = "";
	public String from = "";
	public String msg = "";
	public String time = "";

	public Mess(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Message created id:" + i);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "message " + id + " updated.");
		
	}
	
	private void set(JSONObject obj) {

		try{ this.from = obj.getString("from"); }catch(JSONException e){ }
		try{ this.msg = obj.getString("msg"); }catch(JSONException e){ }
		try{ this.time = obj.getString("time"); }catch(JSONException e){ }

	}

}
