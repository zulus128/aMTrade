package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ArchiveActivity extends Activity{

	private static final String TAG = "MTrade.ArchiveActivity"; 

	private ListView list;
	private ArcAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archive);
        
        Common.arcActivity = this;

        list = (ListView)this.findViewById(R.id.ArchiveList);
    	adapter = new ArcAdapter(this, R.layout.archiveitem, new ArrayList<Deal>());
    	list.setAdapter(adapter);

	}
	
	public void refresh() {

//		if(Common.FIRSTLOAD_FINISHED) {

			adapter.setItems(Common.getAllArcDeals());
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
