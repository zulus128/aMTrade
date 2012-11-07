package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class HistoryActivity extends Activity {

	private static final String TAG = "MTrade.HistoryActivity"; 

	private ListView list;
	private HistoryAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        
        Common.historyActivity = this;
        
        list = (ListView)this.findViewById(R.id.HistoryList);
    	adapter = new HistoryAdapter(this, R.layout.historyitem, new ArrayList<History>());
    	list.setAdapter(adapter);

	}

	public void refresh() {

		if(Common.FIRSTLOAD_FINISHED) {
	      	Log.i(TAG, "History count = " + Common.getAllHistory().size());
	      	adapter.setItems(Common.getAllHistory());
	  		adapter.notifyDataSetChanged();
	        }
		
	}
	
    @Override
    public void onResume() {
    	
      super.onResume();
      Log.i(TAG, "onResume");
      
      refresh();
      
    }
}
