package com.vkassin.mtrade;

import java.util.ArrayList;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsArrayAdapter extends ArrayAdapter<RSSItem> {
	private ArrayList<RSSItem> items;
	private Context ctx;
	private int resourceId;

	public NewsArrayAdapter(Context context, int resourceId, ArrayList<RSSItem> objects) {
		super(context, resourceId, objects);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView= vi.inflate(R.layout.newsitem, null);

    	RSSItem item = getItems().get(position);
    	if (item != null) {
    		
    		TextView title = (TextView) convertView.findViewById(R.id.NewsTitleTextView);
    		TextView date = (TextView) convertView.findViewById(R.id.NewsDateTextView);
    		
    		title.setText(item.getShortTitle());
    		date.setText(DateFormat.format("dd.MM.yy k:mm", item.getPubDate()));
    		
    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<RSSItem> objects) {
		this.items.clear();
		this.items.addAll(objects);
	}

	public void addItems(ArrayList<RSSItem> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<RSSItem> getItems() {
		return items;
	}
}
