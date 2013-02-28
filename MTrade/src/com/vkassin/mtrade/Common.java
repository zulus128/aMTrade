package com.vkassin.mtrade;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TabHost;//
import android.widget.TextView;
import android.widget.Toast;

public class Common {

	private static final String TAG = "MTrade.Common";

	public enum item_type {
		IT_NONE, IT_INSTR
	};

	private static DecimalFormat twoDForm = new DecimalFormat("#0.00");

	static {

		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		twoDForm.setDecimalFormatSymbols(dfs);
	}

	public final static String ip_addr = "192.168.111.19";
	public final static int port_login = 9800;
	public final static int port_register = 9802;

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
	public final static Integer MSG_TYPE_TS_MESSAGE = 110;
	public final static Integer MSG_REGISTER = 111;

	public final static String PROTOCOL_VERSION = "1.0";
	public final static int ERROR_USER_WAS_NOT_FOUND = 200;
	public final static int ERROR_USER_ALREADY_CONNECTED = 201;
	public final static int ERROR_PASSWORD_ERROR = 202;
	public final static int ERROR_WRONG_PROTOCOL_VERSION = 203;
	public final static int ERROR_LOGIN_INFORMATION = 204;

	public final static int SORT_TYPE_INSTR = 1;
	public final static int SORT_TYPE_PRICE = 2;
	public final static int SORT_TYPE_STATUS = 3;
	public final static int SORT_TYPE_DATE = 4;

	public static TabHost tabHost;
	public static TabHost.TabSpec tabspec;
	public static boolean confChanged1 = false;

	public static Activity tabActivity;

	public static boolean connected = false;

	public static boolean paused = true;
	public static boolean paused1 = false;

	public static boolean inLogin = false;

	private static Instrument selectedInstrument;

	private static int ordernum;
	public static Context app_ctx;
	private static final String FLIST_FNAME = "favr_list";
	private static final String ACCOUNT_FNAME = "myacc";

	public static final String MENU_URL = "http://www.kase.kz/ru/feed/news/kase";
	public static final String ITEM_TAG = "item";
	public static final String TITLE_TAG = "title";
	public static final String DATE_TAG = "pubDate";
	public static final String LINK_TAG = "link";

	private static HashMap<String, Instrument> instrMap = new HashMap<String, Instrument>();
	private static HashMap<String, History> historyMap = new HashMap<String, History>();
	private static HashSet<String> favrList = new HashSet<String>();
	private static HashMap<String, String> accMap = new HashMap<String, String>();
	private static HashMap<String, Position> posMap = new HashMap<String, Position>();
	private static HashMap<String, Mess> mesMap = new HashMap<String, Mess>();

	public static HashMap<String, String> myaccount = new HashMap<String, String>();

	public static boolean FIRSTLOAD_FINISHED = false;
	public static boolean loginFromDialog = false;
	public static InstrActivity mainActivity;
	public static QuoteActivity quoteActivity;
	public static HistoryActivity historyActivity;
	public static ChartActivity chartActivity;
	public static PosActivity posActivity;
	public static MessageActivity mesActivity;

	public static RSSItem curnews;

	public static int historyFilter = 3;

	// public static int activities = 0;
	public static String oldName = "x";

	private static int mYear;
	private static int mMonth;
	private static int mDay;
	// static final int DATE_DIALOG_ID = 10;
	static EditText datetxt = null;

	// static boolean expchanged = false;

	public static Instrument getSelectedInstrument() {
		return selectedInstrument;
	}

	public static void setSelectedInstrument(Instrument selectedInstrument) {

		if (selectedInstrument == null) {

			Common.tabHost.getTabWidget().getChildAt(2)
					.setVisibility(View.GONE);
			return;
		}

		Log.i(TAG, "Instr selected = " + selectedInstrument.symbol);
		Common.selectedInstrument = selectedInstrument;
		try {
			mainActivity.sendQuoteGraphSubscription();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Common.tabHost.getTabWidget().getChildAt(2).setVisibility(View.VISIBLE);

	}

	public static ArrayList<Instrument> getFavInstrs() {

		ArrayList<Instrument> a = new ArrayList<Instrument>();
		Iterator<String> itr = instrMap.keySet().iterator();

		while (itr.hasNext()) {

			String key = itr.next();
			if (instrMap.get(key).favourite == true)
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
		if (old == null)
			instrMap.put(key, new Instrument(key, obj));
		else
			old.update(obj);
	}

	public static void addToCharts(String key, JSONObject obj) {

		try {

			long instid = obj.getLong("instrId");
			// Log.i(TAG, "key = " + key + " instr = " + instid);

			Instrument i = getInstrById(instid);
			if (i != null)
				i.addDayChartElement(key, obj);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static ArrayList<Mess> getAllMessages() {

		ArrayList<Mess> arr = new ArrayList<Mess>(mesMap.values());
		// Log.w(TAG, "mes_cnt = " + arr.size());
		return arr;
	}

	public static void clearMessageList() {

		mesMap.clear();
	}

	public static Mess addMessageToList(String key, JSONObject obj) {

		Mess old = (Mess) mesMap.get(key);
		if (old == null) {

			Mess m = new Mess(key, obj);
			mesMap.put(key, m);
			return m;
		} else {
			old.update(obj);
			return old;
		}

	}

	public static ArrayList<Position> getAllPositions() {

		ArrayList<Position> arr = new ArrayList<Position>(posMap.values());
		// Log.w(TAG, "pos_cnt = " + arr.size());
		return arr;
	}

	public static void clearPositionList() {

		posMap.clear();
	}

	public static void addPositionToList(String key, JSONObject obj) {

		Position old = (Position) posMap.get(key);
		if (old == null)
			posMap.put(key, new Position(key, obj));
		else
			old.update(obj);
	}

	public static ArrayList<History> getAllHistory() {

		SortedSet<History> hists = new TreeSet<History>(historyMap.values());
		return new ArrayList<History>(hists);

		// return new ArrayList<History>(historyMap.values());
	}

	public static void clearHistoryList() {

		historyMap.clear();
	}

	public static void addOrderToHistoryList(String key, JSONObject obj) {

		Order old = (Order) historyMap.get(key);
		if (old == null)
			historyMap.put(key, new Order(key, obj));
		else
			old.update(obj);

		// Log.w(TAG, "History cnt = " + historyMap.size());
	}

	public static void addDealToHistoryList(String key, JSONObject obj) {

		Deal old = (Deal) historyMap.get(key);
		if (old == null)
			historyMap.put(key, new Deal(key, obj));
		else
			old.update(obj);
	}

	public static void addToAccountList(String key, JSONObject obj)
			throws JSONException {

		accMap.put(key, obj.getString("code"));
	}

	public static Collection<String> getAccountList() {

		return accMap.values();
	}

	public static void clearAccountList() {

		accMap.clear();
	}

	public static void validateFavourites() {

		// loadFavrList();

		Iterator<String> itr = instrMap.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			instrMap.get(key).favourite = false;
		}

		Iterator<String> setIterator = favrList.iterator();
		while (setIterator.hasNext()) {
			String currentElement = setIterator.next();
			// Log.w(TAG, "cur = "+currentElement);
			if (instrMap.get(currentElement) == null) {
				setIterator.remove();
				Log.w(TAG, "removed");
			} else
				instrMap.get(currentElement).favourite = true;
		}
	}

	public static HashSet<String> getFavrList() {

		return favrList;
	}

	public static void clearFavrList() {

		favrList.clear();
		validateFavourites();
		saveFavrList();
	}

	public static void setFavrList(HashSet<String> a) {

		favrList = a;
		validateFavourites();
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

			Toast.makeText(app_ctx, "Файл не записан " + e.toString(),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {

			Toast.makeText(app_ctx, "Файл не записан: " + e.toString(),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

		// Toast.makeText(app_ctx, "Сохранен список инструментов",
		// Toast.LENGTH_SHORT).show();

	}

	public static void loadFavrList() {

		FileInputStream fileInputStream;
		try {

			fileInputStream = app_ctx.openFileInput(FLIST_FNAME);
			ObjectInputStream oInputStream = new ObjectInputStream(
					fileInputStream);
			Object one = oInputStream.readObject();
			favrList = (HashSet<String>) one;
			oInputStream.close();
			fileInputStream.close();

		} catch (FileNotFoundException e) {

			// e.printStackTrace();
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

		// return favourites;
	}

	public static void saveAccountDetails() {

		FileOutputStream fos;
		try {

			fos = app_ctx.openFileOutput(ACCOUNT_FNAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(myaccount);
			os.close();
			fos.close();
			Log.i(TAG, "saved username: " + myaccount.get("name")
					+ " password: " + myaccount.get("password"));
		} catch (FileNotFoundException e) {

			Toast.makeText(app_ctx, "Файл не записан " + e.toString(),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {

			Toast.makeText(app_ctx, "Файл не записан: " + e.toString(),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}

	public static void loadAccountDetails() {

		FileInputStream fileInputStream;
		try {

			fileInputStream = app_ctx.openFileInput(ACCOUNT_FNAME);
			ObjectInputStream oInputStream = new ObjectInputStream(
					fileInputStream);
			Object one = oInputStream.readObject();
			myaccount = (HashMap<String, String>) one;
			oInputStream.close();
			fileInputStream.close();

		} catch (FileNotFoundException e) {

			// e.printStackTrace();
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

		// return favourites;
	}

	private static byte[] readMsg(InputStream inStream, int remainingToRead)
			throws IOException {
		byte[] buffer = new byte[remainingToRead];
		while (remainingToRead != 0) {
			int len = inStream.read(buffer, buffer.length - remainingToRead,
					remainingToRead);
			remainingToRead -= len;
		}
		return buffer;
	}

	public static String getMd5(String txt) {
		StringBuffer result = new StringBuffer();
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
//			m.update(txt.getBytes(Charset.forName("UTF-8")));
			m.update(txt.getBytes());
			byte[] digest = m.digest();
			for (int i = 0; i < digest.length; i++)
				result.append(String.format("%02x", digest[i]));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			Log.e(TAG, "Error in getMd5!!", e);		}
		return result.toString();
	}

	public static String sign(String login, String passwd) {
		String msgKey = "aW3f!@Jm<h&*9>?g";
		SimpleDateFormat sdf = new SimpleDateFormat("|yyyy|(MM)[dd*HH]");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Calendar currentTime = Calendar.getInstance();
		String currDateTime = sdf.format(currentTime.getTime());
		return getMd5(login + "-" + passwd + "-" + currDateTime + "-"
				+ msgKey);
	}

	public static void login(Context ctx) {

		// ctx = Common.app_ctx;
		Common.connected = false;

		if (inLogin)
			return;

		inLogin = true;

		if (Common.mainActivity != null)
			Common.mainActivity.handler.sendMessage(Message.obtain(
					Common.mainActivity.handler,
					Common.mainActivity.DISMISS_PROGRESS_DIALOG));

		// while(true) {

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.login_dialog);
		dialog.setTitle(R.string.LoginDialogTitle);
		dialog.setCancelable(false);

		final EditText nametxt = (EditText) dialog
				.findViewById(R.id.loginnameedit);
		final EditText passtxt = (EditText) dialog
				.findViewById(R.id.passwordedit);
		final EditText passtxt1 = (EditText) dialog
				.findViewById(R.id.passwordedit1);
		final EditText passtxt2 = (EditText) dialog
				.findViewById(R.id.passwordedit2);
		final EditText mailtxt = (EditText) dialog
				.findViewById(R.id.emailedit2);

		String nam = myaccount.get("name");
		Common.oldName = nam;

		if (nam != null) {

			nametxt.setText(nam);
			passtxt.requestFocus();
		}

		Button customDialog_Register = (Button) dialog
				.findViewById(R.id.goregister);
		customDialog_Register.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				dialog.setTitle(R.string.LoginDialogTitle1);
				final LinearLayout layreg = (LinearLayout) dialog
						.findViewById(R.id.reglayout354);
				layreg.setVisibility(View.VISIBLE);
				final LinearLayout laylog = (LinearLayout) dialog
						.findViewById(R.id.loginlayout543);
				laylog.setVisibility(View.GONE);
			}

		});

		Button customDialog_Register1 = (Button) dialog
				.findViewById(R.id.goregister1);
		customDialog_Register1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				if (mailtxt.getText().length() < 1) {

					Toast toast = Toast.makeText(mainActivity,
							R.string.CorrectEmail, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
							0, 0);
					toast.show();

					return;
				}

				if (passtxt1.getText().length() < 1) {

					Toast toast = Toast.makeText(mainActivity,
							R.string.CorrectPassword, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
							0, 0);
					toast.show();
					return;
				}

				if (!passtxt2.getText().toString()
						.equals(passtxt1.getText().toString())) {

					Toast toast = Toast.makeText(mainActivity,
							R.string.CorrectPassword1, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
							0, 0);
					toast.show();
					return;
				}

				try {

					Socket sock = new Socket(ip_addr, port_register);

					JSONObject msg = new JSONObject();
					msg.put("objType", Common.MSG_REGISTER);
					msg.put("time", Calendar.getInstance().getTimeInMillis());
					msg.put("user", mailtxt.getText().toString());
					msg.put("passwd", passtxt1.getText().toString());
					msg.put("version", Common.PROTOCOL_VERSION);
					msg.put("sign", sign(mailtxt.getText().toString(),  passtxt1.getText().toString() ));

					byte[] array = msg.toString().getBytes();
					ByteBuffer buff = ByteBuffer.allocate(array.length + 4);
					buff.putInt(array.length);
					buff.put(array);
					sock.getOutputStream().write(buff.array());

					ByteBuffer buff1 = ByteBuffer.allocate(4);
					buff1.put(readMsg(sock.getInputStream(), 4));
					buff1.position(0);
					int pkgSize = buff1.getInt();
					// Log.i(TAG, "size = "+pkgSize);
					String s = new String(readMsg(sock.getInputStream(),
							pkgSize));

					sock.close();

					JSONObject jo = new JSONObject(s);

					Log.i(TAG, "register answer = " + jo);

					int t = jo.getInt("status");
					switch (t) {

					case 1:
						Toast toast = Toast.makeText(mainActivity,
								R.string.RegisterStatus1, Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP
								| Gravity.CENTER_HORIZONTAL, 0, 0);
						toast.show();

						dialog.setTitle(R.string.LoginDialogTitle);
						final LinearLayout layreg = (LinearLayout) dialog
								.findViewById(R.id.reglayout354);
						layreg.setVisibility(View.GONE);
						final LinearLayout laylog = (LinearLayout) dialog
								.findViewById(R.id.loginlayout543);
						laylog.setVisibility(View.VISIBLE);

						nametxt.setText(mailtxt.getText());
						break;

					case -2:
						Toast toast1 = Toast.makeText(mainActivity,
								R.string.RegisterStatus3, Toast.LENGTH_LONG);
						toast1.setGravity(Gravity.TOP
								| Gravity.CENTER_HORIZONTAL, 0, 0);
						toast1.show();
						break;

					default:
						Toast toast2 = Toast.makeText(mainActivity,
								R.string.RegisterStatus2, Toast.LENGTH_LONG);
						toast2.setGravity(Gravity.TOP
								| Gravity.CENTER_HORIZONTAL, 0, 0);
						toast2.show();
						break;

					}

				} catch (Exception e) {

					e.printStackTrace();
					Log.e(TAG, "Error in registration process!!", e);

					Toast toast = Toast.makeText(mainActivity,
							R.string.ConnectError, Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
							0, 0);
					toast.show();

				}

			}

		});

		Button customDialog_CancelReg = (Button) dialog
				.findViewById(R.id.cancelreg);
		customDialog_CancelReg.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				dialog.setTitle(R.string.LoginDialogTitle);
				final LinearLayout layreg = (LinearLayout) dialog
						.findViewById(R.id.reglayout354);
				layreg.setVisibility(View.GONE);
				final LinearLayout laylog = (LinearLayout) dialog
						.findViewById(R.id.loginlayout543);
				laylog.setVisibility(View.VISIBLE);

			}

		});

		Button customDialog_Dismiss = (Button) dialog
				.findViewById(R.id.gologin);
		customDialog_Dismiss.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				inLogin = false;

				JSONObject msg = new JSONObject();
				try {

					msg.put("objType", Common.LOGOUT);
					msg.put("time", Calendar.getInstance().getTimeInMillis());
					msg.put("version", Common.PROTOCOL_VERSION);
					msg.put("status", 1);
					mainActivity.writeJSONMsg(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Error! Cannot create JSON logout object", e);
				}

				myaccount.put("name", nametxt.getText().toString());
				myaccount.put("password", passtxt.getText().toString());

				Log.i(TAG, "myaccount username: " + myaccount.get("name")
						+ " password: " + myaccount.get("password"));

				dialog.dismiss();
				mainActivity.stop();
				// saveAccountDetails();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loginFromDialog = true;
				mainActivity.refresh();
				// break;
			}

		});

		dialog.show();
		// Common.confChanged = false;
		// }//while(true);

	}

	public static void delOrder(final Context ctx, History hist) {

		JSONObject msg = new JSONObject();
		try {

			msg.put("objType", Common.CREATE_REMOVE_ORDER);
			msg.put("time", Calendar.getInstance().getTimeInMillis());
			msg.put("version", Common.PROTOCOL_VERSION);
			msg.put("orderNum", ++ordernum);
			msg.put("action", "REMOVE");
			msg.put("transSerial", hist.getSerial());

			mainActivity.writeJSONMsg(msg);

		} catch (Exception e) {

			e.printStackTrace();
			Log.e(TAG, "Error! Cannot create JSON order object (delOrder)", e);
		}
	}

	private static DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			if (datetxt != null) {

				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

				datetxt.setText(sdf.format(new GregorianCalendar(mYear, mMonth,
						mDay).getTime()));

			}
		}
	};

	public static void putOrder(final Context ctx, Quote quote) {

		final Instrument it = Common.selectedInstrument;// adapter.getItem(selectedRowId);

		final Dialog dialog = new Dialog(ctx);
		dialog.setContentView(R.layout.order_dialog);
		dialog.setTitle(R.string.OrderDialogTitle);

		datetxt = (EditText) dialog.findViewById(R.id.expdateedit);
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date dat1 = new Date();
		datetxt.setText(sdf.format(dat1));
		mYear = dat1.getYear() + 1900;
		mMonth = dat1.getMonth();
		mDay = dat1.getDate();
		final Date dat = new GregorianCalendar(mYear, mMonth, mDay).getTime();

		datetxt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.i(TAG, "Show DatePickerDialog");
				DatePickerDialog dpd = new DatePickerDialog(ctx,
						mDateSetListener, mYear, mMonth, mDay);
				dpd.show();
			}
		});

		TextView itext = (TextView) dialog.findViewById(R.id.instrtext);
		itext.setText(it.symbol);

		final Spinner aspinner = (Spinner) dialog
				.findViewById(R.id.acc_spinner);
		List<String> list = new ArrayList<String>(Common.getAccountList());
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				Common.app_ctx, android.R.layout.simple_spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_item);
		aspinner.setAdapter(dataAdapter);

		final EditText pricetxt = (EditText) dialog
				.findViewById(R.id.priceedit);
		final EditText quanttxt = (EditText) dialog
				.findViewById(R.id.quantedit);

		final Button buttonpm = (Button) dialog
				.findViewById(R.id.buttonPriceMinus);
		buttonpm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					double price = Double
							.valueOf(pricetxt.getText().toString());
					price -= 0.01;
					if (price < 0)
						price = 0;
					pricetxt.setText(twoDForm.format(price));

				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectPrice,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final Button buttonpp = (Button) dialog
				.findViewById(R.id.buttonPricePlus);
		buttonpp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					double price = Double
							.valueOf(pricetxt.getText().toString());
					price += 0.01;
					pricetxt.setText(twoDForm.format(price));

				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectPrice,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final Button buttonqm = (Button) dialog
				.findViewById(R.id.buttonQtyMinus);
		buttonqm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					long qty = Long.valueOf(quanttxt.getText().toString());
					qty -= 1;
					if (qty < 0)
						qty = 0;
					quanttxt.setText(String.valueOf(qty));

				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectQty, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		final Button buttonqp = (Button) dialog
				.findViewById(R.id.buttonQtyPlus);
		buttonqp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {

					long qty = Long.valueOf(quanttxt.getText().toString());
					qty += 1;
					quanttxt.setText(String.valueOf(qty));

				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectQty, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		final RadioButton bu0 = (RadioButton) dialog.findViewById(R.id.radio0);
		final RadioButton bu1 = (RadioButton) dialog.findViewById(R.id.radio1);

		if (quote != null) {

			// pricetxt.setText(quote.price.toString());
			pricetxt.setText(quote.getPriceS());
			if (quote.qtyBuy > 0) {

				quanttxt.setText(quote.qtyBuy.toString());
				bu1.setChecked(true);
				bu0.setChecked(false);

			} else {

				quanttxt.setText(quote.qtySell.toString());
				bu1.setChecked(false);
				bu0.setChecked(true);

			}
		}

		Button customDialog_Cancel = (Button) dialog
				.findViewById(R.id.cancelbutt);
		customDialog_Cancel.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				dialog.dismiss();
			}

		});

		Button customDialog_Put = (Button) dialog.findViewById(R.id.putorder);
		customDialog_Put.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

				Double price = new Double(0);
				Long qval = new Long(0);

				try {

					price = Double.valueOf(pricetxt.getText().toString());
				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectPrice,
							Toast.LENGTH_SHORT).show();
					return;
				}
				try {

					qval = Long.valueOf(quanttxt.getText().toString());
				} catch (Exception e) {

					Toast.makeText(ctx, R.string.CorrectQty, Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (dat.compareTo(new GregorianCalendar(mYear, mMonth, mDay)
						.getTime()) > 0) {

					Toast.makeText(ctx, R.string.CorrectDate,
							Toast.LENGTH_SHORT).show();

					return;

				}

				JSONObject msg = new JSONObject();
				try {

					msg.put("objType", Common.CREATE_REMOVE_ORDER);
					msg.put("time", Calendar.getInstance().getTimeInMillis());
					msg.put("version", Common.PROTOCOL_VERSION);
					msg.put("instrumId", Long.valueOf(it.id));
					msg.put("price", price);
					msg.put("qty", qval);
					msg.put("ordType", 1);
					msg.put("side", bu0.isChecked() ? 0 : 1);
					msg.put("code", String.valueOf(aspinner.getSelectedItem()));
					msg.put("orderNum", ++ordernum);
					msg.put("action", "CREATE");
					// msg.put("expireDate", new GregorianCalendar(mYear,
					// mMonth, mDay).getTimeInMillis());

					boolean b = (((mYear - 1900) == dat.getYear())
							&& (mMonth == dat.getMonth()) && (mDay == dat
							.getDate()));
					if (!b)
						msg.put("expired", String.format("%02d.%02d.%04d",
								mDay, mMonth + 1, mYear));

					mainActivity.writeJSONMsg(msg);

				} catch (Exception e) {

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

	public static ArrayList<RSSItem> getNews() {

		RSSHandler handler = new RSSHandler();
		String errorMsg = generalWebServiceCall(MENU_URL, handler);

		if (errorMsg.length() > 0)
			Log.e(TAG, errorMsg);

		return handler.getParsedData();
	}

	public static String generalWebServiceCall(String urlStr,
			ContentHandler handler) {

		String errorMsg = "";

		try {
			URL url = new URL(urlStr);

			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("User-Agent",
					"Android Application: aMTrade");
			urlc.setRequestProperty("Connection", "close");
			// urlc.setRequestProperty("Accept-Charset", "windows-1251");
			// urlc.setRequestProperty("Accept-Charset",
			// "windows-1251,utf-8;q=0.7,*;q=0.7");
			urlc.setRequestProperty("Accept-Charset", "utf-8");

			urlc.setConnectTimeout(1000 * 5); // mTimeout is in seconds
			urlc.setDoInput(true);
			urlc.connect();

			if (urlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				// Get a SAXParser from the SAXPArserFactory.
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();

				// Get the XMLReader of the SAXParser we created.
				XMLReader xr = sp.getXMLReader();

				// Apply the handler to the XML-Reader
				xr.setContentHandler(handler);

				// Parse the XML-data from our URL.
				InputStream is = urlc.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				ByteArrayBuffer baf = new ByteArrayBuffer(500);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
				ByteArrayInputStream bais = new ByteArrayInputStream(
						baf.toByteArray());
				// Reader isr = new InputStreamReader(bais, "windows-1251");
				Reader isr = new InputStreamReader(bais, "utf-8");
				InputSource ist = new InputSource();
				// ist.setEncoding("UTF-8");
				ist.setCharacterStream(isr);
				xr.parse(ist);
				// Parsing has finished.

				bis.close();
				baf.clear();
				bais.close();
				is.close();
			}

			urlc.disconnect();

		} catch (SAXException e) {
			// All is OK :)
		} catch (MalformedURLException e) {
			Log.e(TAG, errorMsg = "MalformedURLException");
		} catch (IOException e) {
			Log.e(TAG, errorMsg = "IOException");
		} catch (ParserConfigurationException e) {
			Log.e(TAG, errorMsg = "ParserConfigurationException");
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.e(TAG, errorMsg = "ArrayIndexOutOfBoundsException");
		}

		return errorMsg;
	}
}
