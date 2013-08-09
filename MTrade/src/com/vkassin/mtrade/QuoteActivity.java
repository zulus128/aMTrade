package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
		
//	      if(Common.FIRSTLOAD_FINISHED) {
	      	
	    	ArrayList<Quote> qq = Common.getSelectedInstrument().getQuotes();
        	adapter.setItems(qq);
        	
        	adapter.notifyDataSetChanged();
        	
        	setPos();
	}

	public void setPos() {
	
    	int c = 0;
    	for(Quote q: adapter.getItems())
    		if(q.qtySell > 0)
    			c++;
    	final int pos = ((c - 3) < 0)?0:(c - 3);
//   		list.setSelection(pos);
   		
//   		list.smoothScrollToPosition(pos);
//  		Log.i(TAG, "---pos = " + pos);

//    	list.clearFocus();
    	list.post(new Runnable() {
    		
    	    public void run() {
//    	        list.requestFocusFromTouch();
    	        list.setSelection(pos);
//    	        list.requestFocus();
    	    }
    	});
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

	String s = getResources().getString(R.string.app_name);
	PackageInfo pInfo;
	try {
		pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
    	s += " " + pInfo.versionName;
	} catch (NameNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
   	Common.tabActivity.setTitle(s);
   	
   }

}
