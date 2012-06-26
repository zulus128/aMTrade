package com.vkassin.mtrade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class InstrActivity extends Activity {

	private static final String TAG = "MTrade.InstrActivity"; 
	
	private Socket sock;
	private BufferedReader r;
	private BufferedWriter out;
	private Thread thrd;
	  
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
        
//        JSONObject json = new JSONObject();
//        try{
//        json.put("time", Calendar.getInstance().getTimeInMillis());
//        json.put("objType", new Integer(Common.INSTRUMENT));
//        
//        }
//        catch(Exception e){
//            e.printStackTrace();
//            Log.(TAG, "Error! Cannot Estabilish Connection", e);
//        }
    }

    public HashMap<String, Object> getLogin() {
    	
        HashMap<String, Object> msg;
        msg = new HashMap<String, Object>();
        msg.put("objType", Common.LOGIN);
        msg.put("time", Calendar.getInstance().getTimeInMillis());
        msg.put("version", Common.PROTOCOL_VERSION);
        msg.put("password", "1");
        msg.put("login", "CE007");
        return msg;
     }

    public void writeDelimitedTo(OutputStream outStream, HashMap<String, Object> msg) throws Exception {
    	
        ObjectMapper mapper = new ObjectMapper();
        byte[] array = mapper.writeValueAsBytes(msg);
        ByteBuffer buff = ByteBuffer.allocate(array.length + 4);
        buff.putInt(array.length);
        buff.put(array);
        outStream.write(buff.array());
     }

    @Override
    public void onResume() {
    	
      super.onResume();
      
      try {

        sock = new Socket("212.19.144.126", 9800);
        //sock = new Socket("192.168.186.129", 9800);

        r = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(sock
            .getOutputStream()));
   
        thrd = new Thread(new Runnable() {
          public void run() {
            while (!Thread.interrupted()) {
              try {
                final String data = r.readLine();
                if (data != null)
                  runOnUiThread(new Runnable() {
//                    @Override
                    public void run() {
                      // do something in ui thread with the data var
                    }
                  });
   
              } catch (IOException e) { }
            }
          }
        });
        thrd.start();
      } catch (IOException ioe) { }
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
   
//    private void sendText() {
//      String text = "test text";
//      try {
//        out.write(text + "\n");
//        out.flush();
//      } catch (IOException e) {}
//    }
}
