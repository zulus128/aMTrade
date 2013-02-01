package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class SelectListView extends ListActivity {

	// private String[] lv_arr = {};
	private ListView mainListView = null;
	private ArrayList<Instrument> listInstr;
	// SelectAdapter<String> adapter;
	private SelectInstrAdapter adapter;

	private static final String TAG = "MTrade.SelectListView";
	private EditText filterText = null;

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {

			// LoadSelections();

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

			SaveSelections();
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			// Log.i(TAG, "b4 filter count = " + adapter.getCount());
			adapter.getFilter().filter(s);
			// Log.i(TAG, "ar filter count = " + adapter.getCount());

		}

	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		View v = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);
		View w = getCurrentFocus();
		int scrcoords[] = new int[2];
		w.getLocationOnScreen(scrcoords);
		float x = event.getRawX() + w.getLeft() - scrcoords[0];
		float y = event.getRawY() + w.getTop() - scrcoords[1];

		// Log.d("Activity",
		// "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
		if (event.getAction() == MotionEvent.ACTION_UP
				&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
						.getBottom())) {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
					.getWindowToken(), 0);
		}
		return ret;
	}

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
		// filterText.setInputType(android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		// ArrayList<String> a = new ArrayList<String>();
		// Iterator<Instrument> itr = listInstr.iterator();
		//
		// while (itr.hasNext()) {
		//
		// Instrument k = itr.next();
		// a.add(k.symbol);
		// }
		//
		// lv_arr = (String[])a.toArray(new String[0]);
		// adapter = new SelectAdapter<String>(SelectListView.this,
		// android.R.layout.simple_list_item_multiple_choice, lv_arr);

		adapter = new SelectInstrAdapter(this, R.layout.selectitem,
				new ArrayList<Instrument>());

		// adapter.slv = this;

		mainListView.setAdapter(adapter);

		// mainListView.setItemsCanFocus(true);
		// mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		adapter.setItems(Common.getAllInstrs());
		LoadSelections();

		Common.paused = false;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		ViewGroup row = (ViewGroup) v;
		CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
		check.toggle();
		Instrument ins = adapter.getItem(position);
		ins.favourite = !ins.favourite;
		Log.i(TAG, ins.symbol + " " + ins.favourite);
	}

	@Override
	protected void onPause() {
		// always handle the onPause to make sure selections are saved if user
		// clicks back button

		// Log.w(TAG, "onPause");

		super.onPause();

		SaveSelections();

		Common.paused1 = true;
		Log.e(TAG, "--++++++++++++ onPause");

	}

	private void ClearSelections() {

		int count = this.mainListView.getAdapter().getCount();

		for (int i = 0; i < count; i++) {
//			this.mainListView.setItemChecked(i, false);
			Instrument ins = adapter.getItem(i);
			ins.favourite = false;
		}

		SaveSelections();

		adapter.notifyDataSetChanged();
	}

	public void LoadSelections() {

		// listInstr = Common.getAllInstrs();

//		int count = this.mainListView.getAdapter().getCount();
//		Log.i(TAG, "load count = " + count);
//
//		for (int y = 0; y < listInstr.size(); y++) {
//
//			if (listInstr.get(y).favourite)
//				Log.i(TAG, "LoadSelection favourite: "
//						+ listInstr.get(y).symbol);
//		}
//
//		for (int i = 0; i < count; i++) {
//
//			Object it = this.mainListView.getAdapter().getItem(i);
//			// long p = this.mainListView.getAdapter().getItemId(i);
//
//			for (int y = 0; y < listInstr.size(); y++) {
//
//				if (listInstr.get(y).symbol.equals(it))
//					this.mainListView.setItemChecked(i,
//							listInstr.get(y).favourite);
//			}
//
//			// Log.i(TAG, " p = " + p + " " + i);
//		}

//		for (int i = 0; i < listInstr.size(); i++) {
//
//			ViewGroup row = (ViewGroup) this.mainListView.getItemAtPosition(i);
//			CheckBox check = (CheckBox) row.findViewById(R.id.checkbox);
//			Instrument ins = adapter.getItem(i);
//			check.setChecked(ins.favourite);
//				
//		}

	}

	public void SaveSelections() {

//		HashSet<String> a = Common.getFavrList();// new HashSet<String>();
//		int count = this.mainListView.getAdapter().getCount();
//
//		for (int i = 0; i < count; i++) {
//
//			Object it = this.mainListView.getAdapter().getItem(i);
//
//			for (int y = 0; y < listInstr.size(); y++) {
//
//				if (listInstr.get(y).symbol.equals(it)) {
//
//					if (this.mainListView.isItemChecked(i)) {
//
//						a.add(listInstr.get(y).id);
//						Log.w(TAG, "add = " + listInstr.get(y).symbol);
//					} else
//						a.remove(listInstr.get(y).id);
//				}
//			}
//
//		}

		HashSet<String> a = Common.getFavrList();// new HashSet<String>();
		for (Instrument instr : listInstr) {

			if(instr.favourite) {
				
					a.add(instr.id);
					Log.w(TAG, "add = " + instr.symbol);
			}
			else
				a.remove(instr.id);
		}
		
		
		Common.setFavrList(a);

		// Log.w(TAG, "favr = " + Common.getFavrList());

		Common.saveFavrList();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		filterText.removeTextChangedListener(filterTextWatcher);

	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.e(TAG, "--++++++++++++ onResume");

		Common.loadAccountDetails();

		if (Common.confChanged1) {

			// Common.confChanged1 = false;
		} else {

			if (Common.paused1)
				Common.login(Common.mainActivity);
		}

		Common.paused1 = false;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		Log.e(TAG, "++++++++++++ ConfigurationChanged");

		// Common.confChanged1 = true;

	}
}
