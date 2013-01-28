package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class MessageActivity extends Activity {

	private static final String TAG = "MTrade.MessageActivity"; 

	private ListView list;
	private MessageAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        
//        Common.posActivity = this;
        
        list = (ListView)this.findViewById(R.id.MesList);
    	adapter = new MessageAdapter(this, R.layout.mesitem, new ArrayList<Mess>());
    	list.setAdapter(adapter);

	}

	public void refresh() {

//		if(Common.FIRSTLOAD_FINISHED) {

			adapter.setItems(Common.getAllMessages());
	  		adapter.notifyDataSetChanged();
	        
//		}
		
	}
	   
    @Override
    public void onResume() {
    	
      super.onResume();
//      Log.e(TAG, "++++++++onResume");
      
      refresh();

    }
    

}
