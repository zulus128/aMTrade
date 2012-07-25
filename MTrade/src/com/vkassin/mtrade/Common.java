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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    public final static String PROTOCOL_VERSION = "1.0";
    public final static int ERROR_USER_WAS_NOT_FOUND = 200;
    public final static int ERROR_USER_ALREADY_CONNECTED = 201;
    public final static int ERROR_PASSWORD_ERROR = 202;
    public final static int ERROR_WRONG_PROTOCOL_VERSION = 203;
    public final static int ERROR_LOGIN_INFORMATION = 204;

	public static TabHost tabHost;

	public static Context app_ctx;
	private static final String FLIST_FNAME = "favr_list";
	
	private static HashMap<String, RSSItem> instrList = new HashMap<String, RSSItem>();
	private static HashSet<String> favrList;

	public static void clearInstrList() {
	
		instrList.clear();
	}

	public static void addToInstrList(String key, JSONObject obj) {
		
		RSSItem old = instrList.get(key);
		if(old == null)
			instrList.put(key, new RSSItem(key, obj));
		else
			old.update(obj);
	}
	
	public static void validateFavourites() {
	
		    Iterator<String> setIterator = favrList.iterator();
		while (setIterator.hasNext()) {
		    String currentElement = setIterator.next();
		    if (instrList.get(currentElement) == null) {
		        setIterator.remove();
		    }
		    else
		    	instrList.get(currentElement).favourite = true;
		}
	}
	
	public static HashSet<String> getFavrList() {
		
		return favrList;
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
