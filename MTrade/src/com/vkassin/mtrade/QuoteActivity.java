package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

public class QuoteActivity extends Activity {

	private static final String TAG = "MTrade.QuoteActivity"; 

	private ListView list;
	private QuoteAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes);
        
        list = (ListView)this.findViewById(R.id.QuoteList);
    	adapter = new QuoteAdapter(this, R.layout.quotesitem, new ArrayList<Quote>());
    	list.setAdapter(adapter);

	}

    @Override
    public void onResume() {
    	
      super.onResume();
      Log.i(TAG, "onResume");
      
      if(Common.FIRSTLOAD_FINISHED) {
    	
      	adapter.setItems(Common.selectedInstrument.getQuotes());
		adapter.notifyDataSetChanged();
      }
      
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            
        	Log.d(this.getClass().getName(), "back button pressed");
        	
	    	Common.tabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
	    	Common.tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
	    	Common.tabHost.setCurrentTab(0);
	    	return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
