package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import android.view.ViewGroup;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

import com.baidu.mobads.AdService;
//import com.baidu.mobads.AdType;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
//import com.kyview.obj.Extra;
//import android.graphics.Color;
import android.content.Context;
import org.json.JSONObject;


public class AdBaiduAdapter extends AdViewAdapter implements AdViewListener  {
	
	//private AdView adView;
	//private AdService adService;
	
	public AdBaiduAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);

		Context mContext = (Context)adViewLayout.activityReference.get();
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
		{
			AdView.setAppSid(mContext, "debug");
			AdView.setAppSec(mContext, "debug");	
		}
		else
		{
			AdView.setAppSid(mContext, ration.key);
			AdView.setAppSec(mContext, ration.key2);	
		}
	}

	@Override
	public void handle() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Baidu");
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		adViewLayout.removeAllViews();
		adViewLayout.activeRation = adViewLayout.nextRation;

		//if((ration.key3).compareTo("1")==0)
		//	new AdService(activity,adViewLayout, new ViewGroup.LayoutParams(-1, -2), this);
		//else
			new AdService(activity, adViewLayout, new ViewGroup.LayoutParams(-1, -2), this);
	}

	public void onAdReady(AdView adView2) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "onAdReady");
		
		try {
			//adView = adService.requestAdView();
			//adView = adView2;
			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if(adViewLayout == null) {
				return;
		 	}
			
			//Extra extra = adViewLayout.extra;
		       //int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		       //int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue); 
			//adView2.setBackgroundColor(bgColor);
      			//adView2.setTextColor(fgColor);
	  
		} catch (Exception e) {
			Log.w(AdViewUtil.ADVIEW, e.getMessage());
		}
	}

	public void onAdShow(JSONObject paramJSONObject) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "onAdShow");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportBaiduImpression();
	}

	public void onAdClick(JSONObject paramJSONObject) { 
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "onAdClick");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();
	}

	public void onAdFailed(String reason) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "AdViewListener.onAdFailed, reason="+reason);	

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
	}

	public void onAdSwitch() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "onAdSwitch");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onVideoFinish() {

	}

	@Override
	public void onVideoStart() {

	}
}
