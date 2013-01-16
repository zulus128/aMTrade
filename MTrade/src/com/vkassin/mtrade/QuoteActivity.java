package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class QuoteActivity extends Activity {

	private static final String TAG = "MTrade.QuoteActivity"; 

	private ListView list;
	private QuoteAdapter adapter;
//	private listviewAdapter adapter;

	private static final int CONTEXTMENU_PUTORDER = 1;
	private int selectedRowId;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quotes);
        
        Common.quoteActivity = this;
        
        list = (ListView)this.findViewById(R.id.QuoteList);
    	adapter = new QuoteAdapter(this, R.layout.quotesitem, new ArrayList<Quote>());
//    	adapter = new listviewAdapter(this, new ArrayList<HashMap<String,String>>());

    	list.setAdapter(adapter);
    	registerForContextMenu(list);
    	
       	list.setOnItemClickListener(new OnItemClickListener() {
			
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    				
    	    	    list.showContextMenuForChild(arg1);

    			}
    		});

	}

	public void refresh() {
		
	      if(Common.FIRSTLOAD_FINISHED) {
	      	
        	adapter.setItems(Common.getSelectedInstrument().getQuotes());
	  		adapter.notifyDataSetChanged();
	        }

	}
	
    @Override
    public void onResume() {
    	
      super.onResume();
      Log.i(TAG, "onResume");
      
      Common.tabActivity.setTitle(Common.getSelectedInstrument().symbol);
      
      refresh();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            
//        	Log.d(this.getClass().getName(), "back button pressed");
        	
	    	Common.tabHost.getTabWidget().getChildAt(0).setVisibility(View.VISIBLE);
	    	Common.tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);
	    	Common.tabHost.setCurrentTab(0);
	    	return true;

        }
        return super.onKeyDown(keyCode, event);
    }
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		
	    AdapterView.AdapterContextMenuInfo info =
            (AdapterView.AdapterContextMenuInfo) menuInfo;

	    selectedRowId = (int)info.id;
	    Log.i(TAG, "Selected " + selectedRowId);
		menu.setHeaderTitle(R.string.MenuTitle);  
	    menu.add(0, CONTEXTMENU_PUTORDER, 0, R.string.MenuItemPutOrder);

	    super.onCreateContextMenu(menu, v, menuInfo);  

	}  
	
   @Override  
   public boolean onContextItemSelected(MenuItem item) {  
		   
	    if (item.getItemId() == CONTEXTMENU_PUTORDER) {

	      	Quote q = Common.getSelectedInstrument().getQuotes().get(selectedRowId);
	    	Common.putOrder(this, q);


	    }
	    return true;  
   }

   @Override
   public void onPause() {
   	
   	super.onPause();

   	Common.tabActivity.setTitle(getResources().getString(R.string.app_name));
   	
   }

   @Override
   public void onStart() {
   	
     super.onStart();
//     Log.i(TAG, "--- onStart ");// + isApplicationBroughtToBackground(this));
     
     Common.activities++;
   }
   
  	@Override
   public void onStop() {
   
   	super.onStop();
   	
//		Log.e(TAG, "++++++++++++ onStop");

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
