package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

	public class SelectListView extends ListActivity {

		private String[] lv_arr = {};
		private ListView mainListView = null;
		private ArrayList<Instrument> listInstr;
		SelectAdapter<String> adapter;
		private static final String TAG = "MTrade.SelectListView"; 
		private EditText filterText = null;

		
		private TextWatcher filterTextWatcher = new TextWatcher() {

		    public void afterTextChanged(Editable s) {

//		    	LoadSelections();

		    }

		    public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
		    	
		    	SaveSelections();
		    }

		    public void onTextChanged(CharSequence s, int start, int before,
		            int count) {
		    	
		    	
//		    	Log.i(TAG, "b4 filter count = " + adapter.getCount());
		        adapter.getFilter().filter(s);
//		    	Log.i(TAG, "ar filter count = " + adapter.getCount());
		        
		    }

		};

		
		
		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.select_instr);

			Button btnClear = (Button) findViewById(R.id.btnClear);
			btnClear.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					ClearSelections();
				}
			});

			listInstr = Common.getAllInstrs();

			this.mainListView = getListView();

			mainListView.setCacheColorHint(0);

			filterText = (EditText) findViewById(R.id.search_box);
		    filterText.addTextChangedListener(filterTextWatcher);
//		    filterText.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		    
			ArrayList<String> a = new ArrayList<String>();
			Iterator<Instrument> itr = listInstr.iterator();
				
			while (itr.hasNext()) {
					
					Instrument k = itr.next();
					a.add(k.symbol);
				}
				
			lv_arr = (String[])a.toArray(new String[0]);
			adapter = new SelectAdapter<String>(SelectListView.this,
					android.R.layout.simple_list_item_multiple_choice, lv_arr);
			
			adapter.slv = this;
			
			mainListView.setAdapter(adapter);

			mainListView.setItemsCanFocus(false);
			mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			LoadSelections();
		}

		@Override
		protected void onPause() {
			// always handle the onPause to make sure selections are saved if user clicks back button

			SaveSelections();

//			Log.w(TAG, "onPause");
			
			super.onPause();
		}

		private void ClearSelections() {

			// user has clicked clear button so uncheck all the items

			int count = this.mainListView.getAdapter().getCount();

			for (int i = 0; i < count; i++) {
				this.mainListView.setItemChecked(i, false);
			}

			// also clear the saved selections
			SaveSelections();

		}

		public void LoadSelections() {

			int count = this.mainListView.getAdapter().getCount();
			Log.i(TAG, "load count = " + count);

			for (int i = 0; i < count; i++) {
				
				Object it = this.mainListView.getAdapter().getItem(i);
				long p = this.mainListView.getAdapter().getItemId(i);
				
				for (int y = 0; y < listInstr.size(); y++) {
				
					if(listInstr.get(y).symbol.equals(it)) 
						this.mainListView.setItemChecked(y, listInstr.get(y).favourite);
				}
				
//				Log.i(TAG, " p = " + p + " " + i);
			}

//			for (int i = 0; i < count; i++) {
//					if(listInstr.get(i).favourite) {
//							
//						this.mainListView.setItemChecked(i, true);
//							
//					Log.i(TAG, "checked "+ i + " " + listInstr.get(i).symbol + " " + this.mainListView.getAdapter().getItem(i));
//					
//					}
//					
//				}

		}

		public void SaveSelections() {

			HashSet<String> a = new HashSet<String>();
			int count = this.mainListView.getAdapter().getCount();
			
			for (int i = 0; i < count; i++)
				if (this.mainListView.isItemChecked(i)) {
					//a.add(this.mainListView.getItemAtPosition(i).toString());
					a.add(listInstr.get(i).id);
					Log.w(TAG, "add = " + listInstr.get(i).symbol);

				}
		
			Common.setFavrList(a);
			
//			Log.w(TAG, "favr = " + Common.getFavrList());
			
		
		}

		@Override
		protected void onDestroy() {
		  
			super.onDestroy();
		    
			filterText.removeTextChangedListener(filterTextWatcher);
			
		}
		
	}
