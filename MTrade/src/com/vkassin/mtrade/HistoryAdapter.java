package com.vkassin.mtrade;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<Order> {
	private ArrayList<Order> items;
	private Context ctx;
	private int resourceId;
	
	public HistoryAdapter(Context context, int resourceId, ArrayList<Order> objects) {
		super(context, resourceId, objects);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	LinearLayout layout = new LinearLayout(parent.getContext());
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	layout.addView(vi.inflate(resourceId, null));
    	
    	Order item = getItems().get(position);
    	if (item != null) {

    		Instrument i = Common.getInstrById(item.instrId);
    		TextView title = (TextView) layout.findViewById(R.id.HistoryName);
    		title.setText(i.symbol);
    		
    		TextView bid = (TextView) layout.findViewById(R.id.HistoryPrice);
    		bid.setText(item.price.toString());

    		TextView ask = (TextView) layout.findViewById(R.id.HistoryQty);
    		ask.setText(item.qty.toString());
    	}
    	
    	return layout;
    }

	public void setItems(ArrayList<Order> objects) {
		this.items.clear();
		this.items.addAll(objects);
	}
	
	public void addItems(ArrayList<Order> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<Order> getItems() {
		return items;
	}
}

