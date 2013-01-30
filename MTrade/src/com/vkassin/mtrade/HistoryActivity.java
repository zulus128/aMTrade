package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryActivity extends Activity {

	private static final String TAG = "MTrade.HistoryActivity";

	private ListView list;
	private HistoryAdapter adapter;

	private static final int CONTEXTMENU_DELETE = 1;
	private static final int CONTEXTMENU_TRANSIT = 2;
	private int selectedRowId;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		Button custom1 = (Button) findViewById(R.id.hbutton1);
		custom1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				Common.historyFilter = 1;
				refresh1();
			}

		});

		Button custom2 = (Button) findViewById(R.id.hbutton2);
		custom2.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				Common.historyFilter = 2;
				refresh1();
			}

		});

		Button custom3 = (Button) findViewById(R.id.hbutton3);
		custom3.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				Common.historyFilter = 3;
				refresh1();
			}

		});

		Common.historyActivity = this;

		list = (ListView) this.findViewById(R.id.HistoryList);
		adapter = new HistoryAdapter(this, R.layout.historyitem,
				new ArrayList<History>());
		list.setAdapter(adapter);

		registerForContextMenu(list);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				String s = ((History) adapter.getItems().get(arg2)).getStatus();
				Toast.makeText(HistoryActivity.this, s, Toast.LENGTH_LONG)
						.show();

			}
		});

		refresh();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

		selectedRowId = (int) info.id;
		Log.i(TAG, "Selected " + selectedRowId);

		History item = ((History) adapter.getItems().get(selectedRowId));
		if (item.getOperationType().equals("Deal"))
			return;

		menu.setHeaderTitle(R.string.MenuTitle);
		menu.add(0, CONTEXTMENU_TRANSIT, 0, R.string.Transit);

		if (!item.canBeDeleted())
			return;

		menu.add(0, CONTEXTMENU_DELETE, 1, R.string.Delete);

		super.onCreateContextMenu(menu, v, menuInfo);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getItemId() == CONTEXTMENU_TRANSIT) {
		
			History it = ((History) adapter.getItems().get(selectedRowId));
			adapter.getFilter().filter("." + it.getOrdSerial() + ".");

		}
		
		if (item.getItemId() == CONTEXTMENU_DELETE) {

			History it = ((History) adapter.getItems().get(selectedRowId));
			Common.delOrder(this, it);
		}
		return true;
	}

	public void refresh1() {

		switch (Common.historyFilter) {

		case 1: // list.setTextFilterEnabled(true);
			adapter.getFilter().filter("transit");
			break;

		case 2: // list.setTextFilterEnabled(true);
			adapter.getFilter().filter("deal");
			break;

		default: // list.setTextFilterEnabled(false);
			adapter.getFilter().filter("");
			break;
		}
		// adapter.getFilter().filter("transit");

	}

	public void refresh() {

		// if(Common.FIRSTLOAD_FINISHED) {

		// Log.i(TAG, "History count = " + Common.getAllHistory().size());
		adapter.setItems(Common.getAllHistory());

		// adapter.ssort();

		adapter.notifyDataSetChanged();
		// }

		refresh1();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.histmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.histinstr:

			adapter.ssort(Common.SORT_TYPE_INSTR);
			adapter.notifyDataSetChanged();

			break;
		case R.id.histprice:

			adapter.ssort(Common.SORT_TYPE_PRICE);
			adapter.notifyDataSetChanged();

			break;
		case R.id.histstatus:

			adapter.ssort(Common.SORT_TYPE_STATUS);
			adapter.notifyDataSetChanged();

			break;
		case R.id.histdate:

			adapter.ssort(Common.SORT_TYPE_DATE);
			adapter.notifyDataSetChanged();

			break;
		}
		return true;
	}

}
