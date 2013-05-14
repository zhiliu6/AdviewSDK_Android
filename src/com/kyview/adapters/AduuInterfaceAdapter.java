package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import com.kyview.DownloadService;
import java.util.concurrent.TimeUnit;
import android.text.TextUtils;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.view.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Build;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import com.kyview.AdviewWebView;
import android.os.Bundle;

class AduuAD
{
	private String reqid;
	private String adid;
	private String feetype;
	private String feecode;
	private String clicktype;
	private String clickcontent;
	private String showtype;
	private String icon;
	private String bg;
	private String text;
	private String subtext;
	private String pic;

	public AduuAD()
	{
	}
	
	public String getReqid()
	{
		return reqid;
	}

	public void setReqid(String id) {
		reqid = id;
	}

	public String getAdid()
	{
		return adid;
	}

	public void setAdid(String id) {
		adid = id;
	}

	public String getFeetype() {
		return feetype;
	}

	public void setFeetype(String type) {
		feetype = type;
	}

	public String getFeecode() {
		return feecode;
	}

	public void setFeecode(String code) {
		feecode = code;
	}

	public String getClicktype() {
		return clicktype;
	}

	public void setClicktype(String type) {
		clicktype = type;
	}

	public String getClickcontent() {
		return clickcontent;
	}

	public void setClickcontent(String content) {
		clickcontent = content;
	}

	public String getShowtype() {
		return showtype;
	}

	public void setShowtype(String type) {
		showtype = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String ic) {
		icon = ic;
	}

	public String getBg() {
		return bg;
	}

	public void setBg(String b) {
		bg = b;
	}

	public String getText() {
		return text;
	}

	public void setText(String txt) {
		text = txt;
	}

	public String getSubtext() {
		return subtext;
	}

	public void setSubtext(String txt) {
		subtext = txt;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String p) {
		pic = p;
	}
}

public class AduuInterfaceAdapter extends AdViewAdapter{
	private double adHeight;
	private double adWidth;
	private AdViewLayout adViewLayout;
	public Context mContext;
	
	public AduuAD aduuAD=null;
	public String ADUU_ADURL = "http://api.adcome.cn/v1/adlist";
	public String ADUU_EVTURL = "http://api.adcome.cn/v1/evt";
	private String appid;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADUU;
	}
	
	public static void load(AdViewAdRegistry registry) {
		registry.registerClass(networkType(), AduuInterfaceAdapter.class);
	}

	public AduuInterfaceAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into AduuInterfaceAdapter");
		
		adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			appid = "89BDDA1F21";
		else
			appid = ration.key;
		
		mContext = (Context)adViewLayout.activityReference.get();
		
		calcAdSize(adViewLayout.adViewManager.width);
		
		adViewLayout.scheduler.schedule(
			new FetchAduuADRunnable(this, this.ration), 0L, TimeUnit.SECONDS);
	}

	private void calcAdSize(int screenWidth) {
		int width=320;
		int height=48;
		
		if (AdViewTargeting.getAdWidth() > 0)
		{
			width = AdViewTargeting.getAdWidth();
			height = AdViewTargeting.getAdHeight();			
		}
		else if (screenWidth <= 480) {
			width = 320;
			height = 48;
		} else if (screenWidth < 728) {
			width = 468;
			height = 60;
		} else if (screenWidth >= 728) {
			width = 728;
			height = 90;
		}

		double density = adViewLayout.mDensity;

		adHeight = AdViewUtil.convertToScreenPixels(height, density);
		adWidth = AdViewUtil.convertToScreenPixels(width, density);
	}
	
	//@Override
	public void click()
	{
	/*
		if ((inmobiAD == null) || (inmobiAD.getAdUrl() == null)) {
			Log.d(AdViewUtil.ADVIEW, "Url is null");
			return;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(inmobiAD.getAdUrl()));
		mContext.startActivity(i);
		*/
	}

	 private void parseJson(String jsonStr)
	{
		try
		{
			//Log.i(AdViewUtil.ADVIEW, "jsonStr :" + jsonStr);
			
			JSONObject jsonObject = new JSONObject(jsonStr);
			boolean isresult = jsonObject.isNull("result");
			if (!isresult) {
				String msg = jsonObject.getString("msg");
				Log.i(AdViewUtil.ADVIEW, "msg is :" + msg);
			}
			else {
				aduuAD = new AduuAD();
				aduuAD.setReqid(jsonObject.getString("reqid"));
				JSONArray ja = jsonObject.getJSONArray("ads");
				if (ja.length() > 0) {
					JSONObject jo = ja.getJSONObject(0);
					String showtype = jo.getString("showtype");
					aduuAD.setShowtype(showtype);
					aduuAD.setAdid(jo.getString("adid"));
					aduuAD.setFeecode(jo.getString("feecode"));
					aduuAD.setFeetype(jo.getString("feetype"));
					String clicktype = jo.getString("clicktype");
					if (clicktype.equals("5")) {
						Log.i(AdViewUtil.ADVIEW, "clicktype=5");
						aduuAD = null;
					} else {
						aduuAD.setClicktype(jo.getString("clicktype"));
						aduuAD.setClickcontent(jo.getString("clickcontent"));
						if ("1".equals(showtype)) {
							aduuAD.setText(jo.getString("text"));
							aduuAD.setSubtext(jo.getString("subtext"));
							aduuAD.setIcon(jo.getString("icon"));
							aduuAD.setBg(jo.getString("bg"));
						} else if ("2".equals(showtype)) {
							aduuAD.setPic(jo.getString("pic"));
						} else if ("3".equals(showtype)) {
							aduuAD.setPic(jo.getString("pic"));
						} else if ("4".equals(showtype)) {
							aduuAD.setText(jo.getString("text"));
							aduuAD.setSubtext(jo.getString("subtext"));
						} else if ("5".equals(showtype)) {
							Log.i(AdViewUtil.ADVIEW, "clicktype=5"); 
							aduuAD = null;
						} else {
							Log.i(AdViewUtil.ADVIEW, "showtype="+showtype);
							aduuAD = null;
						}
					}
				} 
			else
			{
				Log.i(AdViewUtil.ADVIEW, "ads is zero");
				aduuAD = null;
			}
			}
		}
		catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
			aduuAD = null;
		}
	}

	public static String aduu_encrypt(String s)
	{
		byte[] sb = s.getBytes();
		String s1 = null;
		char[] ac = { '9', '0', '6', 'e', 'd', 'c', '1', 'b', '3', '8', '7', '2', 'a', '5', 'f', '4' };
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sb);
			byte[] abyte1 = md.digest();
			char[] ac1 = new char[32];
			int l = 0;
			
			for (int i1 = 0; i1 < 16; i1++) {
				byte byte0 = abyte1[i1];
				ac1[(l++)] = ac[(byte0 >>> 4 & 0xF)];
				ac1[(l++)] = ac[(byte0 & 0xF)];
			}
			s1 = new String(ac1);
		} catch (NoSuchAlgorithmException e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}

		return s1;
	}


	private ArrayList<NameValuePair> getADHttpParams()
	{
		String timestamp = String.valueOf(System.currentTimeMillis());
		String imei = AdViewUtil.getImei(mContext);	
		String token = aduu_encrypt("adview_androidm643b0lz8sph7ka0" + timestamp + imei);
		String deviceid = new String (adViewLayout.adViewManager.mDeviceid);
		String uuid = deviceid.replace(":", "") + "00";
		String imsi = AdViewUtil.getImsi(mContext);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sourceid", "adview_android"));
		params.add(new BasicNameValuePair("token", token));

		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("timestamp", timestamp));
		params.add(new BasicNameValuePair("count", "1"));
		params.add(new BasicNameValuePair("adpos", "adview_ban"));
		params.add(new BasicNameValuePair("imei", imei));
		params.add(new BasicNameValuePair("imsi", imsi));
		params.add(new BasicNameValuePair("brand", Build.MODEL));
		params.add(new BasicNameValuePair("platform", "android-" + Build.VERSION.RELEASE));		
		params.add(new BasicNameValuePair("nettype", AdViewUtil.getAduuNetworkType(mContext)));
		String macAdd = AdViewUtil.getIDByMAC(mContext);
		if (!TextUtils.isEmpty(macAdd))
			macAdd = macAdd.replace(":", "");
		else {
			macAdd = "000000000000";
		}	
		params.add(new BasicNameValuePair("macaddr", macAdd));
		int[] wAndH = AdViewUtil.getWidthAndHeight(mContext);
		String screen = "";
		if ((wAndH != null) && (wAndH.length >= 2))
			screen = wAndH[1] + "S" + wAndH[0];
		else {
			screen = "0S0";
		}
		params.add(new BasicNameValuePair("screen", screen));
		params.add(new BasicNameValuePair("appid", appid));

		return params;
	}  

	public ArrayList<NameValuePair> getEvtHttpParams(String evttype)
	{
		String timestamp = String.valueOf(System.currentTimeMillis());
		String token = aduu_encrypt("adview_androidm643b0lz8sph7ka0" + timestamp);
		String deviceid = new String (adViewLayout.adViewManager.mDeviceid);
		String uuid = deviceid.replace(":", "") + "00";
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sourceid", "adview_android"));
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("uuid", uuid));
		params.add(new BasicNameValuePair("evttime", timestamp));
		params.add(new BasicNameValuePair("reqid", aduuAD.getReqid()));
		params.add(new BasicNameValuePair("evttype", evttype));
		params.add(new BasicNameValuePair("nettype", AdViewUtil.getAduuNetworkType(mContext)));
		params.add(new BasicNameValuePair("appid", appid));
		params.add(new BasicNameValuePair("adid", aduuAD.getAdid()));
		
		return params;
	}


	public void requestAduuAD(AdViewLayout adViewLayout, Ration rat)
	{
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
		HttpConnectionParams.setSoTimeout(httpParameters, 20000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpPost httpPost = new HttpPost(ADUU_ADURL);
		ArrayList <NameValuePair>params = getADHttpParams();
		
		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200)
			{
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				if (TextUtils.isEmpty(strResult)) {
					Log.i(AdViewUtil.ADVIEW, "aduu return is null");
					return;
				}
				parseJson(strResult);

			 
				if (aduuAD != null) {
					String html = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /><style type='text/css'>*{padding:0px;margin:0px;} a:link{text-decoration:none;}</style>";
					if (aduuAD.getShowtype().equals("1"))
						html = html + 
						String.format("<a href='www.adview.cn'><div style='width:320px;height:50px;background-image:url(%s);background-repeat :repeat-x';><img style='width:44px;height:44px;float:left;padding:3px' src='%s'></img> <p style='line-height:50px;color:#000;'>%s</p></div></a>", 
						new Object[] { aduuAD.getBg(), aduuAD.getIcon(), aduuAD.getText() });
					else if (aduuAD.getShowtype().equals("2"))
						html = html + 
						String.format("<a href='www.adview.cn'><img style='width:320px;height:50px;' src='%s'></img></a>", 
						new Object[] { aduuAD.getPic() });
					else if (aduuAD.getShowtype().equals("4"))
						html = html + 
						String.format("<a href='www.adview.cn'><div style='width:320px;height:50px; background-color:#FFF'><p style='line-height:50px;color:#000;'>%s</p></div></a>", 
						new Object[] { aduuAD.getText() });
					else {
						html = null;
					}
					if (html != null) {
						adViewLayout.handler.post(new DisplayAduuADRunnable(this, html));
					} else {
						aduuAD = null;
						Log.i(AdViewUtil.ADVIEW, "html is null");
					}
				} 
       		} 
			else 
			{
				Log.i(AdViewUtil.ADVIEW, "aduu request Fail at StatusCode:" + 
					httpResponse.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}
	}

	public void displayAduuAD(String html) 
	{
		Activity activity = (Activity)adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		
		WebView bannerView = new WebView(activity);
		bannerView.setBackgroundColor(Color.alpha(0));
		bannerView.setWebViewClient(new webViewClient());
		bannerView.getSettings().setJavaScriptEnabled(true);
		bannerView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
		bannerView.getSettings().setDefaultTextEncodingName("UTF-8");
		bannerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

		adViewLayout.removeAllViews();
		adViewLayout.reportImpression();
		adViewLayout.activeRation = adViewLayout.nextRation;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			 (int)this.adWidth, (int)this.adHeight);
		layoutParams.addRule(13, -1);

		adViewLayout.addView(bannerView, layoutParams);
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();

		adViewLayout.scheduler.schedule(new SendAduuCountRunnable(AduuInterfaceAdapter.this, "1"), 2, TimeUnit.SECONDS);
	}

public class webViewClient extends WebViewClient
{
String url2="";
private webViewClient()
{
}
 
public boolean shouldOverrideUrlLoading(WebView view, String url)
{
	AdViewLayout adViewLayout = (AdViewLayout)AduuInterfaceAdapter.this.adViewLayoutReference.get();

	String clickcontent = AduuInterfaceAdapter.this.aduuAD.getClickcontent();
	String clicktype = AduuInterfaceAdapter.this.aduuAD.getClicktype();

	if (TextUtils.isEmpty(clicktype) || TextUtils.isEmpty(clickcontent))
	{
		return false;
	}

	if (clicktype.equals("2")==false)
	{
		adViewLayout.scheduler.schedule(new SendAduuCountRunnable(AduuInterfaceAdapter.this, "2"), 2, TimeUnit.SECONDS);
		adViewLayout.reportClick();
	}
	
	if (clicktype.equals("1")) 
	{
		//Intent i = new Intent(Intent.ACTION_VIEW);
		//i.setData(Uri.parse(clickcontent));
		//mContext.startActivity(i);
		Intent intent = new Intent(mContext, AdviewWebView.class);
		Bundle bundle = new Bundle();
		bundle.putString("adviewurl", clickcontent);
		intent.putExtras(bundle);
		mContext.startActivity(intent);			
	}
	else if (clicktype.equals("2")) 
	{
		if (clickcontent.toLowerCase().endsWith(".apk")) {
			//Intent intent = new Intent(mContext, DownloadService.class);
			//intent.putExtra("adview_url",clickcontent);
			//mContext.startService(intent);	
			String title = "下载提示";
			String message = "确定要下载应用吗?";
			String yesBtn = "确定";
			String noBtn = "取消";
			url2 = clickcontent;
			
	           	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(message)
			.setTitle(title)
			.setPositiveButton(yesBtn, 
			new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						//AduuInterfaceAdapter.this.adViewLayout.scheduler.execute(new SendAduuCountRunnable(AduuInterfaceAdapter.this, "2"));
						AduuInterfaceAdapter.this.adViewLayout.scheduler.schedule(new SendAduuCountRunnable(AduuInterfaceAdapter.this, "2"), 2, TimeUnit.SECONDS);
						AduuInterfaceAdapter.this.adViewLayout.reportClick();
						
						Intent intent = new Intent(mContext, DownloadService.class);
						intent.putExtra("adview_url",url2);
						mContext.startService(intent);	
					}
				}).setNegativeButton(noBtn, 
			new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
			builder.create();
			builder.show();			
		}
	}
	else if (clicktype.equals("3")) 
	{
		try {
			Intent intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + clickcontent));
			intent.addFlags(268435456);
			mContext.startActivity(intent);
		} catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}		
	}
	else if (clicktype.equals("4")) 
	{
		try {
			String[] sms = clickcontent.split(",");
			if (sms.length >= 2) {
				Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + sms[0]));
				intent.putExtra("sms_body", sms[1]);
				mContext.startActivity(intent);
			}
		} catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}		
	}
	
	return true;

}

}



}


class DisplayAduuADRunnable implements Runnable
{
	private AduuInterfaceAdapter aduuADAdapter;
	String html;
	 
	public DisplayAduuADRunnable(AduuInterfaceAdapter aduuADAdapter, String htm)
	{
		this.aduuADAdapter = aduuADAdapter;
		html = htm;
	}

	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "DisplayAduuADRunnable");
		
		aduuADAdapter.displayAduuAD(html);
	}
}

class SendAduuCountRunnable implements Runnable
{
	String evttype;
	AduuInterfaceAdapter aduuADAdapter;
	
	public SendAduuCountRunnable(AduuInterfaceAdapter aduuADAdapter, String evttype)
	{
		this.aduuADAdapter = aduuADAdapter;
		this.evttype = evttype;
	}

	public void run()
	{
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "SendAduuCountRunnable");
		
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
		HttpConnectionParams.setSoTimeout(httpParameters, 20000);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		HttpPost httpPost = new HttpPost(aduuADAdapter.ADUU_EVTURL);

		ArrayList <NameValuePair> params = aduuADAdapter.getEvtHttpParams(evttype);
		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String strResult = EntityUtils.toString(httpResponse.getEntity());
				if(AdViewTargeting.getRunMode()==RunMode.TEST)
					Log.d(AdViewUtil.ADVIEW, "aduu Result="+strResult);
			}
			else
			{
				Log.d(AdViewUtil.ADVIEW, "aduu send count StatusCode="+httpResponse.getStatusLine().getStatusCode());
			}
		}
		catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}
	}
}

class FetchAduuADRunnable implements Runnable {
	private AduuInterfaceAdapter aduuADAdapter;
	private Ration ration;
	
	public FetchAduuADRunnable(AduuInterfaceAdapter aduuADAdapter, Ration ration)
	{
	       this.aduuADAdapter = aduuADAdapter;
	       this.ration = ration;
	}
	 
	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "FetchAduuADRunnable");

	       AdViewLayout adViewLayout = this.aduuADAdapter.adViewLayoutReference.get();
	       if (adViewLayout == null) {
			return;
	       }

		aduuADAdapter.requestAduuAD(adViewLayout, ration);	   
	       if (aduuADAdapter.aduuAD == null)
	       {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.d(AdViewUtil.ADVIEW, "FetchAduuAD failure");
			
	       	adViewLayout.adViewManager.resetRollover_pri();
			adViewLayout.rotateThreadedPri();
	       }	       
	}
}
