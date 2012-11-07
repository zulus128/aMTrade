package com.vkassin.mtrade;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	}

	public String getOperationType() {

		return "Deal";
	}

	public String getInstr() {

		Instrument i = Common.getInstrById(instrId);
		return i.symbol;

	}

	public String getDirect() {

		return (direct > 0) ? "S" : "B";
	}

	public String getPrice() {

		return price.toString();
	}

	public String getQty() {

		return qty.toString();
	}

	public String getStatus() {

		return status.toString();
	}

	public String getDTime() {

		Date d = new Date(dtime);
		return new SimpleDateFormat("HH:mm dd/MM").format(d);

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

}
