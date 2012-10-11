package com.adview.adapters;

import android.app.Activity;
//import android.graphics.Color;
import android.util.Log;

//import java.util.Map;
import java.util.HashMap;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
//import com.adview.AdViewLayout.ViewAdRunnable;
import com.adview.AdViewTargeting.RunMode;
//import com.adview.obj.Extra;
import com.adview.obj.Ration;
import com.adview.util.AdViewUtil;
///import com.adview.AdViewWebView;

import com.adview.util.MD5;
import com.adview.util.SHA1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
//import java.util.HashMap;
import java.util.Iterator;
//import java.util.Set;
//import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import android.text.TextUtils;

//import android.webkit.WebSettings;
import android.webkit.WebView;
//import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.view.View;

//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;


class SuizongAD
{
	public String adid = "";
	public String status = "";
	public String msg = "";
	public String updateTime = "";
	public String refreshTime = "";
	public String awidth = "";
	public String aheight = "";
	public String impURL = "";
	public String clickURL = "";
	public String priceType = "";
	public String price = "";
	public String adType = "";
	public String impResult = "";
	public String clkResult = "";
	public String data = "";
	public String sid = "";
}

class SuizongScreenManager
{
	private String aw = "320";
	private String ah = "48";
	private int screenWidth = -1;

	public String getAw() {
	       return this.aw;
	}
	 
	public void setAw(String aw) {
		 this.aw = aw;
	}

	public String getAh() {
		return this.ah;
	}

	public void setAh(String ah) {
		this.ah = ah;
	}

	public SuizongScreenManager(int screenWidth) {
		this.screenWidth = screenWidth;
		calc();
	}

	private void calc() {
		if (this.screenWidth <= 480) {
			this.aw = "320";
			this.ah = "48";
		} else if (this.screenWidth < 728) {
			this.aw = "468";
			this.ah = "60";
		} else if (this.screenWidth >= 728) {
			this.aw = "728";
			this.ah = "90";
		}
	}
}

public class SuizongInterfaceAdapter extends AdViewAdapter{

	public SuizongAD suizongAD;
	private double density;
	public static String ip;
	private double adHeight;
	private double adWidth;
	private static String url = "";
	private String serial_key = "67590f398bf0447931eb20fa2b63bb36";//"67590f398bf0447931eb20fa2b63bb36"//"67590f398bf0447931eb20fa2b63bb34"
	private AdViewLayout adViewLayout;
	private SuizongScreenManager suizongScreenManager;
	static final String SuizongApiAddr = "http://api.suizong.com/mobile/";
	public String mDeviceid="";
	
	public static int convertToScreenPixels(int dipPixels, double density)
	{
		double pix=0;
		
	 	pix = density > 0.0D ? dipPixels * density : dipPixels;

		return (int)pix;
	}

	public SuizongInterfaceAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
	}

	@Override
	public void handle() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into SuizongInterfaceAdapter");
		
		adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}

		suizongAD = new SuizongAD();
		density = adViewLayout.mDensity;
		calcAdSize(adViewLayout.adViewManager.width);
		mDeviceid = adViewLayout.adViewManager.mDeviceid;
		adViewLayout.scheduler.schedule(
			new FetchSuizongADRunnable(this, this.ration), 0L, TimeUnit.SECONDS);
	}

	private void calcAdSize(int screenWidth) {
		int width=320;
		int height=48;
		
		if (screenWidth <= 480) {
			width = 320;
			height = 48;
		} else if (screenWidth < 728) {
			width = 468;
			height = 60;
		} else if (screenWidth >= 728) {
			width = 728;
			height = 90;
		}

		adHeight = convertToScreenPixels(height, density);
		adWidth = convertToScreenPixels(width, density);
	}
	
	@Override
	public void click()
	{
	     url = SuizongApiAddr + "ADServerClickAPI";
	 
	     new Thread() {
	       public void run() {
			Log.d(AdViewUtil.ADVIEW, "click");
			
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("adid", suizongAD.adid);
			map2.put("updatetime", suizongAD.updateTime);
			map2.put("sid", suizongAD.sid);
			map2.put("appkey", ration.key);
			map2.put("uuid", mDeviceid);
			map2.put("client", "2");
			map2.put("ip", ip);			
			String pcheckGetBody = makePcheck4Show(map2);
			map2.put("pcheck", pcheckGetBody);
			map2.put("icheck", MD5.MD5Encode(pcheckGetBody + serial_key));
			httpRequest(map2, "click");
	       }
	     }
	     .start();
	}

	public static HttpURLConnection createConnection(HashMap<String, String> header, String content, String method, boolean doInput, boolean dooutput, boolean usecaches, boolean followRedirects, int connectTimeout, int readTimeout)
		throws MalformedURLException, IOException
	{
		HttpURLConnection httpurlconnection = null;
		
		httpurlconnection = (HttpURLConnection)new URL(url).openConnection();
		httpurlconnection.setDoInput(doInput);
		httpurlconnection.setDoOutput(dooutput);
		httpurlconnection.setRequestMethod(method);
		httpurlconnection.setUseCaches(usecaches);
		httpurlconnection.setInstanceFollowRedirects(followRedirects);
		httpurlconnection.setConnectTimeout(connectTimeout);
		httpurlconnection.setReadTimeout(readTimeout);
		if ((header != null) && (!header.isEmpty())) {
			Iterator<String> it_key = header.keySet().iterator();
			while (it_key.hasNext()) {
				String key = (String)it_key.next();
				String value = (String)header.get(key);
				httpurlconnection.addRequestProperty(key, value);
			}
		}
		httpurlconnection.connect();
		OutputStream output = httpurlconnection.getOutputStream();
		output.write(content.getBytes());
		output.flush();
		output.close();

		return httpurlconnection;
	}

	public void httpRequest(HashMap<String, String> header, String content) {
		HttpURLConnection httpurlconnection = null;
		InputStream contentStream = null;
		BufferedReader bufferReader = null;
		BufferedWriter bufferWriter = null;

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			;//Log.d(AdViewUtil.ADVIEW, "httpRequest, content="+content);	

		try { 
			httpurlconnection = createConnection(header, content, "POST", true, 
				true, false, false, 30000, 30000);

			int responseCode = httpurlconnection.getResponseCode();

			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				;//Log.d(AdViewUtil.ADVIEW, "httpRequest, responseCode="+responseCode);	

			if (200 == responseCode) {
				if (content.equals("request"))
				{
					this.suizongAD.status = httpurlconnection.getHeaderField("status");
					
					if (suizongAD.status.equals("1"))
					{
						suizongAD.adid = httpurlconnection.getHeaderField("adid");
						suizongAD.updateTime = httpurlconnection.getHeaderField("updatetime");
						suizongAD.awidth = httpurlconnection.getHeaderField("width");
						suizongAD.aheight = httpurlconnection.getHeaderField("height");
						suizongAD.awidth = httpurlconnection.getHeaderField("width");
						suizongAD.refreshTime = httpurlconnection.getHeaderField("refresh");
						suizongAD.impURL = httpurlconnection.getHeaderField("imps_url");
						suizongAD.clickURL = httpurlconnection.getHeaderField("click_url");
						suizongAD.priceType = httpurlconnection.getHeaderField("price_type");
						suizongAD.price = httpurlconnection.getHeaderField("price");
						suizongAD.sid = httpurlconnection.getHeaderField("sid");
						
						return;
					}

					suizongAD.msg = httpurlconnection.getHeaderField("msg");
					if(AdViewTargeting.getRunMode()==RunMode.TEST)
						Log.d(AdViewUtil.ADVIEW, "erro msg:" + this.suizongAD.msg);

					return;
				}

				if (content.equals("getbody")) {
					String contentStr = "";
					//Log.d(AdViewUtil.ADVIEW, "getbody result:" + httpurlconnection.getHeaderField("result"));
					if(AdViewTargeting.getRunMode()==RunMode.TEST)
					{
						//suizongAD.msg = httpurlconnection.getHeaderField("msg");
						//Log.d(AdViewUtil.ADVIEW, "erro msg2:" + this.suizongAD.msg);
					}
					
					contentStream = httpurlconnection.getInputStream();
					bufferReader = new BufferedReader(
					new InputStreamReader(contentStream), 4096);
					while ((contentStr = bufferReader.readLine()) != null)
						this.suizongAD.data += contentStr.replace("%20", " ");
					return;
				}

				if (content.equals("imps"))
				{
					//Log.d(AdViewUtil.ADVIEW, "imps result:" + httpurlconnection.getHeaderField("result"));
					
					if (httpurlconnection.getHeaderField("result") != null) {
						httpurlconnection.getHeaderField("result")
						.equals("1"); 
						return;
					}
				}
				else if ((content.equals("click")) && 
					(httpurlconnection.getHeaderField("result") != null)) {
					//Log.d(AdViewUtil.ADVIEW, "click result:" + httpurlconnection.getHeaderField("result"));
					
					httpurlconnection.getHeaderField("result")
					.equals("1"); 
					return;
				}
			}
			else
			{
				this.suizongAD.status = "2";
			}
		} catch (Exception e)
		{
			this.suizongAD.status = "2";
			Log.e(AdViewUtil.ADVIEW, "httpRequest", e);
		} finally {
			if (bufferReader != null)
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (contentStream != null)
				try {
					contentStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (bufferWriter != null)
				try {
					bufferWriter.flush();
					bufferWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
	}

	public String getLocalIPAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> mEnumeration = 
				NetworkInterface.getNetworkInterfaces(); 
				mEnumeration.hasMoreElements();)
			{
				NetworkInterface intf = (NetworkInterface)mEnumeration.nextElement();
				for (Enumeration<InetAddress> enumIPAddr = intf
					.getInetAddresses(); 
					enumIPAddr.hasMoreElements(); )
				{
					InetAddress inetAddress = (InetAddress)enumIPAddr.nextElement();

					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		Log.e("Error", ex.toString());
		}
		return "";
	}

	private String makePcheck(HashMap<String, String> map)
	{
		String appkey = (String)map.get("appkey");
		String uuid = (String)map.get("uuid");
		String client = (String)map.get("client");
		String ip = (String)map.get("ip");
		String s_u_sd = (String)map.get("density");
		String uw = (String)map.get("aw");
		String uh = (String)map.get("ah");
		String pw = (String)map.get("pw");
		String ph = (String)map.get("ph");
		//String sdk = (String)map.get("sdk");
		String pcheck = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(appkey);
			sb.append(uuid);
			sb.append(client);
			sb.append(ip);
			sb.append(s_u_sd);
			sb.append(uw);
			sb.append(uh);
			sb.append(pw);
			sb.append(ph);
			//sb.append(sdk);
			pcheck = SHA1.SHA2(sb.toString());
		} catch (Exception e) {
			return null;
		}
		
		return pcheck;
	}

	private String makePcheck4GetBody(HashMap<String, String> map)
	{
		String adid = map.get("adid") == null ? "" : (String)map.get("adid");
		String updatetime = map.get("updatetime") == null ? "" : (String)map.get("updatetime");
		String sid = map.get("sid") == null ? "" : (String)map.get("sid");
		String pcheck = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(adid);
			sb.append(updatetime);
			sb.append(sid);
			pcheck = SHA1.SHA2(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pcheck;
	}

	private String makePcheck4Show(HashMap<String, String> map)
	{
		String appkey = (String)map.get("appkey");
		String uuid = (String)map.get("uuid");
		String client = (String)map.get("client");
		String ip = (String)map.get("ip");
		//String s_u_sd = (String)map.get("density");
		//String uw = (String)map.get("aw");
		//String uh = (String)map.get("ah");
		//String pw = (String)map.get("pw");
		//String ph = (String)map.get("ph");
		String adid = map.get("adid") == null ? "" : (String)map.get("adid");
		String updatetime = map.get("updatetime") == null ? "" : (String)map.get("updatetime");
		String sid = map.get("sid") == null ? "" : (String)map.get("sid");
		
		String pcheck = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(appkey);
			sb.append(uuid);
			sb.append(client);
			sb.append(ip);
			//sb.append(s_u_sd);
			//sb.append(uw);
			//sb.append(uh);
			//sb.append(pw);
			//sb.append(ph);
			sb.append(adid);
			sb.append(updatetime);
			sb.append(sid);			
			pcheck = SHA1.SHA2(sb.toString());
		} catch (Exception e) {
			return null;
		}
		
		return pcheck;
	}
	
	public void requestSuizongAD(AdViewLayout adViewLayout, String key)
	{
		try
		{	
			if (TextUtils.isEmpty(ip)) {
				ip = getLocalIPAddress();
			}
			
			this.suizongScreenManager = new SuizongScreenManager(adViewLayout.adViewManager.width);

			HashMap<String, String> map = new HashMap<String, String>();
			map.put("appkey", key);
			map.put("uuid", adViewLayout.adViewManager.mDeviceid);
			map.put("client", "2");
			map.put("ip", ip);
			map.put("density", String.valueOf(density));
			map.put("pw", Integer.toString(adViewLayout.adViewManager.width));
			map.put("ph", Integer.toString(adViewLayout.adViewManager.height));
			map.put("aw", this.suizongScreenManager.getAw());
			map.put("ah", this.suizongScreenManager.getAh());
			map.put("sdk", "adview_1.7.1");
			map.put("pcheck", makePcheck(map));
			map.put("icheck", MD5.MD5Encode(makePcheck(map) + this.serial_key));

			url = SuizongApiAddr + "ADServerGetAPI";

			httpRequest(map, "request");
			if (!this.suizongAD.status.equals("1")) {
				return;
			}
			
			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("adid", this.suizongAD.adid);
			map2.put("updatetime", this.suizongAD.updateTime);
			map2.put("sid", this.suizongAD.sid);
			String pcheckGetBody = makePcheck4GetBody(map2);
			map2.put("pcheck", pcheckGetBody);
			map2.put("icheck", MD5.MD5Encode(pcheckGetBody + this.serial_key));
			url = SuizongApiAddr + "ADServerGetBodyAPI";
			httpRequest(map2, "getbody");

			url = SuizongApiAddr + "ADServerShowAPI";
			new Thread() {
				public void run() {
					HashMap<String, String> map3 = new HashMap<String, String>();
					map3.put("adid", suizongAD.adid);
					map3.put("updatetime", suizongAD.updateTime);
					map3.put("sid", suizongAD.sid);
					map3.put("appkey", ration.key);
					map3.put("uuid", mDeviceid);
					map3.put("client", "2");
					map3.put("ip", ip);
					//map3.put("pw", Integer.toString(adViewLayout.adViewManager.width));
					//map3.put("ph", Integer.toString(adViewLayout.adViewManager.height));
					String pcheckGetBody = makePcheck4Show(map3);
					map3.put("pcheck", pcheckGetBody);
					map3.put("icheck", MD5.MD5Encode(pcheckGetBody + serial_key));
					httpRequest(map3, "imps");
				}
			}
			.start();
		}
		catch (Exception localException)
		{
			localException.printStackTrace();
		}
	}

	public void displaySuizongAD() {
		Activity activity = (Activity)adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "displaySuizongAD");

		WebView bannerView = new WebView(activity);

		bannerView.getSettings().setJavaScriptEnabled(true);

		//Log.d(AdViewUtil.ADVIEW, "this.suizongAD.data="+this.suizongAD.data);
		
		bannerView.loadDataWithBaseURL(SuizongApiAddr, 
			this.suizongAD.data, "text/html", "UTF-8", null);	

		bannerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		//bannerView.setWebViewClient(new webViewClient());
		adViewLayout.removeAllViews();
		adViewLayout.activeRation = adViewLayout.nextRation;
		 RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			 (int)this.adWidth, (int)this.adHeight);
		 layoutParams.addRule(13, -1);

		adViewLayout.addView(bannerView, layoutParams);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

/*
public class webViewClient extends WebViewClient
{
private webViewClient()
{
}
 
public boolean shouldOverrideUrlLoading(WebView view, String url)
{
	AdViewLayout adViewLayout = (AdViewLayout)SuizongInterfaceAdapter.this.adViewLayoutReference.get();
	if (adViewLayout == null) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "adViewLayout is null");
	} else {
		Context context = (Context)adViewLayout.activityReference.get();
		Log.d(AdViewUtil.ADVIEW, "shouldOverrideUrlLoading url="+url);
		{
			//Intent intent = new Intent(context, AdViewWebView.class);
			//Bundle bundle = new Bundle();
			//bundle.putString("link", url);
			//intent.putExtras(bundle);
			//context.startActivity(intent);
		}
	}

	return true;
	}
}
*/
}

class DisplaySuizongADRunnable implements Runnable
{
	private SuizongInterfaceAdapter suizongADAdapter;

	public DisplaySuizongADRunnable(SuizongInterfaceAdapter suizongADAdapter)
	{
		this.suizongADAdapter = suizongADAdapter;
	}

	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "DisplaySuizongADRunnable");
		
		this.suizongADAdapter.displaySuizongAD();
	}
}

class FetchSuizongADRunnable implements Runnable {
	private SuizongInterfaceAdapter suizongADAdapter;
	private Ration ration;
	
	public FetchSuizongADRunnable(SuizongInterfaceAdapter suizongADAdapter, Ration ration)
	{
	       this.suizongADAdapter = suizongADAdapter;
	       this.ration = ration;
	}
	 
	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "FetchSuizongADRunnable");
		
	       AdViewLayout adViewLayout = this.suizongADAdapter.adViewLayoutReference.get();
	       if (adViewLayout == null) {
			return;
	       }
	       suizongADAdapter.requestSuizongAD(adViewLayout, ration.key);
		   
	       if (suizongADAdapter.suizongAD.status.equals("1"))
	       {
	       	adViewLayout.handler.post(new DisplaySuizongADRunnable(suizongADAdapter));
	       }
		else
		{
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.d(AdViewUtil.ADVIEW, "FetchSuizongAD failure");
			
	       	adViewLayout.adViewManager.resetRollover_pri();
			adViewLayout.rotateThreadedPri();
	       }	
	}
}
