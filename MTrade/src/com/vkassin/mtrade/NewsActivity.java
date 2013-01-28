package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class NewsActivity extends Activity {

	private static final String TAG = "MTrade.NewsActivity"; 

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
	}
	
}
