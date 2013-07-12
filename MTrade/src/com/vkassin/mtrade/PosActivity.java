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
import android.widget.ProgressBar;

public class PosActivity extends Activity {

	private static final String TAG = "MTrade.PosActivity"; 

	private ListView list;
	private PosAdapter adapter;
	private static final int CONTEXTMENU_GOARCHIVE = 1;
	private int selectedRowId;
	private ProgressBar pb;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.positions);
        
        Common.posActivity = this;
        
        list = (ListView)this.findViewById(R.id.PosList);
    	adapter = new PosAdapter(this, R.layout.positem, new ArrayList<Position>());
    	list.setAdapter(adapter);

    	registerForContextMenu(list);

    	pb = (ProgressBar)findViewById(R.id.ProgressBarArc01);

//    	refresh();
	}

	public void refresh() {

//		if(Common.FIRSTLOAD_FINISHED) {

			adapter.setItems(Common.getAllPositions());
	  		adapter.notifyDataSetChanged();
	        
//			adapter.getFilter().filter("KZTKp");

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
    			pb.setVisibility(View.VISIBLE);

	            break;
	    }
	    return true;
	}

	public void hideProgressBar() {

		pb.setVisibility(View.GONE);

	}
	
    @Override
    public void onResume() {
    	
      super.onResume();
//      Log.e(TAG, "++++++++onResume");
      
      refresh();

    }
    
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		
		AdapterView.AdapterContextMenuInfo info =
	            (AdapterView.AdapterContextMenuInfo) menuInfo;

		    selectedRowId = (int)info.id;
			Position p =  adapter.getItems().get(selectedRowId);
			Log.i(TAG, "ppp = " + p.symbol);

			if(p.symbol.equals("KZT"))
				return;
			
		menu.add(0, CONTEXTMENU_GOARCHIVE, 0, R.string.MenuItemGoArchive);
	    
		super.onCreateContextMenu(menu, v, menuInfo);  

		
		Common.arcfilter = p.symbol;
		menu.setHeaderTitle(p.symbol);  

	}
    
    @Override  
    public boolean onContextItemSelected(MenuItem item) {  
 		   

 	   if (item.getItemId() == CONTEXTMENU_GOARCHIVE) {
 	    	

//	    	Common.arcfilter = "KZTK";

	    	Common.tabHost.getTabWidget().getChildAt(3).setVisibility(View.GONE);
 	    	Common.tabHost.getTabWidget().getChildAt(4).setVisibility(View.VISIBLE);
 	    	Common.tabHost.setCurrentTab(4);


// 	    	refresh();

 	    }
 	    
 	    return true;  
    }  

}
