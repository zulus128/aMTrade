package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public class NewsActivity extends Activity {

	private static final String TAG = "MTrade.NewsActivity";

	private ListView list;
	private NewsArrayAdapter adapter;
	private ProgressBar pb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news);

		list = (ListView) this.findViewById(R.id.NewsList);
		pb = (ProgressBar) findViewById(R.id.ProgressBar01);

		adapter = new NewsArrayAdapter(this, R.layout.newsitem,
				new ArrayList<RSSItem>());
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// Common.news = adapter.getItems();
				Common.curnews = adapter.getItems().get(arg2);
				Intent i = new Intent(NewsActivity.this, NewsDetail.class);
				NewsDetail.prepare();
				startActivity(i);
				// Log.i(TAG, "row: "+arg2+" arg3: "+arg3);
			}
		});

		// Locale locale = new Locale("us");
		// Locale.setDefault(locale);
		// Configuration config = new Configuration();
		// config.locale = locale;
		// getBaseContext().getResources().updateConfiguration(config,
		// getBaseContext().getResources().getDisplayMetrics());

//		refresh();
	}
    
	@Override
    public void onResume() {
    	
      super.onResume();
//      Log.e(TAG, "++++++++onResume");
      
      refresh();

    }
    
	private void refresh() {

		pb.setVisibility(View.VISIBLE);

		new getRSS().execute();

		// NewsDetail.resetViewsList();

	}
	
	private class getRSS extends AsyncTask<Context, Integer, ArrayList<RSSItem>> {

    	@Override
		protected ArrayList<RSSItem> doInBackground(Context... params) {
    					
    		ArrayList<RSSItem> rssItems = Common.getNews();
            return rssItems;
		}
    	
        protected void onProgressUpdate(Integer... progress) {
        //    ProgressBar mProgress = (ProgressBar)NewsActivity.this.findViewById(R.id.progressBar1);
        //    mProgress.setProgress(progress[0]);
        }

        protected void onPostExecute(final ArrayList<RSSItem> result) {

    		pb.setVisibility(View.GONE);
    		
//    		String sformat = "Обновлено MM.dd в HH:mm";
//    		
//    		Calendar cal = Calendar.getInstance();
//    		SimpleDateFormat sdf = new SimpleDateFormat(sformat);
//    		refreshText.setText(sdf.format(cal.getTime()));
        	adapter.setItems(result);
			adapter.notifyDataSetChanged();
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.newsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.refreshitem: {

	        	this.refresh();
                break;
	        }
	    }
	    return true;
	}    	
}
