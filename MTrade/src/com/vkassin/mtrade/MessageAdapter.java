package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Mess> {
	
	private ArrayList<Mess> items;
	private Context ctx;
	private int resourceId;
//	private final int colorPos;

	private class MesComparator implements Comparator<Mess> {

		public int compare(Mess lhs, Mess rhs) {

		    return lhs.time.compareTo(rhs.time);
		}

	}
	
	public MessageAdapter(Context context, int resourceId, ArrayList<Mess> objects) {
		
		super(context, resourceId, objects);
		
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView = vi.inflate(R.layout.mesitem, null);

    	Mess item = getItems().get(position);
    	if (item != null) {

    		TextView time = (TextView) convertView.findViewById(R.id.MesTime);
    		time.setText(item.time);

    		TextView from = (TextView) convertView.findViewById(R.id.MesFrom);
    		from.setText(item.from);

    		TextView text = (TextView) convertView.findViewById(R.id.MesText);
    		text.setText(item.msg);

    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<Mess> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
		sort(new MesComparator());

	}
	
	public void addItems(ArrayList<Mess> objects) {
		this.items.addAll(objects);
		sort(new MesComparator());
	}

	public ArrayList<Mess> getItems() {
		return items;
	}
}

