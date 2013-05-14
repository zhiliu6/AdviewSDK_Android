package com.kyview.adapters;

import android.util.Log;

import com.adchina.android.ads.*;
import com.adchina.android.ads.views.AdView;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import android.view.View;

public class AdChinaAdapter extends AdViewAdapter implements AdBannerListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADCHINA;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.adchina.android.ads.views.AdView") != null) {
				registry.registerClass(networkType(), AdChinaAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public AdChinaAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into AdChina");
	 	AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		return;
	 	}

		AdManager.setRefershinterval(-1);
		AdManager.setRelateScreenRotate(adViewLayout.getContext(),true);	
		AdView mAdView=new AdView(adViewLayout.getContext(), ration.key, true, false);
		mAdView.setAdBannerListener(this);
		
		mAdView.setVisibility(View.VISIBLE);	
		mAdView.start();
	}
	
	public void onFailedToReceiveAd(AdView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveAd");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri(); 
	}

	public void onReceiveAd(AdView adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onReceiveAd");


		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
		return;
		}

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public boolean OnRecvSms(AdView arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClickBanner(AdView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedToRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefreshAd(AdView arg0) {
		// TODO Auto-generated method stub
		
	}


	

}
