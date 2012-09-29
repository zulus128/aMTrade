package com.vkassin.mtrade;

import org.json.JSONException;
import org.json.JSONObject;

public class DayChartElement implements Comparable<DayChartElement> {

	public Double open;
	public Integer dateTime;
	public Integer volume;
	public Double high;
	public Double low;
	public Double avg;
	public Double close;
	
	public DayChartElement(JSONObject obj){

		try{ this.open = obj.getDouble("open"); }catch(JSONException e){ }
		try{ this.dateTime = obj.getInt("dateTime"); }catch(JSONException e){ }
		try{ this.volume = obj.getInt("volume"); }catch(JSONException e){ }
		try{ this.high = obj.getDouble("high"); }catch(JSONException e){ }
		try{ this.low = obj.getDouble("low"); }catch(JSONException e){ }
		try{ this.avg = obj.getDouble("avg"); }catch(JSONException e){ }
		try{ this.close = obj.getDouble("close"); }catch(JSONException e){ }
	}

	public int compareTo(DayChartElement arg0) {

		return this.dateTime.compareTo(arg0.dateTime);
	}
    
}
