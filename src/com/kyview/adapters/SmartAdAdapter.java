package com.kyview.adapters;


import android.app.Activity;
import android.util.Log;


import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import cn.smartmad.ads.android.SMAdBannerListener;
import cn.smartmad.ads.android.SMAdBannerView;
import cn.smartmad.ads.android.SMAdManager;
import cn.smartmad.ads.android.SMRequestEventCode;

public class SmartAdAdapter extends AdViewAdapter implements SMAdBannerListener{
	private SMAdBannerView smAdBannerView;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_SMARTAD;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("cn.smartmad.ads.android.SMAdBannerView") != null) {
				registry.registerClass(networkType(), SmartAdAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public SmartAdAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into SmartAd");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	   
		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}  	         

		SMAdManager.setApplicationId(activity, ration.key);
		SMAdManager.setAdRefreshInterval(300);
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			SMAdManager.setDebuMode(true);
		else
			SMAdManager.setDebuMode(false);
		smAdBannerView = new SMAdBannerView(activity, ration.key2, SMAdBannerView.AUTO_AD_MEASURE);
		smAdBannerView.setSMAdBannerListener(this);
	}

	@Override
	public void onAppResumeFromAd(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onAppResumeFromAd");

	}

	@Override
	public void onAppSuspendForAd(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onAppSuspendForAd");

	}

	public void onAttachedToScreen(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onAttachedToScreen");

	}

	@Override
	public void onClickedAd() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "smartmad onClickedAd");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.reportClick();	
	}

	@Override
	public void onClosedAdExpand(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onClosedAdExpand");

	}

	@Override
	public void onDetachedFromScreen(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onDetachedFromScreen");

	}

	@Override
	public void onExpandedAd(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onExpandedAd");

	}

	@Override
	public void onLeaveApplication(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onLeaveApplication");

	}

	@Override
	public void onReceivedAd(SMAdBannerView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onReceivedAd");
		
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }

		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		  adViewLayout.rotateThreadedDelayed();		

	}

	@Override
	public void onAttachedToScreen(SMAdBannerView arg0, SMRequestEventCode arg1) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onAttachedToScreen");
		
	}

	@Override
	public void onFailedToReceiveAd(SMAdBannerView arg0, SMRequestEventCode arg1) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
	 		Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveAd");

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "smartmad failure, SMRequestEventCode="+arg1);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  }
		 adViewLayout.adViewManager.resetRollover_pri();
		 adViewLayout.rotateThreadedPri();		
	}


}
