package com.vkassin.mtrade;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;

public class InstrsAdapter extends ArrayAdapter<Instrument> {

	private final int colorBackSelected;
	private final int colorBackUnselected;
	private final int colorTextSelected;
	private final int colorTextUnselected;
	private final int colorInstrUnselected;
	private final int colorInstrSelected;

	private ArrayList<Instrument> items;
	private Context ctx;
	private int resourceId;

	private int selectedPos = -1;	// init value for none-selected

	public InstrsAdapter(Context context, int resourceId, ArrayList<Instrument> objects) {

		super(context, resourceId, objects);
		
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
		
		colorBackSelected = ctx.getResources().getColor(R.color.Blue);
		colorBackUnselected = ctx.getResources().getColor(R.color.SlateGray);
		colorTextSelected = ctx.getResources().getColor(R.color.White);
		colorTextUnselected = ctx.getResources().getColor(R.color.WhiteSmoke);
		colorInstrSelected = ctx.getResources().getColor(R.color.YellowGreen);
		colorInstrUnselected = ctx.getResources().getColor(R.color.Yellow);
	}

	public void setSelectedPosition(int pos){
		
		selectedPos = pos;
		// inform the view of this change
		
//		View view = ((InstrActivity)ctx).list.getChildAt(pos);
//        getView(pos, view, ((InstrActivity)ctx).list);
	}

	public int getSelectedPosition(){
	
		return selectedPos;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView = vi.inflate(R.layout.instritem, null);

    	boolean b = (position == selectedPos);
    	if (b) {
    	    
    		convertView.setBackgroundColor(colorBackSelected);
//    	    Common.setSelectedInstrument( getItems().get(position) );
    	    
    	} else {
    		convertView.setBackgroundColor(colorBackUnselected);
    	}
    	
    	Instrument item = getItems().get(position);
    	if (item != null) {

    		TextView title = (TextView) convertView.findViewById(R.id.InstrName);
    		title.setText(item.symbol);
    		title.setTextColor(b?colorInstrSelected:colorInstrUnselected);

    		TextView tstatus = (TextView) convertView.findViewById(R.id.InstrStatus);
    		tstatus.setText(item.tradeStatus);
    		tstatus.setTextColor(b?colorTextSelected:colorTextUnselected);
    		
    		TextView chg = (TextView) convertView.findViewById(R.id.InstrChg);
    		chg.setText(item.avg.toString());
    		chg.setTextColor(b?colorTextSelected:colorTextUnselected);

    		TextView bid = (TextView) convertView.findViewById(R.id.InstrBid);
    		bid.setText(item.bid.toString());
    		bid.setTextColor(b?colorTextSelected:colorTextUnselected);

    		TextView ask = (TextView) convertView.findViewById(R.id.InstrAsk);
    		ask.setText(item.ask.toString());
    		ask.setTextColor(b?colorTextSelected:colorTextUnselected);

    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<Instrument> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
		
		if((selectedPos < 0) && (objects.size() > 0))
			setSelectedPosition(0);
	}
	
	public void addItems(ArrayList<Instrument> objects) {
		
		this.items.addAll(objects);
	}

	public ArrayList<Instrument> getItems() {
		
		return items;
	}
}

