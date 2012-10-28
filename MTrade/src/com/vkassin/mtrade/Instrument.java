package com.vkassin.mtrade;

import java.io.Serializable;
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

	public String id = "";
	public String symbol = "";
	public String tradeStatus = "";
	public String description = "";

	public Double min = Double.valueOf(0);
	public Double max = Double.valueOf(0);
	public Double bid = Double.valueOf(0);
	public Double ask = Double.valueOf(0);
	public Double avg = Double.valueOf(0);

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

	private SortedSet<Quote> quotes = new TreeSet<Quote>();

	public ArrayList<Quote> getQuotes() {
	
		return new ArrayList<Quote>(quotes);
	}
	
//	/**
//	 * @return the quotes
//	 */
//	public SortedSet<Quote> getQuotes() {
//		return quotes;
//	}
//
//	/**
//	 * @param quotes the quotes to set
//	 */
//	public void setQuotes(SortedSet<Quote> quotes) {
//		this.quotes = quotes;
//	}

	public Instrument(String i, JSONObject obj) {
		
		this.id = i;
		
		set(obj);
		
//		Log.i(TAG, symbol + " : " + description);
		
	}
		
	public void update(JSONObject obj) {
		
		set(obj);
		Log.i(TAG, symbol + " updated.");
		
	}
	
	public void addDayChartElement(String key, JSONObject obj) {
		
		daychart.put(key, new DayChartElement(obj));
	}
		
		
	public void addToQuoteList(String key, JSONObject obj) {
		
		quotes.add(new Quote(key, obj));
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

}
