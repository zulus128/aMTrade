package com.vkassin.mtrade;

import android.app.Activity;
import android.os.Bundle;
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

}
