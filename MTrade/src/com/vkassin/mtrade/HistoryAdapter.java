package com.vkassin.mtrade;

import java.util.ArrayList;
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
	private static Context ctx;
	private int resourceId;
	private final int colorDeal;
	private final int colorTransit;
	private static final String TAG = "MTrade.HistoryAdapter"; 

	private TracksFilter filter;
	
	private static enum DEAL_STAT {
		
		dsConfirmed,
		dsRejectedConf,
		dsRejectedPart,
		dsRejectedSys,
		dsWaitConf,
		dsWaitPart,
		dsWaitSys,
		dsWaitsBuyer,
		dsPaidBuyer,
		dsUnpaid,
		dsNoteDelivered,
		dsUndelivered,
		dsWaitsChange,
		dsWaitsAgree,
		dsRejectDepo,  
		dsRejectedSysNoMoney,
		dsRejectedSysNoSecur,
		dsWaitsAgreeDoubleNoMoney,
		dsWaitsAgreeDoubleNoSecur,
		dsNoAgreementNoMoney,
		dsNoAgreementNoSecur,
		dsRejectRepo,
		dsRejectedSysNotConf,
		dsRejectBuyer,
		dsRejectSeller,
		dsRejectSides, 
		dsWaitsSettlment;
		
	    private static final Map<Integer, DEAL_STAT> lookup = new HashMap<Integer, DEAL_STAT>();

	    static{
	      int ordinal = 0;
	      for (DEAL_STAT suit : EnumSet.allOf(DEAL_STAT.class)) {
	        lookup.put(ordinal, suit);
	        ordinal+= 1;
	      }
	    }

	    public static String fromOrdinal(Long ordinal) {
	    	
	    	DEAL_STAT ds = lookup.get(ordinal.intValue());
	    	if(ds == null)
	    		return "None";
	    	
	    	int i = ctx.getResources().getIdentifier(ds.toString(), "string", ctx.getPackageName());
	    	
	    	if(i == 0)
	    		return "Nope";
	    	
	    	return ctx.getResources().getString(i);
	    	
		    }

	};

	private static enum TRANSIT_STAT {
		
		   trsWait,
		   trsActive,
		   trsActBal,
		   trsDeal,
		   trsDealChg,
		   trsReject,
		   trsDelBrok,
		   trsDelInv,
		   trsDelBalBrok,
		   trsDelBalInv,
		   trsDelOper,
		   trsDelBalOper,
		   trsActChg,
		   trsActBalChg;
		   
	    private static final Map<Integer, TRANSIT_STAT> lookup = new HashMap<Integer, TRANSIT_STAT>();

	    static{
	      int ordinal = 0;
	      for (TRANSIT_STAT suit : EnumSet.allOf(TRANSIT_STAT.class)) {
	        lookup.put(ordinal, suit);
	        ordinal+= 1;
	      }
	    }

	    public static String fromOrdinal(Long ordinal) {
	    	
	    	TRANSIT_STAT ds = lookup.get(ordinal.intValue());
	    	if(ds == null)
	    		return "None_";
	    	
	    	int i = ctx.getResources().getIdentifier(ds.toString(), "string", ctx.getPackageName());
	    	
	    	if(i == 0)
	    		return "Nope_";
	    	
	    	return ctx.getResources().getString(i);
	    	
		    }

	};
	
	public HistoryAdapter(Context context, int resourceId, ArrayList<History> objects) {
		super(context, resourceId, objects);
		
		this.items = objects;
		this.items1 = new ArrayList<History>(objects);

		ctx = context;
		this.resourceId = resourceId;
		
		this.colorDeal =  ctx.getResources().getColor(R.color.Orange);
		this.colorTransit =  ctx.getResources().getColor(R.color.Yellow);
		
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
//    		st.setText(item.getStatus());
    		st.setText(trim(d?DEAL_STAT.fromOrdinal(item.getStatus()):TRANSIT_STAT.fromOrdinal(item.getStatus())));
    		st.setTextColor(d?colorDeal:colorTransit);

    		TextView date = (TextView) convertView.findViewById(R.id.HistoryDate);
    		date.setText(item.getDTime());
    		date.setTextColor(d?colorDeal:colorTransit);

    	}
    	
    	return convertView;
    }
	
	private String trim(String s) {
	
		int t = 4;
		return s.substring(0,  (t > s.length())?s.length():t) + ((t >= s.length())?"":"...");
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

