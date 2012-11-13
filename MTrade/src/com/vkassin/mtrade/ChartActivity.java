package com.vkassin.mtrade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class ChartActivity extends Activity {

	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        Common.chartActivity = this;
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        refresh();
	    }
	   
		public void refresh() {

			if(Common.FIRSTLOAD_FINISHED) {

				ChartView mView = new ChartView(this);
		        setContentView(mView);
		        
			}
			
		}
}
