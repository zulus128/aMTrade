package com.vkassin.mtrade;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class InstrActivity extends Activity {

	private static final String TAG = "MTrade.InstrActivity"; 
	
	private Socket sock;
	private Thread thrd;
	private ListView list;
	private InstrsAdapter adapter;
	private ProgressBar pb;
	
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instrs);
        
        pb = (ProgressBar)findViewById(R.id.ProgressBar01);
//		pb.setVisibility(View.VISIBLE);

        list = (ListView)this.findViewById(R.id.InstrList);
    	adapter = new InstrsAdapter(this, R.layout.instritem, new ArrayList<RSSItem>());
    	list.setAdapter(adapter);
    	
    	list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
			}
		});
     
    	Common.clearInstrList();
    	
    	refresh();
    }

    public JSONObject getLogin() {
    	
      JSONObject msg = new JSONObject();
      try{
        msg.put("objType", Common.LOGIN);
        msg.put("time", Calendar.getInstance().getTimeInMillis());
        msg.put("version", Common.PROTOCOL_VERSION);
//        msg.put("password", "1");
//        msg.put("login", "CE007");
        msg.put("password", "1");
        msg.put("login", "133b06");
    
      }
      catch(Exception e){
          e.printStackTrace();
          Log.e(TAG, "Error! Cannot create JSON login object", e);
      }
      return msg;

    }

    private void writeJSONMsg(JSONObject msg) throws Exception {
    	
        byte[] array = msg.toString().getBytes();
        ByteBuffer buff = ByteBuffer.allocate(array.length + 4);
        buff.putInt(array.length);
        buff.put(array);
       	sock.getOutputStream().write(buff.array());
    }
    
    private static byte[] readMsg(InputStream inStream, int remainingToRead) throws IOException{
        byte[] buffer = new byte[remainingToRead];
        while (remainingToRead != 0) {
           int len = inStream.read(buffer, buffer.length - remainingToRead, remainingToRead);
           remainingToRead -= len;
        }
        return buffer;
     }

    private JSONObject readJSONMsg() throws Exception {
    	
        ByteBuffer buff = ByteBuffer.allocate(4);
        buff.put(readMsg(sock.getInputStream(), 4));
        buff.position(0);
        int pkgSize = buff.getInt();
        String s = new String(readMsg(sock.getInputStream(), pkgSize));
        return new JSONObject(s);
    }
        
    private void sendSubscription() throws Exception {

    	
		Log.w(TAG, "favr1 = " + Common.getFavrList());

        JSONObject msg = new JSONObject();
        try{
          msg.put("objType", Common.SUBSCRIBE);
          msg.put("time", Calendar.getInstance().getTimeInMillis());
          msg.put("version", Common.PROTOCOL_VERSION);
          HashSet<String> t = Common.getFavrList();
          JSONArray jsonA = new JSONArray(t);
          msg.put("subscribe_array", jsonA);
      
          Log.i(TAG, "subscr = "+msg);
        }
        catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "Error! Cannot create JSON login object", e);
        }

        writeJSONMsg(msg);

    }
    
    private void refresh() {
    	
      try {

//        sock = new Socket("212.19.144.126", 9800);
        //sock = new Socket("192.168.186.129", 9800);
        sock = new Socket("192.168.111.12", 9800);

        writeJSONMsg(getLogin());
        
        thrd = new Thread(new Runnable() {
          public void run() {
            while (!Thread.interrupted()) {
              try {
                final JSONObject data = readJSONMsg();
                if (data != null)
                  runOnUiThread(new Runnable() {
//                    @Override
                    public void run() {
                      // do something in ui thread with the data var
//                    	Log.i(TAG, "readMsg: " + data);
                    	try {
                    		
							int t = data.getInt("objType");
							if(t != Common.HEARTBEAT) {

								Log.i(TAG, "objType: " + t);
								Log.i(TAG, "readMsg: " + data);
								
							}
							else
								writeJSONMsg(data);

                    		if( t == Common.LOGIN) {
                    		
    							int s = data.getInt("status");
//    							Log.i(TAG, "Logion status: " + s);
                    			if(s == 0) {
                        			pb.setVisibility(View.VISIBLE);
                        			Common.FIRSTLOAD_FINISHED = false;
                        			Common.loadFavrList();
                    			}
                    			else {
                    				
                    				pb.setVisibility(View.GONE);
                    				Common.validateFavourites();
                    				sendSubscription();
                    				adapter.setItems(Common.getFavInstrs());
                    				adapter.notifyDataSetChanged();
                        			Common.FIRSTLOAD_FINISHED = true;
                        			onResume();
                    			}

                    		}
                    		
                    		if( t == Common.INSTRUMENT) {
                    			
//                    			ArrayList<RSSItem> result = new ArrayList<RSSItem>();
//                    			ArrayList<Integer> idresult = new ArrayList<Integer>();
                    			
                    			Iterator<String> keys = data.keys();
//                    			String k = "0";
                    			while( keys.hasNext() ){
                    				String key = (String)keys.next();
//                    				Log.i(TAG, "key = " + key);
//                    				k = key;
                    				if(!key.equals("time") && !key.equals("objType")) {
                    					
//                    					result.add(new RSSItem(Integer.parseInt(key), data.getJSONObject(key)));
                    					
                    					Common.addToInstrList(key, data.getJSONObject(key));
                    					
//                    					idresult.add(new Integer(key));
                    				}
                    			}
                    			
//                    			writeJSONMsg(getSubscription(idresult));
                    			
//                    			Common.saveInstrList(idresult);
                    			
//                	        	adapter.setItems(result);
                				adapter.notifyDataSetChanged();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.e(TAG, "JSONException problem!!!");
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

                    }
                  });
   
              } catch (Exception e) { e.printStackTrace();}
            }
          }
        });
        thrd.start();
      } catch (Exception ioe) { ioe.printStackTrace();}
    }
   
    @Override
    public void onResume() {
    	
      super.onResume();
      Log.i(TAG, "onResume");
      
      if(Common.FIRSTLOAD_FINISHED) {
    	  
//  		Log.w(TAG, "favr2 = " + Common.getFavrList());

      Common.validateFavourites();
      
//		Log.w(TAG, "favr3 = " + Common.getFavrList());

		try {
			
			sendSubscription();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adapter.setItems(Common.getFavInstrs());
		adapter.notifyDataSetChanged();
      }
      
    }
    
    @Override
    public void onStop() {
    
    	super.onStop();
//    	Common.saveFavrList();
    }
    
    @Override
    public void onDestroy() {
    	
      super.onDestroy();

  	Common.saveFavrList();

      
      if (thrd != null)
        thrd.interrupt();
      try {
        if (sock != null) {
          sock.getOutputStream().close();
          sock.getInputStream().close();
          sock.close();
        }
      } catch (IOException e) {}
      thrd = null;
    }
   
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.instrsmenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
	        case R.id.menuselect: 
	        	
	        	Intent intent = new Intent(this, SelectListView.class);
	            startActivityForResult(intent, 0);
	            break;
	               
	        case R.id.menuorder: 
	        	
	        	Context mContext = getApplicationContext();
	        	Dialog dialog = new Dialog(mContext);

	        	dialog.setContentView(R.layout.order_dialog);
	        	dialog.setTitle("Order Dialog");

	        	TextView text = (TextView) dialog.findViewById(R.id.ordertext);
	        	text.setText("Hello, this is a custom dialog!");
//	        	ImageView image = (ImageView) dialog.findViewById(R.id.orderimage);
//	        	image.setImageResource(R.drawable.android);

	        	dialog.show();
	        	
//	        	AlertDialog.Builder builder;
//	        	AlertDialog alertDialog;
//
//	        	Context mContext = getApplicationContext();
//	        	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//	        	View layout = inflater.inflate(R.layout.order_dialog,
//	        	                               (ViewGroup) findViewById(R.id.layout_root));
//
//	        	TextView text = (TextView) layout.findViewById(R.id.ordertext);
//	        	text.setText("Hello, this is a custom dialog!");
////	        	ImageView image = (ImageView) layout.findViewById(R.id.image);
////	        	image.setImageResource(R.drawable.android);
//
//	        	builder = new AlertDialog.Builder(mContext);
//	        	builder.setView(layout);
//	        	alertDialog = builder.create();
	        	
	        	break;
	    }
	    return true;
	}
}
