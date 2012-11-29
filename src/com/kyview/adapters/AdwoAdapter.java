package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.adwo.adsdk.AdwoAdView;
import com.adwo.adsdk.AdListener;
import com.adwo.adsdk.ErrorCode;

public class AdwoAdapter extends AdViewAdapter implements AdListener{

	public AdwoAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Adwo");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		Extra extra = adViewLayout.extra;
		AdwoAdView adView=null;
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			adView=new AdwoAdView((Activity)adViewLayout.getContext(), ration.key,true,extra.cycleTime);
		else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
			adView=new AdwoAdView((Activity)adViewLayout.getContext(), ration.key,false,extra.cycleTime);
		else{
			adView=new AdwoAdView((Activity)adViewLayout.getContext(), ration.key,false,extra.cycleTime);
		}

		byte id=2;	
		AdwoAdView.setAggChannelId(id);
		adView.setListener(this);
		adViewLayout.AddSubView(adView);
		//*
		//adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		//adViewLayout.rotateThreadedDelayed();
		//*/
		//adView.finalize();
	}

	//@Override
	public void onFailedToReceiveAd(AdwoAdView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveAd");
	    
		  adView.setListener(null);
		  //adView.finalize();

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		 adViewLayout.rotateThreadedPri();
	}

	@Override
	public void onFailedToReceiveAd(AdwoAdView adView, ErrorCode arg1) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveAd, arg1="+arg1);

		  adView.setListener(null);
		  //adView.finalize();

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		 adViewLayout.rotateThreadedPri();
	}
	
	//@Override
	public void onFailedToReceiveRefreshedAd(AdwoAdView paramAdView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveRefreshedAd");
	}

	@Override
	public void onReceiveAd(AdwoAdView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveAd");

		adView.setListener(null);
		  //adView.finalize();
		  
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		adViewLayout.reportImpression();		
		  adViewLayout.adViewManager.resetRollover();
		  //adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
	};
	
}
