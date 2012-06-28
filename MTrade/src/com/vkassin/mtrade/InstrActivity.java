package com.vkassin.mtrade;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Calendar;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class InstrActivity extends Activity {

	private static final String TAG = "MTrade.InstrActivity"; 
	
	private Socket sock;
	private Thread thrd;
	  
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
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
          Log.e(TAG, "Error! Cannot create JSON login objext", e);
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
