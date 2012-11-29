package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;


import com.tencent.mobwin.AdListener;
import com.tencent.mobwin.AdView;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;


public class MobWinAdapter extends AdViewAdapter implements AdListener{
	private AdView adView;
	
	public MobWinAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into MobWin");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	Extra extra = adViewLayout.extra;
	    int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	    //int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
	    Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }  	     
	    // Instantiate an ad view and add it to the view
	
	   adView = new AdView(activity, ration.key, "adview", "ben1574leo");
	   adView.setAdListener(this);	
	   adView.setBackgroundColor(bgColor);

	   adViewLayout.AddSubView(adView);
	}

	@Override
	public void onReceiveFailed(int errorId) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveFailed, errorId="+errorId);
	    
		  adView.setAdListener(null);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onReceiveAd() {
		// TODO Auto-generated method stub
	
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveAd");

		adView.setAdListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		adViewLayout.reportImpression();	
		  adViewLayout.adViewManager.resetRollover();
		  //adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
		
	}

	//@Override
	public void onAdClick() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onAdClick");
		
	}	
	

}
