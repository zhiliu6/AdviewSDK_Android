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
import android.view.View;

public class AdChinaAdapter extends AdViewAdapter implements AdListener{

	public AdChinaAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		 
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
	 	 
		// ���ù�����
		AdEngine.setAdListener(this);	  
		AdManager.setRefershinterval(-1);
		AdView mAdView=new AdView(adViewLayout.getContext(), ration.key, true, false);
		mAdView.setVisibility(View.VISIBLE);	
		
	}
	
	public void onFailedToReceiveAd(AdView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onFailedToReceiveAd");
	    
		AdEngine.setAdListener(null);

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

		AdEngine.setAdListener(null);
		  
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
		return;
		}

		adViewLayout.adViewManager.resetRollover();
		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();
	}

	public void onClickBanner (AdView view)
	{
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "onClickBanner");	
	}
	
	public void onReceiveVideoAd(){
		
	}

	public void onEndFullScreenLandpage() {

	}

	public void onStartFullScreenLandPage(){

	}
	
	/**
	 * Called when user clicks on an sms button on ad the promptText will be
	 * something like SMS:123 To:1000 You can pop up an AlertDialog here and
	 * show the promptText return true for OK, return false for Cancel or you
	 * may simply return true to allow sms
	 */
	public boolean OnRecvSms(AdView adView, String promptText) {
		return true;
	}

	public void onFailedToRefreshAd(AdView arg0) {

	}

	public void onRefreshAd(AdView arg0) {

	}

	public void onFailedToPlayVideoAd() {

	}

	public void onPlayVideoAd() {

	}

	public void onFailedToReceiveVideoAd() {

	}

	public void onReceiveFullScreenAd() {

	}

	public void onFailedToReceiveFullScreenAd() {

	}

	public void onDisplayFullScreenAd() {

	}
}
