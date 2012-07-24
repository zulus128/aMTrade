package com.vkassin.mtrade;

	import java.io.IOException;
	import java.util.ArrayList;
	import java.util.Arrays;

	import org.xmlpull.v1.XmlPullParserException;

	import android.app.Activity;
	import android.app.ListActivity;
	import android.os.Bundle;
	import android.view.View;
	import android.view.View.OnClickListener;
	import android.widget.ArrayAdapter;
	import android.widget.Button;
	import android.widget.ListView;
	import android.widget.Toast;
	import android.content.*;
	import android.content.res.XmlResourceParser;

	public class SelectListView extends ListActivity {

		private String[] lv_arr = {};
		private ListView mainListView = null;
		final String SETTING_TODOLIST = "todolist";

		private ArrayList<String> selectedItems = new ArrayList<String>();

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.simple);

			Button btnSave = (Button) findViewById(R.id.btnSave);
			btnSave.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Toast.makeText(getApplicationContext(),
							" You clicked Save button", Toast.LENGTH_SHORT).show();

					SaveSelections();
				}
			});

			Button btnClear = (Button) findViewById(R.id.btnClear);
			btnClear.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					Toast.makeText(getApplicationContext(),
							" You clicked Clear button", Toast.LENGTH_SHORT).show();

					ClearSelections();
				}
			});

			// Prepare an ArrayList of todo items
			ArrayList<String> listTODO = PrepareListFromXml();

			this.mainListView = getListView();

			mainListView.setCacheColorHint(0);

			// Bind the data with the list
			lv_arr = (String[]) listTODO.toArray(new String[0]);
			mainListView.setAdapter(new ArrayAdapter<String>(SelectListView.this,
					android.R.layout.simple_list_item_multiple_choice, lv_arr));

			mainListView.setItemsCanFocus(false);
			mainListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

			LoadSelections();
		}

		@Override
		protected void onPause() {
			// always handle the onPause to make sure selections are saved if user clicks back button
			SaveSelections();

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

		private void LoadSelections() {
			// if the selections were previously saved load them

			SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);

			if (settingsActivity.contains(SETTING_TODOLIST)) {
				String savedItems = settingsActivity
						.getString(SETTING_TODOLIST, "");

				this.selectedItems.addAll(Arrays.asList(savedItems.split(",")));
				int count = this.mainListView.getAdapter().getCount();

				for (int i = 0; i < count; i++) {
					String currentItem = (String) this.mainListView.getAdapter()
							.getItem(i);
					if (this.selectedItems.contains(currentItem)) {
						this.mainListView.setItemChecked(i, true);
					}

				}

			}
		}

		private void SaveSelections() {

			// save the selections in the shared preference in private mode for the user

			SharedPreferences settingsActivity = getPreferences(MODE_PRIVATE);
			SharedPreferences.Editor prefEditor = settingsActivity.edit();

			String savedItems = getSavedItems();

			prefEditor.putString(SETTING_TODOLIST, savedItems);

			prefEditor.commit();
		}

		private String getSavedItems() {
			String savedItems = "";

			int count = this.mainListView.getAdapter().getCount();

			for (int i = 0; i < count; i++) {

				if (this.mainListView.isItemChecked(i)) {
					if (savedItems.length() > 0) {
						savedItems += "," + this.mainListView.getItemAtPosition(i);
					} else {
						savedItems += this.mainListView.getItemAtPosition(i);
					}
				}

			}
			return savedItems;
		}

		private ArrayList<String> PrepareListFromXml() {
			ArrayList<String> todoItems = new ArrayList<String>();
			XmlResourceParser todolistXml = getResources().getXml(R.xml.todolist);

			int eventType = -1;
			while (eventType != XmlResourceParser.END_DOCUMENT) {
				if (eventType == XmlResourceParser.START_TAG) {

					String strNode = todolistXml.getName();
					if (strNode.equals("item")) {
						todoItems.add(todolistXml.getAttributeValue(null, "title"));
					}
				}

				try {
					eventType = todolistXml.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return todoItems;
		}
	}
