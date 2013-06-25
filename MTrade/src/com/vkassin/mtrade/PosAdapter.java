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

public class PosAdapter extends ArrayAdapter<Position> {
	
	private ArrayList<Position> items;
	private Context ctx;
	private int resourceId;
//	private final int colorPos;

	private class PosComparator implements Comparator<Position> {

		public int compare(Position lhs, Position rhs) {

		    return Integer.valueOf(lhs.symbol.length()).compareTo(Integer.valueOf(rhs.symbol.length()));
		}

		}
	public PosAdapter(Context context, int resourceId, ArrayList<Position> objects) {
		
		super(context, resourceId, objects);
		
//		this.colorPos =  ctx.getResources().getColor(R.color.Azure);
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView = vi.inflate(R.layout.positem, null);

    	Position item = getItems().get(position);
    	if (item != null) {

    		TextView name = (TextView) convertView.findViewById(R.id.PosSymbol);
    		name.setText(item.symbol);

    		TextView code = (TextView) convertView.findViewById(R.id.PosCode);
    		code.setText(item.acc_code);

    		TextView curr = (TextView) convertView.findViewById(R.id.PosCurr);
//    		curr.setText(item.currPos.toString());
    		curr.setText(item.getCurrPos());

//    		TextView buy = (TextView) convertView.findViewById(R.id.PosBuy);
//    		buy.setText(item.buyPlanPos.toString());
//
//    		TextView sell = (TextView) convertView.findViewById(R.id.PosSell);
//    		sell.setText(item.sellPlanPos.toString());

    		TextView rest = (TextView) convertView.findViewById(R.id.PosRest);
//    		rest.setText(item.restPos.toString());
    		rest.setText(item.getRestPos());

    		TextView in = (TextView) convertView.findViewById(R.id.PosIn);
//    		in.setText(item.inPos.toString());
    		in.setText(item.getInPos());

    		TextView avg = (TextView) convertView.findViewById(R.id.PosAVG);
    		avg.setText("0-0");

    		TextView inc = (TextView) convertView.findViewById(R.id.PosIncome);
    		inc.setText("0-0");

    		TextView incp = (TextView) convertView.findViewById(R.id.PosIncomeProc);
    		incp.setText("0-0");


    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<Position> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
		sort(new PosComparator());

	}
	
	public void addItems(ArrayList<Position> objects) {
		this.items.addAll(objects);
		sort(new PosComparator());
	}

	public ArrayList<Position> getItems() {
		return items;
	}
}

