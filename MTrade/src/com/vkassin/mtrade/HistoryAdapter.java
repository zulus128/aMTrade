package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
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
//	private final int colorDeal;
//	private final int colorTransit;
	private static final String TAG = "MTrade.HistoryAdapter"; 

	private TracksFilter filter;
	
	private class HistoryComparator implements Comparator<History> {

		private int orderType;

		public HistoryComparator(int type) {

		    this.orderType = type;

		}

		public int compare(History lhs, History rhs) {

		    int res = 0;
		    
//		    res = (lhs.getPrice()).compareTo(rhs.getPrice());
		    
		    if (orderType == Common.SORT_TYPE_INSTR) {
		            res = (lhs.getInstr()).compareTo(rhs.getInstr());
		        }
		    else if (orderType == Common.SORT_TYPE_PRICE) {
		            res = (lhs.getPriceD()).compareTo(rhs.getPriceD());
		        }
		    else if (orderType == Common.SORT_TYPE_STATUS) {
	            res = (lhs.getStatus()).compareTo(rhs.getStatus());
	        }
		    else if (orderType == Common.SORT_TYPE_DATE) {
	            res = (lhs.getDTimeD()).compareTo(rhs.getDTimeD());
	        }
		    
		    return res;
		}

		}
	
	public HistoryAdapter(Context context, int resourceId, ArrayList<History> objects) {
		super(context, resourceId, objects);
		
		this.items = objects;
		this.items1 = new ArrayList<History>(objects);

		this.ctx = context;
		this.resourceId = resourceId;
		
//		this.colorDeal =  ctx.getResources().getColor(R.color.Orange);
//		this.colorTransit =  ctx.getResources().getColor(R.color.Yellow);
		
//		Log.e(TAG, "stats = " + DEAL_STAT.fromOrdinal(55));
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
//    		type.setTextColor(d?colorDeal:colorTransit);
    		type.setTextColor(item.getColor());

    		TextView name = (TextView) convertView.findViewById(R.id.HistoryName);
    		name.setText(item.getInstr());
//    		name.setTextColor(d?colorDeal:colorTransit);
    		name.setTextColor(item.getColor());


    		TextView dir = (TextView) convertView.findViewById(R.id.HistoryDirect);
    		dir.setText(item.getDirect());
//    		dir.setTextColor(d?colorDeal:colorTransit);
    		dir.setTextColor(item.getColor());


    		TextView price = (TextView) convertView.findViewById(R.id.HistoryPrice);
    		price.setText(item.getPrice());
//    		price.setTextColor(d?colorDeal:colorTransit);
    		price.setTextColor(item.getColor());


    		TextView qty = (TextView) convertView.findViewById(R.id.HistoryQty);
    		qty.setText(item.getQty());
//    		qty.setTextColor(d?colorDeal:colorTransit);
    		qty.setTextColor(item.getColor());


    		TextView st = (TextView) convertView.findViewById(R.id.HistoryStatus);
    		st.setText(trim(item.getStatus()));
//    		st.setText(trim(d?DEAL_STAT.fromOrdinal(item.getStatus()):TRANSIT_STAT.fromOrdinal(item.getStatus())));
//    		st.setTextColor(d?colorDeal:colorTransit);
    		st.setTextColor(item.getColor());


    		TextView date = (TextView) convertView.findViewById(R.id.HistoryDate);
    		date.setText(item.getDTime());
//    		date.setTextColor(d?colorDeal:colorTransit);
    		date.setTextColor(item.getColor());

    		TextView rest = (TextView) convertView.findViewById(R.id.HistoryRest);
    		rest.setText(item.getRest());
    		rest.setTextColor(item.getColor());

    	}
    	
    	return convertView;
    }
	
	private String trim(String s) {
	
		int t = 4;
		return s.substring(0,  (t > s.length())?s.length():t) + ((t >= s.length())?"":"...");
	}
	
	public void ssort(int type) {
		
		sort(new HistoryComparator(type));
//		notifyDataSetChanged();
	}
	
//	@Override
//	public void notifyDataSetChanged() {
//
//		sort(new HistoryComparator(0));
//
//	    super.notifyDataSetChanged();
//	}
	
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
        	
        	
//        	Log.w(TAG, "items cnt for filter = " + items.size());
        	
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

