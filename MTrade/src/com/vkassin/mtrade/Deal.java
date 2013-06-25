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

public class Deal implements Serializable, History {

	private static final long serialVersionUID = 29L;
	private static final String TAG = "MTrade.Deal";

	public String id = "";
	public Long instrId = Long.valueOf(0);
	public Long direct = Long.valueOf(0);
	public Double price = Double.valueOf(0);
	public Long qty = Long.valueOf(0);
	public Long status = Long.valueOf(0);
	public Long dtime = Long.valueOf(0);
	public Long dealSerial = Long.valueOf(0);
	private Long ordSerial = Long.valueOf(0);
	public String account = "";


	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {
	
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}

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
	    	
	    	int i = Common.app_ctx.getResources().getIdentifier(ds.toString(), "string", Common.app_ctx.getPackageName());
	    	
	    	if(i == 0)
	    		return "Nope";
	    	
	    	return Common.app_ctx.getResources().getString(i);
	    	
		    }

	};
	
	public Deal() {
	}
	
	public Deal(String i, JSONObject obj) {

		this.id = i;

		set(obj);

		Log.i(TAG, "Deal created id:" + i);

	}

	public void update(JSONObject obj) {

		set(obj);
		Log.i(TAG, "deal " + id + " updated.");

	}

	private void set(JSONObject obj) {

		try {
			this.price = obj.getDouble("price");
		} catch (JSONException e) {
		}
		try {
			this.qty = obj.getLong("quantity");
		} catch (JSONException e) {
		}
		try {
			this.instrId = obj.getLong("instrId");
		} catch (JSONException e) {
		}
		try {
			this.direct = obj.getLong("direct");
		} catch (JSONException e) {
		}
		try {
			this.status = obj.getLong("status");
		} catch (JSONException e) {
		}
		try {
			this.dtime = obj.getLong("dateTime");
		} catch (JSONException e) {
		}
		
		try{ this.dealSerial = obj.getLong("dealSerial"); }catch(JSONException e){ }
		try{ this.ordSerial = obj.getLong("ordSerial"); }catch(JSONException e){ }
		try{ this.account = obj.getString("account"); }catch(JSONException e){ }

	}

	public String getOperationType() {

		return "Deal";
	}

	public String getInstr() {

		Instrument i = Common.getInstrById(instrId);
		return (i == null)?".":i.symbol;

	}

	public String getDirect() {

		return (direct > 0) ? "S" : "B";
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
		
		return DEAL_STAT.fromOrdinal(status);
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

		if (this.dtime < arg0.getLongDTime()) {

			return -1;
			
		} else if (this.dtime > arg0.getLongDTime()) {

			return 1;
			
		}

		return 0;

	}
	
	public Long getSerial() {
		
		return dealSerial;
	}
	
	public String toString() {
		
		return "deal ." + ordSerial + ".";
	}
	
	public Long getOrdSerial() {
		
		return ordSerial;
	}

	public int getColor() {
		
		return Common.app_ctx.getResources().getColor(R.color.Orange);
	}

	public boolean canBeDeleted() {
		
		return false;
	}
	
	public String getRest() {
		
		return "";
	}


}
