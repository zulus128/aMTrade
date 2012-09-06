package com.vkassin.mtrade;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ChartActivity extends Activity {

	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        ChartView mView = new ChartView(this);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(mView);
	    }
}
