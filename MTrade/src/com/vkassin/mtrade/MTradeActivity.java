package com.vkassin.mtrade;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

public class MTradeActivity extends TabActivity {
	
	private static final String TAG = "MTrade.MTradeActivity"; 

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Common.tabActivity = this;
        Common.app_ctx = getApplicationContext();
        
	    Resources res = getResources(); // Resource object to get Drawables
	    Common.tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    intent = new Intent().setClass(this, InstrActivity.class);
	    Common.tabspec = Common.tabHost.newTabSpec("t_instr").setIndicator("Инструменты",
	                      res.getDrawable(R.drawable.ic_menu_goto))
	                  .setContent(intent);
	    Common.tabHost.addTab(Common.tabspec);

	    intent = new Intent().setClass(this, QuoteActivity.class);
	    Common.tabspec = Common.tabHost.newTabSpec("t_quote").setIndicator("Котировки",
	                      res.getDrawable(R.drawable.ic_menu_goto))
	                  .setContent(intent);
	    Common.tabHost.addTab(Common.tabspec);

	    intent = new Intent().setClass(this, ChartActivity.class);
	    spec = Common.tabHost.newTabSpec("t_chart").setIndicator("Графики",
	                      res.getDrawable(R.drawable.chart))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, PosActivity.class);
	    spec = Common.tabHost.newTabSpec("t_trade").setIndicator("Портфель",
	                      res.getDrawable(R.drawable.ic_menu_friendslist))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HistoryActivity.class);
	    spec = Common.tabHost.newTabSpec("tcab").setIndicator("История",
	                      res.getDrawable(R.drawable.ic_menu_show_list))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);
	    
    	Common.tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);

		Common.paused1 = false;

    }
    
//	@Override
//	protected  
//	 void onUserLeaveHint() {
//		
//		Log.e(TAG, "++++++ onUserLeaveHint()");
//	}
    
//    @Override
// 	protected void onRestart() {
// 		// TODO Auto-generated method stub
// 		super.onRestart();
// 		
// 		Log.e(TAG, "++++++++++++ onRestart");
//     	Common.login(this);
//
// 	}

    @Override
    protected void onPause() {
        super.onPause();
        
 		Log.e(TAG, "--++++++++++++ onPause");
        
 		Common.paused = true;
//  		Common.confChanged1 = false;
 	      Common.confChanged1 = !Common.confChanged1;

    }

    @Override
    protected void onResume() {
        super.onResume();
        
 		Log.e(TAG, "///////////////////--++++++++++++ onResume " + Common.confChanged1 + " onPause = " + Common.paused);
        
    	Common.loadAccountDetails();
    	
      	if(Common.confChanged1) {
      		
//      		Common.confChanged1 = false;
      	}
      	else {
      		
      		if(Common.paused)
      			Common.login(this);
//      		Common.confChanged1 = false;
            Common.paused = false;
    		Common.paused1 = false;

      	}

        Common.confChanged1 = !Common.confChanged1;

    }
    
    @Override
    public void onDestroy() {
    	
      super.onDestroy();

      Log.e(TAG, "///////////////++++++++++++ onDestroy");
    }
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
	  
      Log.e(TAG, "////////////////++++++++++++ ConfigurationChanged to " + !Common.confChanged1);
      
      Common.confChanged1 = !Common.confChanged1;

	}
}