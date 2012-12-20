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
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;//
import android.widget.TextView;
import android.widget.Toast;

public class Common {

	private static final String TAG = "MTrade.Common"; 

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
    public final static Integer POSITIONS_INFO = 109;
    public final static String PROTOCOL_VERSION = "1.0";
    public final static int ERROR_USER_WAS_NOT_FOUND = 200;
    public final static int ERROR_USER_ALREADY_CONNECTED = 201;
    public final static int ERROR_PASSWORD_ERROR = 202;
    public final static int ERROR_WRONG_PROTOCOL_VERSION = 203;
    public final static int ERROR_LOGIN_INFORMATION = 204;

    public static TabHost tabHost;
	public static TabHost.TabSpec tabspec;

	private static Instrument selectedInstrument;

	private static int ordernum;
	public static Context app_ctx;
	private static final String FLIST_FNAME = "favr_list";
	private static final String ACCOUNT_FNAME = "myacc";
	
	private static HashMap<String, Instrument> instrMap = new HashMap<String, Instrument>();
	private static HashMap<String, History> historyMap = new HashMap<String, History>();
	private static HashSet<String> favrList = new HashSet<String>();
	private static HashMap<String, String> accMap = new HashMap<String, String>();
	private static HashMap<String, Position> posMap = new HashMap<String, Position>();
	
	public static HashMap<String, String> myaccount = new HashMap<String, String>();

    public static boolean FIRSTLOAD_FINISHED = false;
    public static boolean loginFromDialog = false;
    public static InstrActivity mainActivity;
    public static QuoteActivity quoteActivity;
    public static HistoryActivity historyActivity;
    public static ChartActivity chartActivity;
    public static PosActivity posActivity;
    
	public static Instrument getSelectedInstrument() {
		return selectedInstrument;
	}

	public static void setSelectedInstrument(Instrument selectedInstrument) {
		
		Log.i(TAG, "Instr selected = " + selectedInstrument.symbol);
		Common.selectedInstrument = selectedInstrument;
		try {
			mainActivity.sendQuoteGraphSubscription();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
//			Log.i(TAG, "key = " + key + " instr = " + instid);
			
			Instrument i = getInstrById(instid);
			if(i != null)
				i.addDayChartElement(key, obj);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static ArrayList<Position> getAllPositions() {
		
		ArrayList<Position> arr = new ArrayList<Position>(posMap.values());
		Log.w(TAG, "pos_cnt = " + arr.size());
		return arr;
	}

	public static void clearPositionList() {
		
		posMap.clear();
	}

	public static void addPositionToList(String key, JSONObject obj) {
		
		Position old = (Position)posMap.get(key);
		if(old == null)
			posMap.put(key, new Position(key, obj));
		else
			old.update(obj);
	}

	public static ArrayList<History> getAllHistory() {

		SortedSet<History> hists = new TreeSet<History>(historyMap.values());
		return new ArrayList<History>(hists);

//		return new ArrayList<History>(historyMap.values());
	}

	public static void clearHistoryList() {
		
		historyMap.clear();
	}

	public static void addOrderToHistoryList(String key, JSONObject obj) {
		
		Order old = (Order)historyMap.get(key);
		if(old == null)
			historyMap.put(key, new Order(key, obj));
		else
			old.update(obj);
	}

	public static void addDealToHistoryList(String key, JSONObject obj) {
		
		Deal old = (Deal)historyMap.get(key);
		if(old == null)
			historyMap.put(key, new Deal(key, obj));
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
//		    Log.w(TAG, "cur = "+currentElement);
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
		
		Log.i(TAG, "saveFavrList()");
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
		
//		Toast.makeText(app_ctx, "Сохранен список инструментов", Toast.LENGTH_SHORT).show();

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
	
	public static void saveAccountDetails() {
		
		FileOutputStream fos;
		try {
			
			fos = app_ctx.openFileOutput(ACCOUNT_FNAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(myaccount);
			os.close();
			fos.close();
			Log.i(TAG, "saved username: " + myaccount.get("name") + " password: " + myaccount.get("password"));
		} catch (FileNotFoundException e) {

			Toast.makeText(app_ctx, "Файл не записан " + e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			
			Toast.makeText(app_ctx, "Файл не записан: " + e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		
	}
	
	public static void loadAccountDetails() {
		   
	FileInputStream fileInputStream;
	try {
		
		fileInputStream = app_ctx.openFileInput(ACCOUNT_FNAME);
		ObjectInputStream oInputStream = new ObjectInputStream(fileInputStream);
		Object one = oInputStream.readObject();
		myaccount = (HashMap<String, String>) one;
		oInputStream.close();
		fileInputStream.close();
		
	} catch (FileNotFoundException e) {
		
		//e.printStackTrace();
  	   Log.i(TAG, "No account file " + ACCOUNT_FNAME);
 	   
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

	public static void login(Context ctx) {
		
    	final Dialog dialog = new Dialog(ctx);
    	dialog.setContentView(R.layout.login_dialog);
    	dialog.setTitle(R.string.LoginDialogTitle);

    	final EditText nametxt = (EditText) dialog.findViewById(R.id.loginnameedit);
    	final EditText passtxt = (EditText) dialog.findViewById(R.id.passwordedit);

    	Button customDialog_Dismiss = (Button)dialog.findViewById(R.id.gologin);
    	customDialog_Dismiss.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
    			 
    			 
    			 JSONObject msg = new JSONObject();
    		      try{

    		    	  msg.put("objType", Common.LOGOUT);
    		    	  msg.put("time", Calendar.getInstance().getTimeInMillis());
    		    	  msg.put("version", Common.PROTOCOL_VERSION);
    		    	  msg.put("status", 1);
   		        	mainActivity.writeJSONMsg(msg);
    		      }
    		      catch(Exception e){
    		          e.printStackTrace();
    		          Log.e(TAG, "Error! Cannot create JSON logout object", e);
    		      }
    		      
    			 myaccount.put("name", nametxt.getText().toString());
    			 myaccount.put("password", passtxt.getText().toString());

    				Log.i(TAG, "myaccount username: " + myaccount.get("name") + " password: " + myaccount.get("password"));

    			 dialog.dismiss(); 
    			 mainActivity.stop();
//    			 saveAccountDetails();
    			 try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			 loginFromDialog = true;
    			 mainActivity.refresh();
    		 }
    		    
    	});
    	
    	dialog.show();
	}
	
	public static void putOrder(final Context ctx, Quote quote) {
		
		final Instrument it = Common.selectedInstrument;// adapter.getItem(selectedRowId);
    	
    	final Dialog dialog = new Dialog(ctx);
    	dialog.setContentView(R.layout.order_dialog);
    	dialog.setTitle(R.string.OrderDialogTitle);

    	TextView itext = (TextView) dialog.findViewById(R.id.instrtext);
    	itext.setText(it.symbol);

    	final Spinner aspinner = (Spinner) dialog.findViewById(R.id.acc_spinner);
    	List<String> list = new ArrayList<String>(Common.getAccountList());
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Common.app_ctx,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	aspinner.setAdapter(dataAdapter);
    	
    	final EditText pricetxt = (EditText) dialog.findViewById(R.id.priceedit);
    	final EditText quanttxt = (EditText) dialog.findViewById(R.id.quantedit);
    	final RadioButton bu0 = (RadioButton) dialog.findViewById(R.id.radio0);
    	final RadioButton bu1 = (RadioButton) dialog.findViewById(R.id.radio1);

    	if(quote != null) {
    		
    		pricetxt.setText(quote.price.toString());
    		if(quote.qtyBuy > 0) {
    			
    			quanttxt.setText(quote.qtyBuy.toString());
    			bu1.setChecked(true);
    			bu0.setChecked(false);
    			
    		}
    		else {

    			quanttxt.setText(quote.qtySell.toString());
    			bu1.setChecked(false);
    			bu0.setChecked(true);
    			
    		}
    	}

    	Button customDialog_Cancel = (Button)dialog.findViewById(R.id.cancelbutt);
    	customDialog_Cancel.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
    			 dialog.dismiss(); 
    		 }
    		    
    	});

    	
    	
    	Button customDialog_Put = (Button)dialog.findViewById(R.id.putorder);
    	customDialog_Put.setOnClickListener(new Button.OnClickListener(){
    		 public void onClick(View arg0) {
    			 
				 Double price = new Double(0);
				 Long qval = new Long(0);
				 
    			 try {
    				 
    				 price = Double.valueOf(pricetxt.getText().toString());
    			 }
  		       catch(Exception e){
  		    	 
  		    	   Toast.makeText(ctx, R.string.CorrectPrice, Toast.LENGTH_SHORT).show();
  		    	   return;
		       }					
    			 try {
    				 
    				  qval = Long.valueOf(quanttxt.getText().toString());
    			 }
  		       catch(Exception e){
  		    	 
  		    	   Toast.makeText(ctx, R.string.CorrectQty, Toast.LENGTH_SHORT).show();
  		    	   return;
		       }					
    			 
    			 JSONObject msg = new JSONObject();
    		       try{
    		    	   
    		         msg.put("objType", Common.CREATE_REMOVE_ORDER);
    		         msg.put("time", Calendar.getInstance().getTimeInMillis());
    		         msg.put("version", Common.PROTOCOL_VERSION);
    		         msg.put("instrumId", Long.valueOf(it.id));
    		         msg.put("price", price);
    		         msg.put("qty", qval);
    		         msg.put("ordType", 1);
    		         msg.put("side", bu0.isChecked()?0:1);
    		         msg.put("code", String.valueOf(aspinner.getSelectedItem()));
    		         msg.put("orderNum", ++ordernum);
    		         msg.put("action", "CREATE");
    		     
    		         mainActivity.writeJSONMsg(msg);

    		       }
    		       catch(Exception e){
    		    	   
    		           e.printStackTrace();
    		           Log.e(TAG, "Error! Cannot create JSON order object", e);
    		       }
    		       
    			 dialog.dismiss(); 
    		 }
    		    
    	});
    	
    	dialog.show();
    	
    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.FILL_PARENT;
        dialog.getWindow().setAttributes(lp);
    }
	
}
