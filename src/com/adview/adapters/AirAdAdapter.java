package com.adview.adapters;

import android.app.Activity;
import android.util.Log;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;

//import com.adview.AdViewLayout.ViewAdRunnable;
import com.adview.AdViewTargeting.RunMode;
import com.adview.obj.Ration;
import com.adview.util.AdViewUtil;


import com.mt.airad.AirAD;
import com.mt.airad.AirADListener;

//import android.widget.RelativeLayout;

public class AirAdAdapter extends AdViewAdapter implements AirADListener {
	
	private AirAD airAD=null;
	public AirAdAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			AirAD.setAppID(AirAD.TEST_APP_ID);
		else
			AirAD.setAppID(ration.key);
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			AirAD.setDebugMode(AirAD.DebugMode_ON);
		AirAD.setGPSMode(AirAD.GPSMode_ON);
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Into AirAD");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		  
		airAD=new AirAD(activity);

		airAD.setBannerBGMode(AirAD.BANNER_BG_ON);
		airAD.setRefreshMode(AirAD.REFRESH_MODE_MANUAL);
		//airAD.setIntervalTime(20);
		
		airAD.setAirADListener(this);

		adViewLayout.AddSubView(airAD);
		airAD.refreshAD();
	}

	@Override
	public void onAdReceived() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "AirAD success");

		airAD.setAirADListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, airAD));
		adViewLayout.rotateThreadedDelayed();
		
	}

	@Override
	public void onAdReceivedFailed(int errorCode) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "AirAD failure, errorCode="+errorCode);

		airAD.setAirADListener(null);
		AirAD.onDestroy();
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
	}

            @Override
            public void onAdContentShow() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdContentShow");
            }

            @Override
            public void onAdContentLoadFinished() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdContentLoadFinished");

            }

            @Override
            public void onAdContentClose() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdContentClose");
            }

            @Override
            public void onAdBannerShown() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdBannerShown");

            }
}
