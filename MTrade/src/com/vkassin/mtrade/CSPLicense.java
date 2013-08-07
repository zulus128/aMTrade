package com.vkassin.mtrade;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CSPLicense {

	private static final String TAG = "MTrade.CSPLicense"; 

	private static final String licenseURL = "https://shop.gamma.kz/gs_cart/get_mobile_license";

	String  getLicenseCode() {
		
		return "9651f0911f6d7e10dce1d83a4001e12a";
	}
	
	
	//Функция запрашивает у сервера лицензию, используя КОД лицензии, введенный пользователем 
	boolean getLicense(){ 
	    
	HttpClient httpclient = getNewHttpClient(); 
	HttpPost httppost = new HttpPost(licenseURL);   //licenseURL - url к серверу лицензий, например (https://shop.gamma.kz/gs_cart/get_mobile_license) 
	try { 

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3); 
		nameValuePairs.add(new BasicNameValuePair("os", "android")); 
		TelephonyManager telephonyManager = (TelephonyManager)Common.app_ctx.getSystemService(Context.TELEPHONY_SERVICE);  
		nameValuePairs.add(new BasicNameValuePair("imei", telephonyManager.getDeviceId())); 
		nameValuePairs.add(new BasicNameValuePair("code", getLicenseCode()));   //В этот параметр передается КОД лицензии, введенный пользователем 
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs)); 
		HttpResponse response = httpclient.execute(httppost); 
		HttpEntity entity = response.getEntity(); 
		if (entity != null) { 
			String result = EntityUtils.toString(entity);   //В переменной result помещается ЛИЦЕНЗИЯ 
			Log.i(TAG,"license = " + result);
			boolean b = this.installLicense(result);
			Log.i(TAG, "write to file = " + b);
			
		} 
		else return false; 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return false; 
	} 
	return true; 
}
	
	//Функция возвращает HttpClient, используемый для оправки запроса лицензии на сервер 
	public HttpClient getNewHttpClient() { 
	try { 
		
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
		trustStore.load(null, null); 
		SSLSocketFactory sf = new MySSLSocketFactory(trustStore); 
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 
		HttpParams params = new BasicHttpParams(); 
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8); 
		SchemeRegistry registry = new SchemeRegistry(); 
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80)); 
		registry.register(new Scheme("https", sf, 443)); 
		ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry); 
		return new DefaultHttpClient(ccm, params); 
	
	} catch (Exception e) { 
	return new DefaultHttpClient(); 
	} 
	}
	
	//Для работы с сервером через https был переопределен класс SSLSocketFactory 
	public class MySSLSocketFactory extends SSLSocketFactory { 
		
	SSLContext sslContext = SSLContext.getInstance("TLS"); 
	public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException { 
	super(truststore); 
	
	TrustManager tm = new X509TrustManager() { 
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
	} 
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException { 
	} 
	public X509Certificate[] getAcceptedIssuers() { 
	return null; 
	} 
	}; 
	sslContext.init(null, new TrustManager[] { tm }, null); 
	} 
	@Override 
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException { 
	return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose); 
	} 
	@Override 
	public Socket createSocket() throws IOException { 
	return sslContext.getSocketFactory().createSocket(); 
	} 
	}   
	
	//Функия записывает лицензию в файл /sdcard/TumarCSP/cptumar.reg 
	private boolean installLicense(String license){ 
	try{ 
	
		if ((Environment.getExternalStorageDirectory() != null) && (Environment.getExternalStorageDirectory().canRead())){ 
			String dirpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TumarCSP/"; 
			String licPath = dirpath + "cptumar.reg"; 
			File dir = new File(dirpath); 
			if(!dir.exists()){ 
				if (!dir.mkdir()) return false; 
			} 
	
			File lic = new File(licPath); 
			if (lic.exists()) { 
				if (!lic.delete()) return false; 
			} 
	
			license = license.substring(1,license.length()-1); 
			license = license.replace("\\\\","\\"); 
			license = license.replace("\\n","\n"); 
			license = license.replace("\\r","\r"); 
			license = license.replace("\\\"","\""); 
			FileOutputStream outputStream = new FileOutputStream(licPath); 
			outputStream.write(license.getBytes()); 
			outputStream.close(); 
			} 
			else return false; 
			}catch (Exception ex){ 
			return false; 
			} 
	
	return true; 
	}
	
}
