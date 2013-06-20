package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;

public class PosActivity extends Activity {

	private static final String TAG = "MTrade.PosActivity"; 

	private ListView list;
	private PosAdapter adapter;
	private static final int CONTEXTMENU_GOARCHIVE = 1;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positions);
        
        Common.posActivity = this;
        
        list = (ListView)this.findViewById(R.id.PosList);
    	adapter = new PosAdapter(this, R.layout.positem, new ArrayList<Position>());
    	list.setAdapter(adapter);

    	registerForContextMenu(list);

	}

	public void refresh() {

//		if(Common.FIRSTLOAD_FINISHED) {

			adapter.setItems(Common.getAllPositions());
	  		adapter.notifyDataSetChanged();
	        
//		}
		
	}
	   
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.portmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	        case R.id.menuport: 
	        	
	        	Common.askArcDeals();
	        	
	            break;
	    }
	    return true;
	}

    @Override
    public void onResume() {
    	
      super.onResume();
//      Log.e(TAG, "++++++++onResume");
      
      refresh();

    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		
		menu.setHeaderTitle(R.string.MenuTitle);  
	    menu.add(0, CONTEXTMENU_GOARCHIVE, 0, R.string.MenuItemGoArchive);
	    
		super.onCreateContextMenu(menu, v, menuInfo);  

	}
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
 		   

 	   if (item.getItemId() == CONTEXTMENU_GOARCHIVE) {
 	    	
 		   Common.tabHost.getTabWidget().getChildAt(3).setVisibility(View.GONE);
 	    	Common.tabHost.getTabWidget().getChildAt(4).setVisibility(View.VISIBLE);
 	    	Common.tabHost.setCurrentTab(4);


 	    }
 	    
 	    return true;  
    }  

}
