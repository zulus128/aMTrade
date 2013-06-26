package com.vkassin.mtrade;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;

import kz.gamma.tumarcsp.LibraryWrapper;
import kz.gamma.tumarcsp.TumarCspFunctions;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Position implements Serializable {

	private static final long serialVersionUID = 29L;
	private static final String TAG = "MTrade.Position"; 

	public String id = "";
	public String symbol = "";
	public String acc_code = "";
	private Double currPos = Double.valueOf(0);
//	public Double buyPlanPos = Double.valueOf(0);
//	public Double sellPlanPos = Double.valueOf(0);
	private Double restPos = Double.valueOf(0);
	private Double inPos = Double.valueOf(0);

	private Double avg;
	private Double income;
	private Double incomeProc;
	
	
	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {
	
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}


	public void calcDohod() {
		
		long qty = 0;
		double val = 0;
		double sumbuy = 0;
		double sumsell = 0;
		
		Iterator<String> itr = Common.arcdealMap.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			Deal d =Common.arcdealMap.get(key);

				if(this.symbol.equals(d.getInstr())) {
		
					qty += d.qty.longValue();
					double v =  (d.price.doubleValue() * d.qty.longValue());
					val += v;
					if(d.direct.intValue() == 0)
						sumbuy += v;
					else
						sumsell += v;
				}
		}
		
		this.avg = (qty == 0)?0:val / qty;
		this.income = sumsell - sumbuy;
		this.incomeProc = (sumbuy < 0.1)?0:((sumsell * 100 / sumbuy) - 100);
			

	}
	
	public Position(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
		Log.i(TAG, "Position created id:" + i);
		
	}

	public Position(String i, String acc, String instr) {
		
		this.id = i;
		this.acc_code = acc;
		this.symbol = instr;
		Log.i(TAG, "Position created id:" + i + " acc: " + acc + "instr: " + instr);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, "position " + id + " updated.");
		
	}
	
	private void set(JSONObject obj) {

		try{ this.symbol = obj.getString("instrum"); }catch(JSONException e){ }
		try{ this.acc_code = obj.getString("code"); }catch(JSONException e){ }
		try{ this.currPos = obj.getDouble("currPos"); }catch(JSONException e){ }
//		try{ this.buyPlanPos = obj.getDouble("buyPlanPos"); }catch(JSONException e){ }
//		try{ this.sellPlanPos = obj.getDouble("sellPlanPos"); }catch(JSONException e){ }
		try{ this.restPos = obj.getDouble("restPos"); }catch(JSONException e){ }
		try{ this.inPos = obj.getDouble("inPos"); }catch(JSONException e){ }

	}

	public String getCurrPos() {

		return twoDForm.format(currPos);

	}
	
	public String getRestPos() {

		return twoDForm.format(restPos);

	}
	
	public String getInPos() {

		return twoDForm.format(inPos);

	}

	public String getAVG() {

		return twoDForm.format(avg);

	}

	public String getIncome() {

		return twoDForm.format(income);

	}

	public String getIncomeProc() {

		return twoDForm.format(incomeProc);

	}


}
