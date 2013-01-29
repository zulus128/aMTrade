package com.vkassin.mtrade;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

	public class SelectInstrAdapter extends ArrayAdapter<Instrument> {

		private ArrayList<Instrument> items;
		private ArrayList<Instrument> items1;

		private Context ctx;
		private int resourceId;
		private static final String TAG = "MTrade.SelectInstrAdapter"; 

		private InstrFilter filter;

		
		
		public SelectInstrAdapter(Context context, int resourceId, ArrayList<Instrument> objects) {
			super(context, resourceId, objects);
			
			this.items = objects;
			this.items1 = new ArrayList<Instrument>(objects);

			this.ctx = context;
			this.resourceId = resourceId;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
	    	
	    	LayoutInflater vi = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    	if(convertView == null)
	    		convertView = vi.inflate(R.layout.selectitem, null);

	    	Instrument item = getItems().get(position);
	    	if (item != null) {

	    		TextView type = (TextView) convertView.findViewById(R.id.SelectSymbol);
	    		type.setText(item.symbol);
	    		TextView name = (TextView) convertView.findViewById(R.id.SelectDescr);
	    		name.setText(item.description);
	    		CheckBox check = (CheckBox)  convertView.findViewById(R.id.checkbox);
	    		check.setChecked(item.favourite);


	    	}
	    	
	    	return convertView;
	    }
		
		public void setItems(ArrayList<Instrument> objects) {
			
			this.items.clear();
			this.items.addAll(objects);
			this.items1 = new ArrayList<Instrument>(objects);
		}
		
		public void addItems(ArrayList<Instrument> objects) {

			this.items.addAll(objects);
			this.items1 = new ArrayList<Instrument>(objects);
		}

		public ArrayList<Instrument> getItems() {
			
			return items;
		}
	
		@Override
	    public Filter getFilter() {
	         if (this.filter == null)
	            filter = new InstrFilter();
	        return filter;
	    }


	    private class InstrFilter extends Filter {
	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	        	
	            FilterResults result = new FilterResults();
	            if (constraint != null && constraint.toString().length() > 0) {
	                List<Instrument> founded = new ArrayList<Instrument>();
	                for (Instrument t : items1) {
//	                	Log.i(TAG, t.symbol + " contains " + constraint + " = " + t.symbol.contains(constraint));
	                    if (t.symbol.toUpperCase().contains(constraint.toString().toUpperCase()))
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
            
            for (Instrument o : (List<Instrument>) filterResults.values) {
                add(o);
            }
            
            notifyDataSetChanged();

        }
    }

	}

