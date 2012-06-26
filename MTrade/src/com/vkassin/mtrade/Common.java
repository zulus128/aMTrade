package com.vkassin.mtrade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.widget.TabHost;

public class Common {

	private static final String TAG = "MTrade.Common"; 
    private static final int HTTP_STATUS_OK = 200;
	private static String sUserAgent = null;
    private static byte[] sBuffer = new byte[4096];   

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

	public static class ApiException extends Exception {
	
		public ApiException(String detailMessage, Throwable throwable) {
	    
			super(detailMessage, throwable);
		}
		public ApiException(String detailMessage) {
	    
			super(detailMessage);
		}
	}
	
    protected static synchronized String getUrlContent(String url) throws ApiException {
    	
        if (sUserAgent == null) {
            throw new ApiException("User-Agent string must be prepared");
        }

        // Create client and set our specific user-agent string
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", sUserAgent);

        try {
            HttpResponse response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                throw new ApiException("Invalid response from server: " +
                        status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            // Read response into a buffered stream
            int readBytes = 0;
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            // Return result from buffered stream
            return new String(content.toByteArray());
        } catch (IOException e) {
        	Log.e(TAG, "Error:",e);
            throw new ApiException("Problem communicating with API", e);
        }
    }

}
