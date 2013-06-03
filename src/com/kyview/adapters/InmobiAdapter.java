package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.RelativeLayout;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import com.inmobi.androidsdk.IMAdListener;
import com.inmobi.androidsdk.IMAdRequest;
import com.inmobi.androidsdk.IMAdView;
import com.inmobi.androidsdk.IMAdRequest.ErrorCode;


import java.util.Map;
import java.util.HashMap;

public class InmobiAdapter extends AdViewAdapter {
	private IMAdView mIMAdView = null;
	private IMAdRequest mAdRequest;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_INMOBI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.inmobi.androidsdk.IMAdView") != null) {
			
				registry.registerClass(networkType(), InmobiAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public InmobiAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Inmobi");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		// set the test mode to true (Make sure you set the test mode to false
		// when distributing to the users)
		mIMAdView = new IMAdView(activity, IMAdView.INMOBI_AD_UNIT_320X50, ration.key);
		mAdRequest = new IMAdRequest();
	    if(AdViewTargeting.getRunMode()==RunMode.TEST)
	    	mAdRequest.setTestMode(true); 
	    else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
	    	mAdRequest.setTestMode(false);
	    else{
	    	mAdRequest.setTestMode(false);
	    } 
		Map<String,String> reqParams = new HashMap<String,String>();
		reqParams.put("tp","c_adview");
		mAdRequest.setRequestParams(reqParams);



		mIMAdView.setIMAdRequest(mAdRequest);
		mIMAdView.setIMAdListener(mIMAdListener);
		//adViewLayout.addView(mIMAdView);
		//mIMAdView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));	
		mIMAdView.loadNewAd(mAdRequest);
		//adViewLayout.adViewManager.resetRollover(); 
		//adViewLayout.rotateThreadedDelayed();

	}
	private IMAdListener mIMAdListener = new IMAdListener() {

		public void onShowAdScreen(IMAdView adView) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.i(AdViewUtil.ADVIEW, "ImMobi, onShowAdScreen");
 
		}

		public void onDismissAdScreen(IMAdView adView) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.i(AdViewUtil.ADVIEW, "ImMobi, onShowAdScreen");
		}

		public void onLeaveApplication(IMAdView adView) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				Log.i(AdViewUtil.ADVIEW, "ImMobi, onShowAdScreen");
		}
		
		public void onAdRequestFailed(IMAdView adView, ErrorCode errorCode) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				  Log.d(AdViewUtil.ADVIEW, "ImMobi failure, errorCode="+errorCode);
		    
			adView.setIMAdListener(null);

			AdViewLayout adViewLayout = adViewLayoutReference.get();
			if(adViewLayout == null) {
				return; 
			}
			adViewLayout.adViewManager.resetRollover_pri();
			adViewLayout.rotateThreadedPri();

		} 

		public void onAdRequestCompleted(IMAdView adView) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
				  Log.d(AdViewUtil.ADVIEW, "InMobi success");

			adView.setIMAdListener(null);
			
			  AdViewLayout adViewLayout = adViewLayoutReference.get();
			  if(adViewLayout == null) {
				  return;
			  }
			  //RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			  //layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			  //mIMAdView.setLayoutParams(layoutParams);
			  //mIMAdView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));				
			  adViewLayout.adViewManager.resetRollover();
			  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
			  adViewLayout.rotateThreadedDelayed(); 
		}
	};



}
