package com.kyview.adapters;

import android.graphics.Color;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.ad.KyAdView;
import com.kyview.ad.KyAdView.onAdListener;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

import com.kyview.AdViewAdRegistry;


public class AdViewHouseAdapter extends AdViewAdapter implements onAdListener
{
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADVIEWAD;
	}
	
	public static void load(AdViewAdRegistry registry) {
		registry.registerClass(networkType(), AdViewHouseAdapter.class);
	}

	public AdViewHouseAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
	}
	
	@Override
	public void handle() {
		//Log.d(AdViewUtil.ADVIEW, "Into AdViewHouse");
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into AdViewHouse");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		 return;
	 	 }
	
	 	Extra extra = adViewLayout.extra;
		int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		KyAdView kyAdView=null;
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			kyAdView= new KyAdView(adViewLayout.getContext(), ration.key,ration.key2,ration.logo, extra.cycleTime, true,bgColor, fgColor, adViewLayout.keyDev);   
		else{
			kyAdView= new KyAdView(adViewLayout.getContext(), ration.key,ration.key2,ration.logo, extra.cycleTime, false,bgColor, fgColor, adViewLayout.keyDev);
		}

		kyAdView.setAdListener(this);
		kyAdView.setHorizontalScrollBarEnabled(false);
		kyAdView.setVerticalScrollBarEnabled(false);

	}

	@Override
	public void onConnectFailed(KyAdView view) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "AdViewHouse failure");
	    
		view.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
		
	}

	@Override
	public void onReceivedAd(KyAdView view) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "AdViewHouse success");

		view.setAdListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, view));
		adViewLayout.rotateThreadedDelayed();
		
	}


		
	
}