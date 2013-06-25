package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class ArchiveActivity extends Activity{

	private static final String TAG = "MTrade.ArchiveActivity"; 

	private ListView list;
	public ArcAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
        
        Common.arcActivity = this;

        list = (ListView)this.findViewById(R.id.ArchiveList);
    	adapter = new ArcAdapter(this, R.layout.archiveitem, new ArrayList<Deal>());
    	list.setAdapter(adapter);


  		refresh();
    	
	}
	
	public void refresh() {

//		if(Common.FIRSTLOAD_FINISHED) {

	        
//		adapter.setItems(Common.getAllArcDeals());
		adapter.setItems(Common.getArcDealsWithFilter());
  		adapter.notifyDataSetChanged();

//	  		adapter.getFilter().filter(Common.arcfilter);
//		}
		
	}
	
	  @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {    	
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	            
		    	Common.tabHost.getTabWidget().getChildAt(3).setVisibility(View.VISIBLE);
		    	Common.tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);
		    	Common.tabHost.setCurrentTab(3);
		    	return true;

	        }
	        return super.onKeyDown(keyCode, event);
	    }
	  
    @Override
    public void onResume() {
    	
      super.onResume();
//      Log.e(TAG, "++++++++onResume");
      
//	adapter.getFilter().filter("KZTKp");

      refresh();

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.arcdealmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	        case R.id.menuarcdeal: 
	        	
	        	Common.putArcDeal(this);
	            break;
	    }
	    return true;
	}

}
