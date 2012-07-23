package com.vkassin.mtrade;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

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
        
    }

    public JSONObject getLogin() {
    	
      JSONObject msg = new JSONObject();
      try{
        msg.put("objType", Common.LOGIN);
        msg.put("time", Calendar.getInstance().getTimeInMillis());
        msg.put("version", Common.PROTOCOL_VERSION);
        msg.put("password", "1");
        msg.put("login", "CE007");
    
      }
      catch(Exception e){
          e.printStackTrace();
          Log.e(TAG, "Error! Cannot create JSON login object", e);
      }
      return msg;

    }

    public JSONObject getSubscription(ArrayList<Integer> arr) {
    	
        JSONObject msg = new JSONObject();
        try{
          msg.put("objType", Common.SUBSCRIBE);
          msg.put("time", Calendar.getInstance().getTimeInMillis());
          msg.put("version", Common.PROTOCOL_VERSION);
          JSONArray jsonA = new JSONArray(arr);
          msg.put("subscribe_array", jsonA);
      
          Log.i(TAG, "subscr = "+msg);
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
        
    @Override
    public void onResume() {
    	
      super.onResume();
      
      try {

        sock = new Socket("212.19.144.126", 9800);
        //sock = new Socket("192.168.186.129", 9800);

//        r = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//        out = new BufferedWriter(new OutputStreamWriter(sock
//            .getOutputStream()));
   
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
                    	Log.i(TAG, "readMsg: " + data);
                    	try {
                    		
							int t = data.getInt("objType");
                    		Log.i(TAG, "objType: " + t);
							
                    		if( t == Common.LOGIN) {
                    		
    							int s = data.getInt("status");
//    							Log.i(TAG, "Logion status: " + s);
                    			if(s == 0)
                        			pb.setVisibility(View.VISIBLE);
                    			else
                    				pb.setVisibility(View.GONE);

                    		}
                    		
                    		if( t == Common.INSTRUMENT) {
                    			
                    			ArrayList<RSSItem> result = new ArrayList<RSSItem>();
                    			ArrayList<Integer> idresult = new ArrayList<Integer>();
                    			
                    			Iterator<String> keys = data.keys();
//                    			String k = "0";
                    			while( keys.hasNext() ){
                    				String key = (String)keys.next();
//                    				Log.i(TAG, "key = " + key);
//                    				k = key;
                    				if(!key.equals("time") && !key.equals("objType")) {
                    					
                    					result.add(new RSSItem(Integer.parseInt(key), data.getJSONObject(key)));
                    					idresult.add(new Integer(key));
                    				}
                    			}
                    			
                    			writeJSONMsg(getSubscription(idresult));
                    			
                	        	adapter.setItems(result);
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
   
              } catch (Exception e) { }
            }
          }
        });
        thrd.start();
      } catch (Exception ioe) { }
    }
   
    @Override
    public void onPause() {
      super.onPause();
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
   
}
