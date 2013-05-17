package com.kyview.adapters;

import android.app.Activity;
import android.content.Context;
//import android.graphics.Color;
import android.util.Log;


//import cn.domob.android.ads.DomobAdListener;
import cn.domob.android.ads.DomobAdManager;
import cn.domob.android.ads.DomobAdView;
import cn.domob.android.ads.DomobAdEventListener;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
//import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;


public class DomobAdapter extends AdViewAdapter implements DomobAdEventListener{
	
	private AdViewLayout adViewLayout;
	private Context mContext;

	public DomobAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		mContext = (Context)adViewLayout.activityReference.get();
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Domob");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	//Extra extra = adViewLayout.extra;
	  //  int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	  // int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
	    Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }  	     
	    // Instantiate an ad view and add it to the view
		
	    DomobAdView ad=new DomobAdView(activity, ration.key, DomobAdView.INLINE_SIZE_320X50);
	    //DomobAdManager.setPublisherId(ration.key);
/*	    if(AdViewTargeting.getRunMode()==RunMode.TEST)
	    	DomobAdManager.setIsTestMode(true);
	    else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
	    	DomobAdManager.setIsTestMode(false);
	    else{
	    	DomobAdManager.setIsTestMode(false);
	    }*/
	    ad.setAdEventListener(this);
	    //ad.setBackgroundColor(bgColor);
	    //ad.setPrimaryTextColor(fgColor);
	
	   ad.requestAdForAggregationPlatform() ;
	    //ad.setRequestInterval(0);
		
	   
	}

	//@Override
	public void onDomobAdFailed(DomobAdView adView, DomobAdManager.ErrorCode paramErrorCode) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Domob failure, ErrorCode="+paramErrorCode);
	    
		  //adView.setAdEventListener(null);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onDomobAdReturned(DomobAdView adView) {
		// TODO Auto-generated method stub
	
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Domob success");

		//adView.setAdEventListener(null);
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
		
	}

	public void onDomobAdClicked(DomobAdView adView) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Domob onDomobAdClicked");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();		
	}

	public void onDomobLeaveApplication(DomobAdView adView) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Domob onDomobLeaveApplication");
		
	}
	
	@Override
	public void onDomobAdOverlayPresented(DomobAdView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.i(AdViewUtil.ADVIEW, "overlayPresented");
	}

	@Override
	public void onDomobAdOverlayDismissed(DomobAdView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.i(AdViewUtil.ADVIEW, "Overrided be dismissed");
		
	}

	@Override
	public Context onDomobAdRequiresCurrentContext() {
		// TODO Auto-generated method stub
		return mContext;
	}
}
