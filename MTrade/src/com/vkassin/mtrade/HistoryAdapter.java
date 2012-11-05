package com.vkassin.mtrade;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<History> {
	private ArrayList<History> items;
	private Context ctx;
	private int resourceId;
	private final int colorDeal;
	private final int colorTransit;

	public HistoryAdapter(Context context, int resourceId, ArrayList<History> objects) {
		super(context, resourceId, objects);
		
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
		
		this.colorDeal =  ctx.getResources().getColor(R.color.Green);
		this.colorTransit =  ctx.getResources().getColor(R.color.Yellow);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	LinearLayout layout = new LinearLayout(parent.getContext());
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	layout.addView(vi.inflate(resourceId, null));

    	History item = getItems().get(position);
    	if (item != null) {

        	boolean d = item.getOperationType().equals("Deal"); 

    		TextView type = (TextView) layout.findViewById(R.id.HistoryType);
    		type.setText(item.getOperationType());
    		type.setTextColor(d?colorDeal:colorTransit);

    		TextView name = (TextView) layout.findViewById(R.id.HistoryName);
    		name.setText(item.getInstr());
    		name.setTextColor(d?colorDeal:colorTransit);

    		TextView dir = (TextView) layout.findViewById(R.id.HistoryDirect);
    		dir.setText(item.getDirect());
    		dir.setTextColor(d?colorDeal:colorTransit);

    		TextView price = (TextView) layout.findViewById(R.id.HistoryPrice);
    		price.setText(item.getPrice());
    		price.setTextColor(d?colorDeal:colorTransit);

    		TextView qty = (TextView) layout.findViewById(R.id.HistoryQty);
    		qty.setText(item.getQty());
    		qty.setTextColor(d?colorDeal:colorTransit);

    		TextView st = (TextView) layout.findViewById(R.id.HistoryStatus);
    		st.setText(item.getStatus());
    		st.setTextColor(d?colorDeal:colorTransit);

    		TextView date = (TextView) layout.findViewById(R.id.HistoryDate);
    		date.setText(item.getDTime());
    		date.setTextColor(d?colorDeal:colorTransit);

    	}
    	
    	return layout;
    }

	public void setItems(ArrayList<History> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
	}
	
	public void addItems(ArrayList<History> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<History> getItems() {
		return items;
	}
}

