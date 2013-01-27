package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

import com.wqmobile.sdk.WQAdEventListener;
import com.wqmobile.sdk.WQAdView;

public class WqAdapter extends AdViewAdapter implements WQAdEventListener{

	public WqAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
 			Log.d(AdViewUtil.ADVIEW, "Into WQ");
 		AdViewLayout adViewLayout = adViewLayoutReference.get();
 		if(adViewLayout == null) {
 			return;
 		}
 		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			  return;
		}

		WQAdView adView = new WQAdView(activity);
		adView.setAdEventListener(this);
		adView.init(ration.key, ration.key2);
		//LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		//adViewLayout.addView(adView, params);
		adViewLayout.AddSubView(adView);
	}

	@Override
	public void onWQAdReceived(WQAdView adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onWQAdReceived");

		adView.setAdEventListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		adViewLayout.reportImpression();	
		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onWQAdFailed(WQAdView adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onWQAdFailed");
		adView.setAdEventListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
	}

	@Override
	public void onWQAdDismiss(WQAdView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.i(AdViewUtil.ADVIEW, "onWQAdDismiss");
	}

	@Override
	public void onWQAdLoadTimeout(WQAdView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.i(AdViewUtil.ADVIEW, "onWQAdDismiss");
	}
		
}
