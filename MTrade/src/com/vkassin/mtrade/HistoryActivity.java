package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class HistoryActivity extends Activity {

	private static final String TAG = "MTrade.HistoryActivity"; 

	private ListView list;
	private HistoryAdapter adapter;

	private int filter = 3;
	
	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        
      	Button custom1 = (Button)findViewById(R.id.hbutton1);
    	custom1.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
    			 filter = 1;
    			 refresh1();
    		 }
    		    
    	});
        
      	Button custom2 = (Button)findViewById(R.id.hbutton2);
    	custom2.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
    			 filter = 2;
    			 refresh1();
    		 }
    		    
    	});
        
      	Button custom3 = (Button)findViewById(R.id.hbutton3);
    	custom3.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
    			 filter = 3;
    			 refresh1();
    		 }
    		    
    	});
        
        Common.historyActivity = this;
        
        list = (ListView)this.findViewById(R.id.HistoryList);
    	adapter = new HistoryAdapter(this, R.layout.historyitem, new ArrayList<History>());
    	list.setAdapter(adapter);

	}

	public void refresh1() {

		switch(filter) {
      	
      	case 1: 	//list.setTextFilterEnabled(true);  
      					adapter.getFilter().filter("transit");
		  				break;
      	
      	case 2: 	//list.setTextFilterEnabled(true);  
      					adapter.getFilter().filter("deal");
		  				break;
      	
      	default:  	//list.setTextFilterEnabled(false);
      					adapter.getFilter().filter("");
		  				break;
      	}
//      	adapter.getFilter().filter("transit");
		
	}
	
	public void refresh() {

		if(Common.FIRSTLOAD_FINISHED) {
			
//	      	Log.i(TAG, "History count = " + Common.getAllHistory().size());
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
    
    @Override
    public void onStart() {
    	
      super.onStart();
//      Log.i(TAG, "--- onStart ");// + isApplicationBroughtToBackground(this));
      
      Common.activities++;
    }
    
   	@Override
    public void onStop() {
    
    	super.onStop();
    	
// 		Log.e(TAG, "++++++++++++ onStop");

 		Common.activities--;
    }

   	@Override
   	protected void onRestart() {
   		// TODO Auto-generated method stub
   		super.onRestart();
   		
//   		Log.e(TAG, "++++++++++++ onRestart " + Common.activities);
    
   		if(Common.activities == 0)
   			Common.login(this);

   	}

}
