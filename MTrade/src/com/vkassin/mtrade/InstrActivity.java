package com.vkassin.mtrade;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class InstrActivity extends Activity {

	private static final String TAG = "MTrade.InstrActivity"; 

	private static final int CONTEXTMENU_PUTORDER = 1;
	private static final int CONTEXTMENU_GOGLASS = 2;
	private static final int CONTEXTMENU_GOCHART = 3;
//	private static final int CONTEXTMENU_RELOGIN = 3;
	private int selectedRowId;
	
	public static final int CREATE_PROGRESS_DIALOG = 151;
	public static final int DISMISS_PROGRESS_DIALOG = 152;
	public static final int CREATE_MESSAGE_DIALOG = 153;

    private final int HEARTBEAT_TOCLIENT_DELAY = 30000;

	private boolean onstart = false;
   
	private Timer hbTimer = new Timer();

//	private boolean onActivityResultCalledBeforeOnResume;

//	private Socket sock;
	private SSLSocket sock;
	private SSLContext sslcontext;
	
	private Thread thrd;
	public ListView list;
	private InstrsAdapter adapter;
	private ProgressBar pb;
	private Button customDialog_Dismiss;
//	private static int ordernum;
	private LinearLayout header;
	
private static enum ERROR_STAT {
		
    err_NoPermissions(1000),
    err_NoRights(1304),
    err_OrderDisabled(1400),
    err_OrderTypeDisabled(1401),
    err_OrderCheckLot(1402),
    err_OrderCheckMax(1403),
    err_OrderPriceStep(1404),
    err_InstrDispLastDealPrice(1406),
    err_InstrumentBlocked(1414),
    err_TrdAccLimit(1501),
    err_TradeAccountBlocked(1503),
    err_CashAccLimit(1550),
    err_CashAccOpenPosLimit(1551),
    err_OrderWrongTrdAccID(1700),
    err_OrderAlienFirmCashAcc(1701),
    err_OrderNoRightsOnTrdAcc(1702),
    err_OrderNoRightsOnCashAcc(1703),
    err_OrderWrongExpireDate(1710),
    err_OrderWrongExpireTime(1711),
    err_CmdBadSatisfyOrderID(1712),
    err_OrderWrongDirection(1713),
    err_OrderWrongPrice(1714),
    err_OrderWrongQuantity(1715),
    err_OrderWrongKredId(1716),
    err_OrderWrongFirm(1717),
    err_OrderWrongPriceOrQuantity(1718),
    err_OrderWrongRazdel(1719),
    err_CmdBadInstrumentID(2000);
		
    ERROR_STAT(int num) {
    
    	number = num;
    }
    
    private int number;
    
	public static String fromNumber(Long n) {

	      for (ERROR_STAT suit : EnumSet.allOf(ERROR_STAT.class)) {
	    	  
	    	  if(suit.number == n) {
	    		  
	  	    	int i = Common.app_ctx.getResources().getIdentifier(suit.toString(), "string", Common.app_ctx.getPackageName());
		    	if(i == 0)
		    		return "_";
		    	return Common.app_ctx.getResources().getString(i);
	    	  }
	      }
	      
	      return "+";
		 
	}
	    	
};
	
public Handler handler = new Handler(){
	
	AlertDialog dialog = null;
    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
        case CREATE_MESSAGE_DIALOG:
        	new AlertDialog.Builder(InstrActivity.this).setMessage(msg.getData().getString("msg")).show();
        	break;
        case CREATE_PROGRESS_DIALOG:

        	if(Common.inLogin)
        		break;
        	
        	if(dialog != null)
        		dialog.dismiss();
        	//create the dialog
        	AlertDialog.Builder bu = new AlertDialog.Builder(InstrActivity.this).setMessage(R.string.ConnectError1)
			.setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					Common.login(InstrActivity.this);
				}
			})
//						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int id) {
//					
////					stop();
//		        	dialog = null;
//
//				}
//			});

			;

        	dialog = bu.create();

//			Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//			if(b != null)
//				b.
			
			dialog.show();

            break;
            
        case DISMISS_PROGRESS_DIALOG:
        	
        	if(dialog != null)
        		dialog.dismiss();
        	
        	dialog = null;
        	
        	break;
        	

        }
    }
};

    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instrs);

        Common.mainActivity = this;
        
        header = (LinearLayout) findViewById(R.id.LinLayout02);

        pb = (ProgressBar)findViewById(R.id.ProgressBar01);
//		pb.setVisibility(View.VISIBLE);

        list = (ListView)this.findViewById(R.id.InstrList);
    	adapter = new InstrsAdapter(this, R.layout.instritem, new ArrayList<Instrument>());
    	
//    	list.addHeaderView(getLayoutInflater().inflate(R.layout.instritem, null), null, false);
    	list.setAdapter(adapter);
    	
    	registerForContextMenu(list);

//    	Common.selectedListItem = getIntent().getIntExtra("PositionInList", -1);
    	
    	list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
//				adapter.setSelectedPosition(arg2);				
//	    	    Common.setSelectedInstrument( adapter.getItems().get(arg2) );
	    	    list.showContextMenuForChild(arg1);

			}
		});

       	Common.tabActivity.setTitle(getResources().getString(R.string.app_name));

       	
       	InputStream caInput = null;
//       	Certificate ca = null;
       	
    try {
  	
    	CertificateFactory cf = CertificateFactory.getInstance("X.509");
//    	caInput = new BufferedInputStream(new FileInputStream("server_pub.crt"));
    	caInput = new BufferedInputStream(this.getAssets().open("server_pub.crt"));
    	Certificate ca = cf.generateCertificate(caInput);
    	System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

    	 // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        sslcontext = SSLContext.getInstance("TLS");
        sslcontext.init(null, tmf.getTrustManagers(), null);

    } catch (CertificateException e) {
		e.printStackTrace();
	} 
	catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	catch (IOException e) {
		e.printStackTrace();
	} catch (KeyStoreException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NoSuchAlgorithmException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     finally {
         try {
			caInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
    
    
    
    }

    public JSONObject getLogin() {
    	
    	
       	Common.tabActivity.setTitle(getResources().getString(R.string.app_name));


    	if(!Common.loginFromDialog)
    		Common.loadAccountDetails();
    	
    	Common.loginFromDialog = false;
    	
      JSONObject msg = new JSONObject();
      try{
        msg.put("objType", Common.LOGIN);
        msg.put("time", Calendar.getInstance().getTimeInMillis());
        msg.put("version", Common.PROTOCOL_VERSION);

        String name = Common.myaccount.get("name");
        String password = Common.myaccount.get("password");


        if((name == null) || (password == null)) {

        	Common.login(this);
        	return null;
        }

      msg.put("password", password);
      msg.put("login", name);
        
//        msg.put("password", "1");
//        msg.put("login", "133b06");

      msg.put("sign", Common.sign(name, password));

      Log.i(TAG, "Login message: " + msg);

      }
      catch(Exception e){
          e.printStackTrace();
          Log.e(TAG, "Error! Cannot create JSON login object", e);
      }
      return msg;

    }

    public void writeJSONMsg(JSONObject msg) throws Exception {
    	
//    	Log.i(TAG, "writeJSONMsg:" + msg.toString());
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
//        Log.i(TAG, "size = "+pkgSize);
        String s = new String(readMsg(sock.getInputStream(), pkgSize));
        return new JSONObject(s);
    }
        
    private void sendSubscription() throws Exception {

    	
//		Log.w(TAG, "favr1 = " + Common.getFavrList());

        JSONObject msg = new JSONObject();
        try{
          msg.put("objType", Common.SUBSCRIBE);
          msg.put("time", Calendar.getInstance().getTimeInMillis());
          msg.put("version", Common.PROTOCOL_VERSION);
          HashSet<String> t = Common.getFavrList();
          
          if(t.size() < 1)
        	  return;
          
          JSONArray jsonA = new JSONArray(t);
          msg.put("subscribe_array", jsonA);
      
          Log.i(TAG, "subscr = "+msg);
        }
        catch(Exception e) {
        	
            e.printStackTrace();
            Log.e(TAG, "Error! Cannot create JSON login object", e);
        }

        writeJSONMsg(msg);

//        sendGraphSubscription();
    }

    public void sendQuoteGraphSubscription() {

        try {
        	
//        	HashSet<String> t = Common.getFavrList();
        	
        	String id = Common.getSelectedInstrument().id;
        	
//            for(String id : t) {
            	
//            	Log.i(TAG, "f: " + id);
                JSONObject msg = new JSONObject();
            	msg.put("objType", Common.QUOTE_CHART_SUBSCRIPTION);
            	msg.put("time", Calendar.getInstance().getTimeInMillis());
            	msg.put("version", Common.PROTOCOL_VERSION);
            	msg.put("instrumId", Long.valueOf(id));
      
             	Log.i(TAG, "chart subscr = " + msg);
             	writeJSONMsg(msg);
//            }
        }
        catch(Exception e) {
        	
            e.printStackTrace();
            Log.e(TAG, "Error! Cannot create JSON login object", e);
        }

   
    }
    
    private void resetHBTimer() {

        hbTimer.cancel();
        hbTimer = new Timer();

        hbTimer.schedule(new TimerTask() {

            public void run() {

          	Log.i(TAG, "-- NO HEARTBEAT!!");
            	
            stop();
        	  
          	  if(!Common.paused)
          		  handler.sendMessage(Message.obtain(handler,
          	            CREATE_PROGRESS_DIALOG));
            }
        },
        HEARTBEAT_TOCLIENT_DELAY, HEARTBEAT_TOCLIENT_DELAY);

    }
    
    public void stop() {
    	
    	Log.w(TAG, " --- Stop!!!");
    	
    	
    	Common.connected = false;
    	
		if(thrd != null)
			thrd.interrupt();
		
        hbTimer.cancel();

		if(sock != null) {
			
			try {
				sock.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sock = null;
		}
    }
    
    public boolean refresh() {
     	
    	Log.w(TAG, " --- Refresh!!!");
  	  
    	if((sock != null) && sock.isConnected())
    		return true;

    	if(Common.connected)
    		return true;

    	try {

//        sock = new Socket(Common.ip_addr, Common.port_login);

//    		SocketFactory sf = SSLSocketFactory.getDefault();
//    		sock = (SSLSocket) sf.createSocket(Common.ip_addr, Common.port_login_ssl);	

    		sock = (SSLSocket) (sslcontext.getSocketFactory()).createSocket(Common.ip_addr, Common.port_login_ssl);	
    		
    		
        JSONObject login = getLogin();
        if(login == null)
        	return false;
        
        writeJSONMsg(login);
        
        thrd = new Thread(new Runnable() {
          public void run() {
//          	Log.w(TAG, " --- Start!!!");
        	  
              Common.connected = true;

            while (!Thread.interrupted()) {
              try {
                final JSONObject data = readJSONMsg();
                if (data != null)
                  runOnUiThread(new Runnable() {
//                    @Override
                    public void run() {

//                      	Log.w(TAG, " --- getData!!!");
//        				new AlertDialog.Builder(InstrActivity.this).setMessage(R.string.ConnectError1)
//        				.setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
//        					public void onClick(DialogInterface dialog, int id) {
//        					}
//        				})
//        				.show();

                    	try {
                    		
							int t = data.getInt("objType");
							if(t != Common.HEARTBEAT) {

								Log.i(TAG, "objType: " + t);
								Log.i(TAG, "readMsg: " + data);
								
							}
							else {

//								Log.i(TAG, "---!!! Heartbeat !!!");

								resetHBTimer();

								writeJSONMsg(data);
								
							}

                    		if( t == Common.LOGIN) {
                    		
    							int s = data.getInt("status");
    							Log.e(TAG, "Logion status: " + s);
                    			if(s == 0) {

                    		        String name = Common.myaccount.get("name");
                    		       	Common.tabActivity.setTitle(getResources().getString(R.string.app_name) + " " + name);

                    				
                        			Common.clearInstrList();
                        			Common.clearAccountList();
                        			Common.clearPositionList();
                        			Common.clearHistoryList();
                        			Common.clearMessageList();
//                        			Common.setSelectedInstrument(null);
                        			
//                        			Common.clearFavrList();
                        			
                        			if(Common.historyActivity != null)
                        				Common.historyActivity.refresh();
                        			if(Common.posActivity != null)
                        				Common.posActivity.refresh();

                    				
//                    				header.setVisibility(View.GONE);
                        			pb.setVisibility(View.VISIBLE);
                        			Common.FIRSTLOAD_FINISHED = false;
                        			Common.loadFavrList();
                        			
                        	        String name1 = Common.myaccount.get("name");
                        	        if(!Common.oldName.equals(name1))
                        	        	Common.clearFavrList();

                        			Common.saveAccountDetails();
                        			
                    			}
                    			else 
                    			if(s == 1) {
                    				
                        			Common.FIRSTLOAD_FINISHED = true;

                        			resetHBTimer();

                    				
//                    				header.setVisibility(View.VISIBLE);
                    				pb.setVisibility(View.GONE);
                    				Common.validateFavourites();
                    				sendSubscription();
                    				adapter.setItems(Common.getFavInstrs());
                    				adapter.notifyDataSetChanged();

                        			
                        			onResume1();
                    			}
                    			else 
                    			if((s == 200)||(s == 202)) {
                    			
                    				Toast.makeText(InstrActivity.this, R.string.LoginFailed200202, Toast.LENGTH_LONG).show();
                    				Common.login(InstrActivity.this);

                    			}
                        		else 
                            	if(s == 201) {
                            				
                    				Toast.makeText(InstrActivity.this, R.string.LoginFailed201, Toast.LENGTH_LONG).show();
                    				Common.login(InstrActivity.this);

                            	}
                    			else {
                    				
                    				Toast.makeText(InstrActivity.this, R.string.LoginFailed, Toast.LENGTH_LONG).show();
                    				Common.login(InstrActivity.this);
                    			}

                    		}
                    		else
                    		if( t == Common.LOGOUT) {
                    			
                    			Log.i(TAG, "LOGOUT received!");
//                    			sock.close();
//                    			sock = null;
//                    			thrd.interrupt();
                    				
                    		}
                    		else
                    		if( t == Common.INSTRUMENT) {
                    			
                    			Iterator<String> keys = data.keys();
                    			while( keys.hasNext() ){
                    				String key = (String)keys.next();
                    				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                    					Common.addToInstrList(key, data.getJSONObject(key));
                    				}
                    			}
                				adapter.notifyDataSetChanged();
							}
                    		else
                        		if( t == Common.QUOTE) {
                        			
                        			Iterator<String> keys = data.keys();
                        			if(Common.getSelectedInstrument() != null)
                        			while( keys.hasNext() ) {
                        				String key = (String)keys.next();
                        				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                        					
//                        					Instrument instr = Common.getInstrById(data.getJSONObject(key).getLong("instrId"));
                        					Instrument instr = Common.getSelectedInstrument();
                        					instr.modifyQuoteList(key, data.getJSONObject(key));
                        				}
                        			}
                    				
                        			if(Common.quoteActivity != null)
                        				Common.quoteActivity.refresh();
                        			
//                        			adapter.notifyDataSetChanged();
    							}
                    		else
                        		if( t == Common.CHART) {
                        			
                        			Iterator<String> keys = data.keys();
                        			while( keys.hasNext() ){
                        				String key = (String)keys.next();
                        				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                        					Common.addToCharts(key, data.getJSONObject(key));
                        				}
                        			}

                        			Log.i(TAG, "Chart message processed.");

                        			if(Common.chartActivity != null)
                        				Common.chartActivity.refresh();

//                    				adapter.notifyDataSetChanged();
    							}
                        		else
                            		if(t == Common.DEAL) {
                            			
                            			Iterator<String> keys = data.keys();
                            			while( keys.hasNext() ){
                            				String key = (String)keys.next();
                            				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                            					Common.addDealToHistoryList(key, data.getJSONObject(key));
                            				}
                            			}

                            			if(Common.historyActivity != null)
                            				Common.historyActivity.refresh();
        							}
                    		else
                        		if(t == Common.TRANSIT_ORDER) {
                        			
                        			Iterator<String> keys = data.keys();
                        			while( keys.hasNext() ){
                        				String key = (String)keys.next();
                        				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                        					Common.addOrderToHistoryList(key, data.getJSONObject(key));
                        				}
                        			}
                        			if(Common.historyActivity != null)
                        				Common.historyActivity.refresh();
    							}
                        		else
                            		if(t == Common.CREATE_REMOVE_ORDER) {
                            			
                       					String s = data.getString("status");
//                       					String s1 = data.getString("action");
                       					Long err = Long.valueOf(s.split(":")[1]);
                       					if (err == 0) {
                          				
                       						Toast.makeText(InstrActivity.this, R.string.TransitStatusOk, Toast.LENGTH_LONG).show();
                       					}
                       					else {
                       				    	
                       						int i = Common.app_ctx.getResources().getIdentifier("TransitStatusError", "string", Common.app_ctx.getPackageName());
                       				    	String ss = (i == 0)?"000":Common.app_ctx.getResources().getString(i);
//                       						Toast.makeText(InstrActivity.this, ss + " " + err, Toast.LENGTH_LONG).show();
                       				    	String message = ss + " " + ERROR_STAT.fromNumber(err) + "(" + err + ")";
                       						new AlertDialog.Builder(InstrActivity.this).setMessage(message).show();


                       					}
                       						

                            			if(Common.historyActivity != null)
                            				Common.historyActivity.refresh();
        							}
                            		else
                                		if(t == Common.POSITIONS_INFO) {
                                			
                                			Iterator<String> keys = data.keys();
                                			while( keys.hasNext() ){
                                				String key = (String)keys.next();
                                				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                                					Common.addPositionToList(key, data.getJSONObject(key));
                                				}
                                			}

                                			if(Common.posActivity != null)
                                				Common.posActivity.refresh();
            							}
                                		else
                                    		if(t == Common.MSG_TYPE_TS_MESSAGE) {
                                    			
                                    			Iterator<String> keys = data.keys();
                                    			while( keys.hasNext() ){
                                    				String key = (String)keys.next();
                                    				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                                    					
                                    					Mess m = Common.addMessageToList(key, data.getJSONObject(key));
                                    					
                                    					if(Common.FIRSTLOAD_FINISHED) {
                                    					
                                    						Message msg = Message.obtain(handler, CREATE_MESSAGE_DIALOG);
	                                    					String s = m.from + ": " + m.msg;
	                                    					Bundle b = new Bundle();
	                                    					b.putString("msg", s);
	                                    					msg.setData(b);
	                                  	            	  	handler.sendMessage(msg);
                                    					}
                                    				}
                                    			}

                                    			if(Common.mesActivity != null)
                                    				Common.mesActivity.refresh();
                							}
                    		else
                    		if( t == Common.TRADEACCOUNT) {
                    			
                    			Iterator<String> keys = data.keys();
                    			while( keys.hasNext() ){
                    				String key = (String)keys.next();
                    				if(!key.equals("time") && !key.equals("objType")&& !key.equals("version")) {
                    			
                    					Common.addToAccountList(key, data.getJSONObject(key));
                    				}
                    			}
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
   
              } catch (Exception e) {
            	  
            	  if(Common.connected) {
            		  
	            	  e.printStackTrace();
	
	            	  stop();
	            	  
	            	  if(!Common.paused)
	            		  handler.sendMessage(Message.obtain(handler,
	            	            CREATE_PROGRESS_DIALOG));
            	  }
            	  
            	  
              }
              
            }
            

          }
        });
        
        thrd.start();
        
      }        
//      catch (java.net.ConnectException ce) {
//    	  
//    	  ce.printStackTrace();
//    	  
//      }
      catch (Exception ioe) {
    	  
    	  ioe.printStackTrace();

    	  Toast toast = Toast.makeText(InstrActivity.this, R.string.ConnectError, Toast.LENGTH_LONG);
//    	  toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
    	  toast.show();

    	  stop();

    	  Common.login(InstrActivity.this);

    	  return false;
      }
//    }//while(true)
      return true;
  }
   
    private void onResume1() {
    	
        if(Common.FIRSTLOAD_FINISHED) {
      	  
//    		Log.w(TAG, "favr2 = " + Common.getFavrList());

        Common.validateFavourites();
        
//  		Log.w(TAG, "favr3 = " + Common.getFavrList());

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
    public void onResume() {
    	
      super.onResume();

//      PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//      boolean isScreenOn = pm.isScreenOn();
      
//      Log.e(TAG, "--- onResume " + Common.activities + "Common.confChanged = " + Common.confChanged );// + isApplicationBroughtToBackground(this));
      
//      Common.activities++;

//      if(!onstart) {
//    	  
//	  	Common.loadAccountDetails();
//		
//	  	if(Common.confChanged) {
//	  		
//	  		Common.confChanged = false;
//	  	}
//	  	else {
//	  		
////	  		if(Common.activities == 0)
//	  			Common.login(this);
//	  	}
//      }
//      onstart = false;

//      Common.loadAccountDetails();
//	
//  	if(Common.confChanged) {
//  		
//  		Common.confChanged = false;
//  	}
//  	else {
//  		
//  		if(Common.paused)
//  			Common.login(this);
//  	}
//
//  Common.paused = false;
//	Common.paused1 = false;

      
  	  onResume1();
  		
//      Common.activities++;

    }
    
    @Override
    public void onPause() {
    	
    	super.onPause();
    	Common.saveFavrList();
// 		Log.e(TAG, "++++++++++++ onPause " + Common.activities);

//		Common.activities--;

    }

    @Override
    public void onDestroy() {
    	
      super.onDestroy();

      Common.inLogin = false;
      
//      Log.e(TAG, "++++++++++++ onDestroy");

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
//	            startActivityForResult(intent, 0);
	        	startActivityFromChild(this, intent, -1);
	            break;
	        case R.id.menulogin: 
	        	
	        	Common.login(this);
	            break;
	               
//	        case R.id.menuorder: 
//	        	break;
	    }
	    return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
		
		menu.setHeaderTitle(R.string.MenuTitle);  
	    menu.add(0, CONTEXTMENU_PUTORDER, 0, R.string.MenuItemPutOrder);
	    menu.add(0, CONTEXTMENU_GOGLASS, 1, R.string.MenuItemGoGlass);
	    menu.add(0, CONTEXTMENU_GOCHART, 2, R.string.MenuItemGoChart);
//	    menu.add(0, CONTEXTMENU_RELOGIN, 2, R.string.MenuItemRelogin);
	    
		super.onCreateContextMenu(menu, v, menuInfo);  

		AdapterView.AdapterContextMenuInfo info =
            (AdapterView.AdapterContextMenuInfo) menuInfo;

	    selectedRowId = (int)info.id;
		adapter.setSelectedPosition(selectedRowId);				
	    Common.setSelectedInstrument( adapter.getItems().get(selectedRowId) );

//	    Log.i(TAG, "selectedRowId = "+selectedRowId);
	    
//		try {
//		
//			sendQuoteGraphSubscription();
//	
//		} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//		}

	}  
	

	
@Override  
   public boolean onContextItemSelected(MenuItem item) {  
		   

	   if (item.getItemId() == CONTEXTMENU_GOGLASS) {
	    	
//			if(Common.quoteActivity != null) {
//				
//				Common.quoteActivity.refresh();
//			}

		   
		   Common.tabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
	    	Common.tabHost.getTabWidget().getChildAt(1).setVisibility(View.VISIBLE);
	    	Common.tabHost.setCurrentTab(1);


	    }
	    
	    if (item.getItemId() == CONTEXTMENU_PUTORDER) {

	    	Common.putOrder(this, null);
	    }

	    if (item.getItemId() == CONTEXTMENU_GOCHART) {

	    	Common.tabHost.setCurrentTab(2);
	    }

	    return true;  
   }  

//public static boolean isApplicationBroughtToBackground(final Activity activity) {
//	  ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
//	  List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
//
//	  // Check the top Activity against the list of Activities contained in the Application's package.
//	  if (!tasks.isEmpty()) {
//	    ComponentName topActivity = tasks.get(0).topActivity;
//	    try {
//	      PackageInfo pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_ACTIVITIES);
//	      for (ActivityInfo activityInfo : pi.activities) {
//	        if(topActivity.getClassName().equals(activityInfo.name)) {
//	          return false;
//	        }
//	      }
//	    } catch( PackageManager.NameNotFoundException e) {
//	      return false; // Never happens.
//	    }
//	  }
//	  return true;
//	}
}
