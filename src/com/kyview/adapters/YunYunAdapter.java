package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.suizong.mobile.ads.Ad;
import com.suizong.mobile.ads.AdError;
import com.suizong.mobile.ads.AdListener;
import com.suizong.mobile.ads.AdRequest;
import com.suizong.mobile.ads.AdSize;
import com.suizong.mobile.ads.AdView;

public class YunYunAdapter extends AdViewAdapter implements AdListener {
	AdView adView;
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_YUNYUN;
	}

	public static void load(AdViewAdRegistry registry) {
		try {
			if (Class.forName("com.suizong.mobile.ads.AdListener") != null) {
				registry.registerClass(networkType(), YunYunAdapter.class);
			}
		} catch (ClassNotFoundException e) {
		}
	}

	@Override
	public void handle() {

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		AdRequest request = new AdRequest();
		if (AdViewTargeting.getRunMode() == RunMode.TEST) {
			Log.d(AdViewUtil.ADVIEW, "Into YunYun");
			request.setTesting(true);
		}else{
			request.setTesting(false);
		}
		request.setRefreshTime(0); 
		AdView adView = new AdView(activity, AdSize.BANNER,
				ration.key);
		adViewLayout.AddSubView(adView);

		adView.loadAd(request);
		// 璁剧疆鐩戝惉鍣�
		adView.setAdListener(this);

	}

	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, AdError arg1) {
		if(!arg1.toString().equals("[3]There was an internal error.")){
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "YunYun onAdLoadFailed");	
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();	
		}
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveAd(Ad arg0, long arg1) {
//		if (AdViewTargeting.getRunMode() == RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onReceiveAd");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
//		adView.setAdListener(null);
		
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		adViewLayout.reportImpression();

	}


}
