package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;

import cn.aduu.android.AdListener;
import cn.aduu.android.AdView;
import cn.aduu.android.AdViewSize;
import cn.aduu.android.AdManager;

public class AduuAdapter extends AdViewAdapter implements AdListener{
	private AdView adView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADUU;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("cn.aduu.android.AdView") != null) {
				registry.registerClass(networkType(), AduuAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub	
		

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Aduu new"); 
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		//if(AdViewTargeting.getRunMode()==RunMode.TEST)
		AdManager.init(activity, ration.key, ration.key2, 90,0, 0,61);//-9, 0, 2, 61
		adView = new AdView(activity, null);
		adView.setBannerSize(AdViewSize.SIZE_320X50);
		adView.setSingleLine(false);
		adView.setCloseable(false);
		
		adView.setAdViewListener(this);
		adViewLayout.AddSubView(adView);
	}

	public void onReceiveSuccess() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "aduu--onReceiveAd");

		adView.setAdViewListener(null);
		  
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
	}

	//@Override
	public void onReceiveFail(int code) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "aduu--onReceiveFail, code="+code);
	    
		adView.setAdViewListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
	}	
	
	public void onAdSwitch() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "aduu--onAdSwitch");
	}	
	
	@Override
	public void onPresentScreen() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "aduu--onPresentScreen");
	}

	@Override
	public void onDismissScreen() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "aduu--onDismissScreen");
	}
			
}
