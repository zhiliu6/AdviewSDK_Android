package com.adview.adapters;

import android.app.Activity;
import android.util.Log;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
//import com.adview.AdViewLayout.ViewAdRunnable;
import com.adview.AdViewTargeting.RunMode;
import com.adview.obj.Ration;
import com.adview.util.AdViewUtil;
import com.vpon.adon.android.AdListener;
import com.vpon.adon.android.AdOnPlatform;
import com.vpon.adon.android.AdView;

public class VponAdapter extends AdViewAdapter implements AdListener{
	private int adHeight;
	private int adWidth;
	
	public VponAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
	}

	private void calcAdSize(AdViewLayout adViewLayout) {
		int width=320;
		int height=48;
		int screenWidth = adViewLayout.adViewManager.width;
		if (screenWidth <= 480) {
			width = 320;
			height = 48;
		} else if (screenWidth < 728) {
			width = 480;
			height = 72;
		} else if (screenWidth >= 728) {
			width = 720;
			height = 108;
		}

		adHeight = AdViewUtil.convertToScreenPixels(height, adViewLayout.mDensity);
		adWidth = AdViewUtil.convertToScreenPixels(width, adViewLayout.mDensity);
	}
	
	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Vpon");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		calcAdSize(adViewLayout);
		try {
			AdView adView = new AdView(activity, adWidth, adHeight);
			boolean autoRefreshAd = false;
			if (adViewLayout.adViewManager.bLocationForeign == false)
				adView.setLicenseKey(ration.key, AdOnPlatform.CN, autoRefreshAd);
			else
				adView.setLicenseKey(ration.key, AdOnPlatform.TW, autoRefreshAd);	
			adView.setAdListener(this);
			
			
			//adViewLayout.addView(adView, new LayoutParams(
			//		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView)); 
			adViewLayout.AddSubView(adView);
			
		} catch (IllegalArgumentException e) {
			adViewLayout.rollover();
			return;
		}
	}

	@Override
	public void onFailedToRecevieAd(AdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Vpon fail");
		arg0.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onRecevieAd(AdView arg0) {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Vpon success");

		arg0.setAdListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		adViewLayout.reportImpression();	
		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
		
	}

}
