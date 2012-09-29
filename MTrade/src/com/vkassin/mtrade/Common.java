package com.vkassin.mtrade;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.TabHost;//
import android.widget.Toast;

public class Common {

	private static final String TAG = "MTrade.Common"; 
//    private static final int HTTP_STATUS_OK = 200;
//	private static String sUserAgent = null;
//    private static byte[] sBuffer = new byte[4096];   

	public enum item_type { IT_NONE, IT_INSTR };
	 
	public final static Integer NO_ERRORS = 0;
	public final static Integer INITIAL_LOADING_COMPLITE = 1;
     
    public final static Integer HEARTBEAT = 10;
    public final static Integer LOGIN = 11;
    public final static Integer LOGOUT = 12;

    public final static Integer INSTRUMENT = 100;
    public final static Integer TRADEACCOUNT = 101;
    public final static Integer QUOTE = 102;
    public final static Integer DEAL = 103;
    public final static Integer TRANSIT_ORDER = 104;
    public final static Integer CREATE_REMOVE_ORDER = 105;
    public final static Integer CHART = 106;
    	
    public final static Integer SUBSCRIBE = 107;
    public final static Integer QUOTE_CHART_SUBSCRIPTION = 108;
    
    public final static String PROTOCOL_VERSION = "1.0";
    public final static int ERROR_USER_WAS_NOT_FOUND = 200;
    public final static int ERROR_USER_ALREADY_CONNECTED = 201;
    public final static int ERROR_PASSWORD_ERROR = 202;
    public final static int ERROR_WRONG_PROTOCOL_VERSION = 203;
    public final static int ERROR_LOGIN_INFORMATION = 204;

	public static TabHost tabHost;

	public static Context app_ctx;
	private static final String FLIST_FNAME = "favr_list";
	
	private static HashMap<String, Instrument> instrMap = new HashMap<String, Instrument>();
	private static HashMap<String, Order> orderMap = new HashMap<String, Order>();
	private static HashSet<String> favrList = new HashSet<String>();
	private static HashMap<String, String> accMap = new HashMap<String, String>();

    public static boolean FIRSTLOAD_FINISHED = false;
//    public static int selectedListItem = 0;
    
    public static ArrayList<Instrument> getFavInstrs() {
		
		ArrayList<Instrument> a = new ArrayList<Instrument>();
		Iterator<String> itr = instrMap.keySet().iterator();
		while (itr.hasNext()) {
			
			String key = itr.next();
			if(instrMap.get(key).favourite == true)
				a.add(instrMap.get(key));
		}
		
		return a;
	}

    public static ArrayList<Instrument> getAllInstrs() {
		
		return new ArrayList<Instrument>(instrMap.values());
	}

    public static Instrument getInstrById(long id) {
		
		return instrMap.get(String.valueOf(id));
	}
	
	public static ArrayList<String> getInstrNameArray() {
	
		ArrayList<String> a = new ArrayList<String>();
		Iterator<String> itr = instrMap.keySet().iterator();
			while (itr.hasNext()) {
				
				String key = itr.next();
				a.add(instrMap.get(key).symbol);
			}
			
		return a;
	}
	
	public static void clearInstrList() {
	
		instrMap.clear();
	}

	public static void addToInstrList(String key, JSONObject obj) {
		
		Instrument old = instrMap.get(key);
		if(old == null)
			instrMap.put(key, new Instrument(key, obj));
		else
			old.update(obj);
	}

	public static void addToCharts(String key, JSONObject obj) {
	
		try {
			
			long instid = obj.getLong("instrId");
			Log.i(TAG, "key = " + key + " instr = " + instid);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	public static ArrayList<Order> getAllOrders() {
		
		return new ArrayList<Order>(orderMap.values());
	}

	public static void clearOrderList() {
		
		orderMap.clear();
	}

	public static void addToOrderList(String key, JSONObject obj) {
		
		Order old = orderMap.get(key);
		if(old == null)
			orderMap.put(key, new Order(key, obj));
		else
			old.update(obj);
	}

	public static void addToAccountList(String key, JSONObject obj) throws JSONException {
		
			accMap.put(key, obj.getString("code"));
	}

	public static Collection<String> getAccountList() {
		
		return accMap.values();
	}
	
	public static void validateFavourites() {
	
//		loadFavrList();
		
		Iterator<String> itr = instrMap.keySet().iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				instrMap.get(key).favourite = false;
			}

			Iterator<String> setIterator = favrList.iterator();
		while (setIterator.hasNext()) {
		    String currentElement = setIterator.next();
		    Log.w(TAG, "cur = "+currentElement);
		    if (instrMap.get(currentElement) == null) {
		        setIterator.remove();
		        Log.w(TAG, "removed");
		    }
		    else
		    	instrMap.get(currentElement).favourite = true;
		}
	}
	
	public static HashSet<String> getFavrList() {
		
		return favrList;
	}

	public static void setFavrList(HashSet<String> a) {
		
		favrList = a;
	}
	
	public static void saveFavrList() {
		
		FileOutputStream fos;
		try {
			
			fos = app_ctx.openFileOutput(FLIST_FNAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(favrList);
			os.close();
			fos.close();
			
		} catch (FileNotFoundException e) {

			Toast.makeText(app_ctx, "Файл не записан " + e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			
			Toast.makeText(app_ctx, "Файл не записан: " + e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
		Toast.makeText(app_ctx, "Сохранен список инструментов", Toast.LENGTH_SHORT).show();

	}
	
	public static void loadFavrList() {
	   
	FileInputStream fileInputStream;
	try {
		
		fileInputStream = app_ctx.openFileInput(FLIST_FNAME);
		ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
		Object one = oInputStream.readObject();
		favrList = (HashSet<String>) one;
		oInputStream.close();
		fileInputStream.close();
		
	} catch (FileNotFoundException e) {
		
		//e.printStackTrace();
  	   Log.i(TAG, "creates blank. no file " + FLIST_FNAME);
 	   favrList = new HashSet<String>();
 	   
	} catch (StreamCorruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//return favourites;
	}
}
