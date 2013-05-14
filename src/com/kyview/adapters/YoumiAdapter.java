package com.kyview.adapters;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewLinstener;
import android.app.Activity;
import android.util.Log;


import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;

public class YoumiAdapter extends AdViewAdapter implements AdViewLinstener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_YOUMI;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("net.youmi.android.banner.AdView") != null) {
				registry.registerClass(networkType(), YoumiAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public YoumiAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
		String key=new String(ration.key);
		String key2=new String(ration.key2);

		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			  return;
		 }
		if(AdViewTargeting.getRunMode()==RunMode.TEST){
			if(adViewLayout.adViewManager.getYoumiInit()){
				AdManager.getInstance(activity).init(key, key2, true);
				adViewLayout.adViewManager.setYoumiInit(false);
			}
		}
		else{
			if(adViewLayout.adViewManager.getYoumiInit()){
				AdManager.getInstance(activity).init(key, key2, false);
				adViewLayout.adViewManager.setYoumiInit(false);
			}
		}
	}
	
	 	public void handle() {
	 		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 			Log.d(AdViewUtil.ADVIEW, "Into Youmi");
	 		AdViewLayout adViewLayout = adViewLayoutReference.get();
	 		if(adViewLayout == null) {
	 			return;
	 	 	}
	    		Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
			AdView adView=new AdView(activity,AdSize.SIZE_320x50);  
			adView.setAdListener(this);

			adViewLayout.AddSubView(adView);
	}

	@Override
	public void onSwitchedAd(AdView adView)
	{
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onSwitchedAd");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		adView.setAdListener(null);

		adViewLayout.reportImpression();
		adViewLayout.adViewManager.resetRollover();
		// adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onReceivedAd(AdView adView)
	{
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onReceivedAd");
	}
	
	@Override
	public void onFailedToReceivedAd(AdView adView)
	{
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onFailedToReceivedAd");

		adView.setAdListener(null);
	
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
	}


	
}
