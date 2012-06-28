package com.vkassin.mtrade;

import android.widget.TabHost;

public class Common {

	private static final String TAG = "MTrade.Common"; 
//    private static final int HTTP_STATUS_OK = 200;
//	private static String sUserAgent = null;
//    private static byte[] sBuffer = new byte[4096];   

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
    
    public final static String PROTOCOL_VERSION = "1.0";
    public final static int ERROR_USER_WAS_NOT_FOUND = 200;
    public final static int ERROR_USER_ALREADY_CONNECTED = 201;
    public final static int ERROR_PASSWORD_ERROR = 202;
    public final static int ERROR_WRONG_PROTOCOL_VERSION = 203;
    public final static int ERROR_LOGIN_INFORMATION = 204;

	public static TabHost tabHost;


}
