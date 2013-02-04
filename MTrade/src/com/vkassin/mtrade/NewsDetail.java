package com.vkassin.mtrade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsDetail extends Activity {


	public static void prepare() {

	}

	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);

		WebView engine = (WebView) findViewById(R.id.web_detail);
		engine.setWebViewClient(new HelloWebViewClient());
//		engine.getSettings().setJavaScriptEnabled(true);
		engine.loadUrl(Common.curnews.clink);

 		Common.paused = false;

    }
 

	private class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
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
