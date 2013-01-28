package com.vkassin.mtrade;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Order implements Serializable, History {

	private static final long serialVersionUID = 23L;
	private static final String TAG = "MTrade.Order"; 

	public String id = "";
	public Long instrId = Long.valueOf(0);
	public Long direct = Long.valueOf(0);
	private Double price = Double.valueOf(0);
	public Long qty = Long.valueOf(0);
	public Long status = Long.valueOf(0);
	public Long dtime = Long.valueOf(0);
	public Long transSerial = Long.valueOf(0);
	
	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {
	
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}
	
	private static enum TRANSIT_STAT {
		
		   trsWait,
		   trsActive,
		   trsActBal,
		   trsDeal,
		   trsReject,
		   trsDelBrok,
		   trsDelInv,
		   trsDelBalBrok,
		   trsDelBalInv,
		   trsDelOper,
		   trsDelBalOper,
		   trsActChg,
		   trsActBalChg,
		   trsDealChg;
		   
	    private static final Map<Integer, TRANSIT_STAT> lookup = new HashMap<Integer, TRANSIT_STAT>();

	    static{
	      int ordinal = 0;
	      for (TRANSIT_STAT suit : EnumSet.allOf(TRANSIT_STAT.class)) {
	        lookup.put(ordinal, suit);
	        ordinal+= 1;
	      }
	    }

	    public static TRANSIT_STAT typeFromOrdinal(Long ordinal) {
	    
	    	return lookup.get(ordinal.intValue());
	    }
	    
	    public static String fromOrdinal(Long ordinal) {
	    	
	    	TRANSIT_STAT ds = lookup.get(ordinal.intValue());
	    	if(ds == null)
	    		return "None_";
	    	
	    	int i = Common.app_ctx.getResources().getIdentifier(ds.toString(), "string", Common.app_ctx.getPackageName());
	    	
	    	if(i == 0)
	    		return "Nope_";
	    	
	    	return Common.app_ctx.getResources().getString(i);
	    	
		    }

	};

	public Order(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Order created id:" + i);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "order " + id + " updated.");
		
	}
	
	private void set(JSONObject obj) {

		try{ this.price = obj.getDouble("price"); }catch(JSONException e){ }
		try{ this.qty = obj.getLong("quantity"); }catch(JSONException e){ }
		try{ this.instrId = obj.getLong("instrId"); }catch(JSONException e){ }
		try{ this.direct = obj.getLong("direct"); }catch(JSONException e){ }
		try{ this.status = obj.getLong("status"); }catch(JSONException e){ }
		try{ this.dtime = obj.getLong("dateTime"); }catch(JSONException e){ }
		try{ this.transSerial = obj.getLong("transSerial"); }catch(JSONException e){ }

	}

	public String getOperationType() {
		
		return "Transit";
	}

	public String getInstr() {
		
		Instrument i = Common.getInstrById(instrId);
		return i.symbol;
		
	}

	public String getDirect() {
		
		return (direct > 0)?"S":"B";
	}

	public String getPrice() {

//		return price.toString();
		return twoDForm.format(price);

	}

	public Double getPriceD() {
		
		return price;
	}

	public String getQty() {

		return qty.toString();
	}

	public String getStatus() {

		return TRANSIT_STAT.fromOrdinal(status);
//		return status;
	}

	public String getDTime() {
		
		Date d = new Date(dtime);
		return new SimpleDateFormat("HH:mm dd/MM").format(d);
	}

	public Date getDTimeD() {
		
		return new Date(dtime);
	}

	public Long getLongDTime() {
		
		return dtime;
	}

	public int compareTo(History arg0) {

		if(this.dtime < arg0.getLongDTime()) {

			return -1;
	    }   
	    else
			if(this.dtime > arg0.getLongDTime()) {

				return 1;
			}

		return 0;  
	    
	}

	public Long getSerial() {
		
		return transSerial;
	}

	public String toString() {
		
		return "transit";
	}
	
	public int getColor() {
		
    	TRANSIT_STAT ds = TRANSIT_STAT.typeFromOrdinal(status);
    	if(ds != null)
    	switch(ds) {
    	
    	case trsWait:
    		return Common.app_ctx.getResources().getColor(R.color.White);
    		
    	case trsActive:
    	case trsActBal:
    		return Common.app_ctx.getResources().getColor((direct == 0)?R.color.Green:R.color.Red);

    	case trsReject:
    	case trsDelBrok:
    	case trsDelInv:
    	case trsDelBalBrok:
    	case trsDelBalInv:
    	case trsDelOper:
    	case trsDelBalOper:
    		return Common.app_ctx.getResources().getColor(R.color.DarkRed);
		case trsActBalChg:
		case trsActChg:
		case trsDeal:
		case trsDealChg:
		default:
			break;

    	}
		return Common.app_ctx.getResources().getColor(R.color.Yellow);
	}

	
}
