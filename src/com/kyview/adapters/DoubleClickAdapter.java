package com.kyview.adapters;

import android.app.Activity;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.doubleclick.DfpAdView;
import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

public class DoubleClickAdapter extends AdViewAdapter implements AdListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_DoubleClick;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.google.ads.doubleclick.DfpAdView") != null) {
				registry.registerClass(networkType(), DoubleClickAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public DoubleClickAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		 AdViewUtil.logInfo("model is test");
		 AdViewLayout adViewLayout=adViewLayoutReference.get();
		 if(adViewLayout==null){
			 return ;
		 }
		 Activity activity=adViewLayout.activityReference.get();
		 if(activity==null){
			 return ;
		 }
		 DfpAdView adview=new DfpAdView(activity,AdSize.BANNER,ration.key);
		 adViewLayout.addView(adview);
		 adview.setAdListener(this);
		 adview.loadAd(requestForAdWhirlLayout(adViewLayout));
	}

	private AdRequest requestForAdWhirlLayout(AdViewLayout adViewLayout) {
		 AdRequest result = new AdRequest();
		 //result.addExtra("kw", "banner");
		 return result;
	}

	public void onDismissScreen(Ad arg0) {
		AdViewUtil.logInfo("DoubleClick onDismissScreen");
	}

	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		AdViewUtil.logInfo("DoubleClick fail");
		arg0.setAdListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
	}

	public void onLeaveApplication(Ad arg0) {
		AdViewUtil.logInfo("DoubleClick onLeaveApplication");
	}

	public void onPresentScreen(Ad arg0) {
		AdViewUtil.logInfo("AdMob onPresentScreen");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		
	}

	public void onReceiveAd(Ad arg0) {
		AdViewUtil.logInfo("AdMob success");
		arg0.setAdListener(null);	
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		if (!(arg0 instanceof AdView)) {
			return;
		}
		AdView adView = (AdView)arg0;
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();	 
		
	}



}
