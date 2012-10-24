package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
    	
//    	adapter.setItems(Common.getAllOrders());
//		adapter.notifyDataSetChanged();
      }
      
    }
}
