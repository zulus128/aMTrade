package com.vkassin.mtrade;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class ChartActivity extends Activity {

	private int m = 12;
	
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        Common.chartActivity = this;
	        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        
	        
	        refresh();
	    }
	   
		public void refresh() {

			if(Common.FIRSTLOAD_FINISHED) {

				ChartView mView = new ChartView(this, m);
				
//				LinearLayout header = (LinearLayout) findViewById(R.id.ggglay);
		    	LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	LinearLayout header = (LinearLayout)vi.inflate(R.layout.grr, null);

				header.addView(mView);

		      	Button custom3 = (Button)header.findViewById(R.id.grbutton3);
		    	custom3.setOnClickListener(new Button.OnClickListener(){
		    		 public void onClick(View arg0) {
		    			 
		    			 m = 3;
		    			 refresh();
		    		 }
		    		    
		    	});

		    	Button custom6 = (Button)header.findViewById(R.id.grbutton6);
		    	custom6.setOnClickListener(new Button.OnClickListener(){
		    		 public void onClick(View arg0) {
		    			 
		    			 m = 6;
		    			 refresh();
		    		 }
		    		    
		    	});

		      	Button custom12 = (Button)header.findViewById(R.id.grbutton12);
		    	custom12.setOnClickListener(new Button.OnClickListener(){
		    		 public void onClick(View arg0) {
		    			 
		    			 m = 12;
		    			 refresh();
		    		 }
		    		    
		    	});

		        setContentView(header);
//		        setContentView(mView);
		        
			}
			
		}
}
