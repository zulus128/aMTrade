package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;

public class SelectAdapter<T> extends ArrayAdapter <T> {

	private static final String TAG = "MTrade.SelectAdapter"; 

	public SelectListView slv;
	
	public SelectAdapter(Context context, int textViewResourceId, T[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
	    
	    Log.i(TAG, "notifyDataSetChanged()");
//	    slv.SaveSelections();
	    slv.LoadSelections();
	}
}
