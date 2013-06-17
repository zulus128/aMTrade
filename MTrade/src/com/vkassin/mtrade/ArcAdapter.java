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

public class ArcAdapter extends ArrayAdapter<Deal> {
	
	private ArrayList<Deal> items;
	private Context ctx;
	private int resourceId;
//	private final int colorPos;

	private class ArcComparator implements Comparator<Deal> {

		public int compare(Deal lhs, Deal rhs) {

			return (rhs.getDTimeD()).compareTo(lhs.getDTimeD());		
		}

		}
	
	public ArcAdapter(Context context, int resourceId, ArrayList<Deal> objects) {
		
		super(context, resourceId, objects);
		
//		this.colorPos =  ctx.getResources().getColor(R.color.Azure);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView = vi.inflate(R.layout.archiveitem, null);

    	Deal item = getItems().get(position);
    	if (item != null) {

    		TextView name = (TextView) convertView.findViewById(R.id.ArchiveName);
    		name.setText(item.getInstr());

    		TextView dir = (TextView) convertView.findViewById(R.id.ArchiveDirect);
    		dir.setText(item.getDirect());

    		TextView price = (TextView) convertView.findViewById(R.id.ArchivePrice);
    		price.setText(item.getPrice());

    		TextView qty = (TextView) convertView.findViewById(R.id.ArchiveQty);
    		qty.setText(item.getQty());

    		TextView date = (TextView) convertView.findViewById(R.id.ArchiveDate);
    		date.setText(item.getDTime());


    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<Deal> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
		sort(new ArcComparator());

	}
	
	public void addItems(ArrayList<Deal> objects) {
		this.items.addAll(objects);
		sort(new ArcComparator());
	}

	public ArrayList<Deal> getItems() {
		return items;
	}
}

