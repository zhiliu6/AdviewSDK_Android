package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdviewWebView;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import com.kyview.util.MD5;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.net.URLEncoder;
import android.webkit.WebSettings;
import android.os.Bundle;


class InmobiAD
{
	private int adsType;
	private String imgUrl;
	private Drawable adImg;
	private String placement;
	private String adUrl;
	private String linkText;

	public int getAdsType()
	{
		return adsType;
	}

	public void setAdsType(int type) {
		adsType = type;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String url) {
		imgUrl = url;
	}

	public Drawable getAdImg() {
		return adImg;
	}

	public void setAdImg(Drawable img) {
		adImg = img;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String pm) {
		placement = pm;
	}

	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String url) {
		adUrl = url;
	}

	public String getLinkText() {
		return linkText;
	}

	public void setLinkText(String txt) {
		linkText = txt;
	}

	public InmobiAD()
	{
	}
}

public class InmobiInterfaceAdapter extends AdViewAdapter{
	private double adHeight;
	private double adWidth;
	private AdViewLayout adViewLayout;
	public Context mContext;

	public InmobiAD inmobiAD;
	private String userAgent = "";

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_INMOBI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		registry.registerClass(networkType(), InmobiInterfaceAdapter.class);
	}

	public InmobiInterfaceAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into InmobiInterfaceAdapter");
		
		adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		mContext = (Context)adViewLayout.activityReference.get();
		
		userAgent = getUsrAgent(mContext);
		calcAdSize(adViewLayout.adViewManager.width);

		adViewLayout.scheduler.schedule(
			new FetchInmobiADRunnable(this, this.ration), 0L, TimeUnit.SECONDS);
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
	
	private String encodeString(String str)
	{
		try
		{
			return URLEncoder.encode(str, "utf-8"); 
		} 
		catch (Exception e)
		{
			Log.e(AdViewUtil.ADVIEW, "encodeString", e);
		}
		return str;
	}

	private String getUsrAgent(Context context)
	{
		if (context == null) {
			return "";
		}
		WebView webView = new WebView(mContext);
		WebSettings settings = webView.getSettings();
		String ua = settings.getUserAgentString();
		return encodeString(ua);
	}

	
	//@Override
	public void click()
	{
		if ((inmobiAD == null) || (inmobiAD.getAdUrl() == null)) {
			Log.d(AdViewUtil.ADVIEW, "Url is null");
			return;
		}

		//Intent i = new Intent(Intent.ACTION_VIEW);
		//i.setData(Uri.parse(inmobiAD.getAdUrl()));
		//mContext.startActivity(i);
		Intent intent = new Intent(mContext, AdviewWebView.class);
		Bundle bundle = new Bundle();
		bundle.putString("adviewurl", inmobiAD.getAdUrl());
		intent.putExtras(bundle);
		mContext.startActivity(intent);	
	}

	public String getAndroidId()
	{
		String mAndroidId = android.provider.Settings.Secure.getString(mContext.getContentResolver(),
		android.provider.Settings.Secure.ANDROID_ID);

		if (mAndroidId == null)
		{
			mAndroidId = android.provider.Settings.System.getString(mContext.getContentResolver(),
			android.provider.Settings.System.ANDROID_ID);
		}
		//Log.i(AdViewUtil.ADVIEW, "mAndroidId:" + mAndroidId);
		return mAndroidId;
	}

	public InmobiAD requestInmobiAD(AdViewLayout adViewLayout, String key)
	{
		InmobiAD inmobiAd = null;
		String mkSiteid = key;
		String hUserAagent = userAgent;
		String uId=encodeString("{\"UM5\":\""+MD5.MD5Encode(getAndroidId())+"\"}");
		//String uId="{\"UM5\":\""+MD5.MD5Encode(getAndroidId())+"\"}";
		//String uId = MD5.MD5Encode(getAndroidId());//(adViewLayout.adViewManager.mDeviceid);

		String dLocalization;
		String dNetType;
		String mkVersion = "pr-SPEC-ATATA-20090521";
		String refTag = "adsAdview";
		String format = "xml";

		if (adViewLayout.netType.equals("Wi-Fi"))
		{
			dNetType = "wifi";
		}
		else
		{
			dNetType = "mobile";
		}

		if (adViewLayout.adViewManager.bLocationForeign == false)
			dLocalization = "zh_CN";
		else
			dLocalization = "en_US";
		
		String paramsStr = 
		String.format("mk-siteid=%s&h-user-agent=%s&u-id-map=%s&u-key-ver=0&d-localization=%s&d-netType=%s&mk-version=%s&ref-tag=%s&format=%s&d-device-screen-density=%3.1f&d-device-screen-size=%s", new Object[] { //&tp=c_adview
			mkSiteid, hUserAagent, uId, dLocalization, 
			dNetType, mkVersion, refTag, format,adViewLayout.mDensity, adViewLayout.resolution});
		if(AdViewTargeting.getRunMode()==RunMode.TEST)	
			Log.i(AdViewUtil.ADVIEW, "request parameters:" + paramsStr);
		try {
			String host;
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				host = "http://i.w.sandbox.inmobi.com/showad.asm";
			else
				host = "http://i.w.inmobi.com/showad.asm";

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(host);

			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded ");

			httpPost.setEntity(new StringEntity(paramsStr));

			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 200)
				inmobiAd = getInmobiADByXML(EntityUtils.toString(httpResponse.getEntity(), "utf-8"));
			else
			{
				Log.i(AdViewUtil.ADVIEW, "getStatusCode=" + httpResponse.getStatusLine().getStatusCode());
				return null;
			}
		}
		catch (Exception e)
		{
			Log.e(AdViewUtil.ADVIEW, "requestInmobiAD", e);
		}

		return inmobiAd;
	}

 	private InmobiAD getInmobiADByXML(String xmlStr)
	{
		InmobiAD inmobiAD = null;

		if(AdViewTargeting.getRunMode()==RunMode.TEST)	
			Log.i(AdViewUtil.ADVIEW, "getInmobiADByXML, xml="+xmlStr);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));

			if (doc == null) {
				Log.w(AdViewUtil.ADVIEW, "xml is null");
				return null;
			}

			Element root = doc.getDocumentElement();
			if (root == null) {
				Log.w(AdViewUtil.ADVIEW, "invalid xml");
				return null;
			}
			NodeList nodeList = root.getElementsByTagName("Ad");

			if ((nodeList == null) || (nodeList.getLength() < 1))
			{
				Log.w(AdViewUtil.ADVIEW, "list is null");
				return null;
			}

			inmobiAD = new InmobiAD();
			
			Element adElement = (Element)nodeList.item(0);
			NodeList adInfoList = adElement.getChildNodes();

			if (adElement.getAttribute("type").equals("banner")) {
				inmobiAD.setAdsType(1);
				for (int i = 0; i < adInfoList.getLength(); i++) {
					Element infoElement = (Element)adInfoList.item(i);
					if (infoElement.getNodeName().equals("ImageURL")) {
						inmobiAD.setImgUrl(infoElement.getFirstChild().getNodeValue());
					}
					else if (infoElement.getNodeName()
					.equals("Placement"))
						inmobiAD.setPlacement(infoElement.getFirstChild().getNodeValue());
					else if (infoElement.getNodeName().equals("AdURL")) {
						inmobiAD.setAdUrl(infoElement.getFirstChild().getNodeValue());
					}
				}
				inmobiAD.setAdImg(fetchImage(inmobiAD.getImgUrl()));
			} else {
				inmobiAD.setAdsType(2);
				for (int i = 0; i < adInfoList.getLength(); i++) {
					Element infoElement = (Element)adInfoList.item(i);
					if (infoElement.getNodeName().equals("LinkText")) {
						inmobiAD.setLinkText(infoElement.getFirstChild().getNodeValue());
					}
					else if (infoElement.getNodeName().equals("Placement"))
						inmobiAD.setPlacement(infoElement.getFirstChild().getNodeValue());
					else if (infoElement.getNodeName().equals("AdURL")) {
						inmobiAD.setAdUrl(infoElement.getFirstChild().getNodeValue());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return inmobiAD;
 	}

	private Drawable fetchImage(String urlString)
	{
		try {
			URL url = new URL(urlString);
			InputStream is = (InputStream)url.getContent();
			Drawable d = Drawable.createFromStream(is, "src");
			is.close();
			return d;
		} catch (Exception e) {
			Log.e(AdViewUtil.ADVIEW, "fetchImage: ", e);
		}
		return null;
	}

	public void displayInmobiAD() 
	{
		Activity activity = (Activity)adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Inmobi AD type="+inmobiAD.getAdsType());
					
		switch (inmobiAD.getAdsType()) {
		case 1:
			if (inmobiAD.getAdImg() == null)
			{
				Log.d(AdViewUtil.ADVIEW, "image is null");
				
				adViewLayout.adViewManager.resetRollover_pri();
				adViewLayout.rotateThreadedPri();
			
				return;
			}

			ImageView imageView = new ImageView(activity);
			imageView.setImageDrawable(inmobiAD.getAdImg());
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

			RelativeLayout.LayoutParams bannerViewParams = new RelativeLayout.LayoutParams(
			(int)adWidth, (int)adHeight);
			bannerViewParams.addRule(13, -1);
			RelativeLayout bannerLayout = new RelativeLayout(activity);

			bannerLayout.addView(imageView, bannerViewParams);

			adViewLayout.removeAllViews();
			adViewLayout.reportImpression();
		
			adViewLayout.addView(bannerLayout);
			adViewLayout.adViewManager.resetRollover();
			adViewLayout.rotateThreadedDelayed();
			break;
		case 2:
			if (inmobiAD.getLinkText() == null)
			{
				Log.d(AdViewUtil.ADVIEW, "text is null");
				
				adViewLayout.adViewManager.resetRollover_pri();
				adViewLayout.rotateThreadedPri();
			
				return;
			}

			Extra extra = adViewLayout.extra;	
			int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	    		int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		
			TextView adText = new TextView(activity);

			adText.setText(inmobiAD.getLinkText());
			adText.setTypeface(Typeface.DEFAULT_BOLD, 1);
			adText.setTextColor(fgColor);
			adText.setMaxLines(2);
			adText.setTextSize(16.0F);

			RelativeLayout.LayoutParams bannerViewParams2 = new RelativeLayout.LayoutParams(
			(int)adWidth, (int)adHeight);
			bannerViewParams2.setMargins(2, 2, 2, 2);
			bannerViewParams2.addRule(13, -1);
			RelativeLayout bannerLayout2 = new RelativeLayout(activity);

			bannerLayout2.addView(adText, bannerViewParams2);
			bannerLayout2.setBackgroundColor(bgColor);
			//bannerLayout2.setPadding(2,2,2,2);
			
			adViewLayout.removeAllViews();
			adViewLayout.reportImpression();
		
			adViewLayout.addView(bannerLayout2);
			adViewLayout.adViewManager.resetRollover();
			adViewLayout.rotateThreadedDelayed();
		}
	}


}

class DisplayInmobiADRunnable implements Runnable
{
	private InmobiInterfaceAdapter inmobiADAdapter;

	public DisplayInmobiADRunnable(InmobiInterfaceAdapter inmobiADAdapter)
	{
		this.inmobiADAdapter = inmobiADAdapter;
	}

	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "DisplayInmobiADRunnable");
		
		this.inmobiADAdapter.displayInmobiAD();
	}
}

class FetchInmobiADRunnable implements Runnable {
	private InmobiInterfaceAdapter inmobiADAdapter;
	private Ration ration;
	
	public FetchInmobiADRunnable(InmobiInterfaceAdapter inmobiADAdapter, Ration ration)
	{
	       this.inmobiADAdapter = inmobiADAdapter;
	       this.ration = ration;
	}
	 
	public void run() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "FetchInmobiADRunnable");
		
	       AdViewLayout adViewLayout = this.inmobiADAdapter.adViewLayoutReference.get();
	       if (adViewLayout == null) {
			return;
	       }
	       inmobiADAdapter.inmobiAD=inmobiADAdapter.requestInmobiAD(adViewLayout, ration.key);
		   
	       if (inmobiADAdapter.inmobiAD != null)
	       {
	       	adViewLayout.handler.post(new DisplayInmobiADRunnable(inmobiADAdapter));
	       }
		else
		{
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.d(AdViewUtil.ADVIEW, "FetchInmobiAD failure");
			
	       	adViewLayout.adViewManager.resetRollover_pri();
			adViewLayout.rotateThreadedPri();
	       }	
	}
}
