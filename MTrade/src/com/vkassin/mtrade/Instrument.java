package com.vkassin.mtrade;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class Instrument implements Serializable {

	private static final long serialVersionUID = 22L;
	private static final String TAG = "MTrade.Instrument"; 
	
	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {
	
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}
	
	public String id = "";
	public String symbol = "";
	public String tradeStatus = "";
	public String description = "";

	private Double min = Double.valueOf(0);
	private Double max = Double.valueOf(0);
	private Double bid = Double.valueOf(0);
	private Double ask = Double.valueOf(0);
	private Double avg = Double.valueOf(0);

	public boolean favourite;
	
//	public SortedSet<DayChartElement> daychart = new TreeSet<DayChartElement>();
	private HashMap<String, DayChartElement> daychart = new HashMap<String, DayChartElement>();
	
	/**
	 * @return the daychart
	 */
	public HashMap<String, DayChartElement> getDaychart() {
		return daychart;
	}

//	/**
//	 * @param daychart the daychart to set
//	 */
//	public void setDaychart(HashMap<String, DayChartElement> daychart) {
//		this.daychart = daychart;
//	}

//	private SortedSet<Quote> quotes = new TreeSet<Quote>();
	private HashMap<String, Quote> quoteMap = new HashMap<String, Quote>();

	public ArrayList<Quote> getQuotes() {
	
		SortedSet<Quote> quotes = new TreeSet<Quote>(quoteMap.values());
		return new ArrayList<Quote>(quotes);
	}

	public Instrument(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
//		Log.i(TAG, symbol + " : " + description);
		
		if(Common.getSelectedInstrument() == null)
			Common.setSelectedInstrument(this);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, symbol + " updated.");
		
	}
	
	public void addDayChartElement(String key, JSONObject obj) {
		
		daychart.put(key, new DayChartElement(obj));
	}
		
		
	public void modifyQuoteList(String key, JSONObject obj) {
		
//		Log.i(TAG, "addToQuoteList from instr = " + this.symbol + " cnt = " + quoteMap.size());
		
		try {
			if(obj.getString("action").equals("REMOVE")) {

				quoteMap.remove(key);
				return;
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Quote old = (Quote)quoteMap.get(key);
		if(old == null)
			quoteMap.put(key, new Quote(key, obj));
		else
			old.update(obj);

//		quotes.add(new Quote(key, obj));
	}

	private void set(JSONObject obj){

		try{ this.description = obj.getString("descRu"); }catch(JSONException e){ }
		try{ this.symbol = obj.getString("symbol"); }catch(JSONException e){ }
		try{ this.tradeStatus = obj.getString("tradeStatus"); }catch(JSONException e){ }
		
		try{ this.min = obj.getDouble("min"); }catch(JSONException e){ }
		try{ this.max = obj.getDouble("max"); }catch(JSONException e){ }
		try{ this.bid = obj.getDouble("bid"); }catch(JSONException e){ }
		try{ this.ask = obj.getDouble("ask"); }catch(JSONException e){ }
		try{ this.avg = obj.getDouble("avg"); }catch(JSONException e){ }
	}
	
	public String getShortContent() {
		
		return description.length() > 100 ? description.substring(0, 97) + "..." : description;
	}

	public String getAvgS() {
		
		return twoDForm.format(avg);
	}

	public String getBidS() {
		
		return twoDForm.format(bid);
	}
	public String getAskS() {
		
		return twoDForm.format(ask);
	}
}
