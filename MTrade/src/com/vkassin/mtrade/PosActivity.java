package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class PosActivity extends Activity {

	private static final String TAG = "MTrade.PosActivity"; 

	private ListView list;
	private PosAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positions);
        
        Common.posActivity = this;
        
        list = (ListView)this.findViewById(R.id.PosList);
    	adapter = new PosAdapter(this, R.layout.positem, new ArrayList<Position>());
    	list.setAdapter(adapter);

	}

	public void refresh() {

		if(Common.FIRSTLOAD_FINISHED) {

			adapter.setItems(Common.getAllPositions());
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
