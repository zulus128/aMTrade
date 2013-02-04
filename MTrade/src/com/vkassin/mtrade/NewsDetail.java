package com.vkassin.mtrade;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetail extends Activity {

	private static final String TAG = "MTrade.NewsDetail"; 

//	private static boolean page1;

	public static void prepare() {

		Common.paused1 = false;
//		page1 = false;
	}

	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);

		WebView engine = (WebView) findViewById(R.id.web_detail);
		engine.setWebViewClient(new HelloWebViewClient());
		engine.getSettings().setJavaScriptEnabled(true);
		engine.loadUrl(Common.curnews.clink);

 		Common.paused = false;

    }
 

	private class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	
	    	
	    	if(url.endsWith(".pdf")) {
	    	
	    		if (!url.startsWith("http://") && !url.startsWith("https://"))
	    		   url = "http://" + url;
	    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	    		startActivity(browserIntent);
	    	
	    		return true;
	    	}
	    	else {
	    		
	    		view.loadUrl(url);
	    		return true;
	    		
	    	}
	    	
	    }

	}

	@Override
	protected void onPause() {
		super.onPause();

		Common.paused1 = true;
//		Log.e(TAG, "--++++++++++++ onPause");

	}
	
	@Override
	protected void onResume() {
		super.onResume();

//		Log.e(TAG, "--++++++++++++ onResume");

		if (Common.confChanged1) {

			// Common.confChanged1 = false;
		} else {

			if (Common.paused1)
				Common.login(this);
		}

		Common.paused1 = false;
		Common.paused = false;

	}
}
