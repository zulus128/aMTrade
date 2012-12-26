package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<History> {

	private ArrayList<History> items;
	private ArrayList<History> items1;
	private Context ctx;
	private int resourceId;
	private final int colorDeal;
	private final int colorTransit;
	private static final String TAG = "MTrade.HistoryAdapter"; 

	private TracksFilter filter;

	public HistoryAdapter(Context context, int resourceId, ArrayList<History> objects) {
		super(context, resourceId, objects);
		
		this.items = objects;
		this.items1 = new ArrayList<History>(objects);

		this.ctx = context;
		this.resourceId = resourceId;
		
		this.colorDeal =  ctx.getResources().getColor(R.color.Orange);
		this.colorTransit =  ctx.getResources().getColor(R.color.Yellow);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
    	
    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    	if(convertView == null)
    		convertView = vi.inflate(R.layout.historyitem, null);

    	History item = getItems().get(position);
    	if (item != null) {

        	boolean d = item.getOperationType().equals("Deal"); 

    		TextView type = (TextView) convertView.findViewById(R.id.HistoryType);
    		type.setText(item.getOperationType());
    		type.setTextColor(d?colorDeal:colorTransit);

    		TextView name = (TextView) convertView.findViewById(R.id.HistoryName);
    		name.setText(item.getInstr());
    		name.setTextColor(d?colorDeal:colorTransit);

    		TextView dir = (TextView) convertView.findViewById(R.id.HistoryDirect);
    		dir.setText(item.getDirect());
    		dir.setTextColor(d?colorDeal:colorTransit);

    		TextView price = (TextView) convertView.findViewById(R.id.HistoryPrice);
    		price.setText(item.getPrice());
    		price.setTextColor(d?colorDeal:colorTransit);

    		TextView qty = (TextView) convertView.findViewById(R.id.HistoryQty);
    		qty.setText(item.getQty());
    		qty.setTextColor(d?colorDeal:colorTransit);

    		TextView st = (TextView) convertView.findViewById(R.id.HistoryStatus);
    		st.setText(item.getStatus());
    		st.setTextColor(d?colorDeal:colorTransit);

    		TextView date = (TextView) convertView.findViewById(R.id.HistoryDate);
    		date.setText(item.getDTime());
    		date.setTextColor(d?colorDeal:colorTransit);

    	}
    	
    	return convertView;
    }

	public void setItems(ArrayList<History> objects) {
		
		this.items.clear();
		this.items.addAll(objects);
		this.items1 = new ArrayList<History>(objects);
	}
	
	public void addItems(ArrayList<History> objects) {
		this.items.addAll(objects);
		this.items1 = new ArrayList<History>(objects);
	}

	public ArrayList<History> getItems() {
		return items;
	}
	
	
	@Override
    public Filter getFilter() {
         if (this.filter == null)
            filter = new TracksFilter();
        return filter;
    }


    private class TracksFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // NOTE: this function is *always* called from a background thread, and
            // not the UI thread.
//            constraint = constraint.toString().toLowerCase();
        	
        	
        	Log.w(TAG, "items cnt for filter = " + items.size());
        	
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                List<History> founded = new ArrayList<History>();
                for (History t : items1) {
                    if (t.toString().contains(constraint))
                        founded.add(t);
                }


                result.values = founded;
                result.count = founded.size();
            } else {
                result.values = items1;
                result.count = items1.size();
            }




            return result;
        }


        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        	
            clear();
            
            for (History o : (List<History>) filterResults.values) {
                add(o);
            }
            
            notifyDataSetChanged();

        }
    }
}

