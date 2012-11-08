package com.vkassin.mtrade;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteAdapter extends ArrayAdapter<Quote> {

	private ArrayList<Quote> items;
	private Context ctx;
	private int resourceId;
	private static final String TAG = "MTrade.QuoteAdapter"; 
	
	private final int colorBuy;
	private final int colorSell;
	
	public QuoteAdapter(Context context, int resourceId, ArrayList<Quote> objects) {
		
		super(context, resourceId, objects);
		
		this.items = objects;
		this.ctx = context;
		this.resourceId = resourceId;
		
		colorBuy = ctx.getResources().getColor(R.color.LimeGreen);
		colorSell = ctx.getResources().getColor(R.color.Red);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView= vi.inflate(R.layout.quotesitem, null);

    	Quote item = getItems().get(position);
    	boolean b = item.qtyBuy > 0;
    	
    	if (item != null) {

    		TextView bid = (TextView) convertView.findViewById(R.id.QuoteBidQ);
    		bid.setText(item.qtyBuy.toString());
    		bid.setTextColor(b?colorBuy:colorSell);

    		TextView price = (TextView) convertView.findViewById(R.id.QuotePrice);
    		price.setText(item.price.toString());
    		price.setTextColor(b?colorBuy:colorSell);

    		TextView ask = (TextView) convertView.findViewById(R.id.QuoteSellQ);
    		ask.setText(item.qtySell.toString());
    		ask.setTextColor(b?colorBuy:colorSell);


    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<Quote> objects) {
		
		Log.w(TAG, "Quotes cnt = " + objects.size());
		this.items.clear();
		this.items.addAll(objects);
	}
	
	public void addItems(ArrayList<Quote> objects) {
		this.items.addAll(objects);
	}

	public ArrayList<Quote> getItems() {
		return items;
	}
	
	
}

