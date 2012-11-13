package com.vkassin.mtrade;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

public class MTradeActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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

    }
}