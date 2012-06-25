package com.vkassin.mtrade;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class InstrActivity extends Activity {

	private static final String TAG = "MTrade.InstrActivity"; 
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
        JSONObject json = new JSONObject();
        try{
        json.put("time", Calendar.getInstance().getTimeInMillis());
        json.put("objType", new Integer(Common.INSTRUMENT));
        
        }
        catch(Exception e){
            e.printStackTrace();
            Log.(TAG, "Error! Cannot Estabilish Connection", e);
        }
}
