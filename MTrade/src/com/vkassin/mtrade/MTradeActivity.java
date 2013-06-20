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
    	int i = Common.app_ctx.getResources().getIdentifier("TabCaption1", "string", Common.app_ctx.getPackageName());
	    Common.tabspec = Common.tabHost.newTabSpec("t_instr").setIndicator(Common.app_ctx.getResources().getString(i),
                res.getDrawable(android.R.drawable.ic_menu_edit))
            .setContent(intent);
    	Common.tabHost.addTab(Common.tabspec);

	    intent = new Intent().setClass(this, QuoteActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption2", "string", Common.app_ctx.getPackageName());
	    Common.tabspec = Common.tabHost.newTabSpec("t_quote").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_edit))
	                  .setContent(intent);
	    Common.tabHost.addTab(Common.tabspec);

	    intent = new Intent().setClass(this, ChartActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption3", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("t_chart").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_gallery))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, PosActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption4", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("t_trade").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_agenda))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, ArchiveActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption4_1", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("t_arctrade").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_agenda))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HistoryActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption5", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("tcab").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_sort_by_size))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);

	    intent = new Intent().setClass(this, MessageActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption6", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("t_mess").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_upload))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, NewsActivity.class);
    	i = Common.app_ctx.getResources().getIdentifier("TabCaption7", "string", Common.app_ctx.getPackageName());
	    spec = Common.tabHost.newTabSpec("t_mess").setIndicator(Common.app_ctx.getResources().getString(i),
	                      res.getDrawable(android.R.drawable.ic_menu_compass))
	                  .setContent(intent);
	    Common.tabHost.addTab(spec);
	    
    	Common.tabHost.getTabWidget().getChildAt(1).setVisibility(View.GONE);

    	Common.tabHost.getTabWidget().getChildAt(2).setVisibility(View.GONE);
//    	Common.tabHost.getTabWidget().getChildAt(2).setEnabled(false);

    	Common.tabHost.getTabWidget().getChildAt(4).setVisibility(View.GONE);

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
        
// 		Log.e(TAG, "--++++++++++++ onPause");
        
 		Common.paused = true;
//  		Common.confChanged1 = false;
 	      Common.confChanged1 = !Common.confChanged1;

    }

    @Override
    protected void onResume() {
        super.onResume();
        
// 		Log.e(TAG, "///////////////////--++++++++++++ onResume " + Common.confChanged1 + " onPause = " + Common.paused);
        
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

//      Log.e(TAG, "///////////////++++++++++++ onDestroy");
    }
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
	  
//      Log.e(TAG, "////////////////++++++++++++ ConfigurationChanged to " + !Common.confChanged1);
      
      Common.confChanged1 = !Common.confChanged1;

	}
}