package com.adview.adapters;

import android.graphics.Color;
import android.util.Log;

import com.adview.AdViewLayout;
import com.adview.AdViewTargeting;
//import com.adview.AdViewLayout.ViewAdRunnable;
import com.adview.AdViewTargeting.RunMode;
import com.adview.obj.Extra;
import com.adview.obj.Ration;
import com.adview.util.AdViewUtil;

import com.wiyun.ad.AdView;
import com.wiyun.ad.AdView.AdListener;
import android.widget.RelativeLayout;


public class WiyunAdapter extends AdViewAdapter implements AdListener{
	AdView ad;

	public WiyunAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		
	}

	
	@Override
	public void handle() {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Wiyun");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
	 	Extra extra = adViewLayout.extra;
		int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		     
		// Instantiate an ad view and add it to the view
		ad=new AdView(adViewLayout.getContext());

		ad.setBackgroundColor(bgColor);
		ad.setTextColor(fgColor);
		ad.setResId(ration.key);
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			ad.setTestMode(true);
		else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
			ad.setTestMode(false);
		else{
			ad.setTestMode(false);
		}

		ad.setTransitionType(AdView.TRANSITION_TOP_PUSH);
		ad.setRefreshInterval(0);
		ad.requestAd();
		ad.setListener(this);	

		adViewLayout.removeAllViews();
		adViewLayout.activeRation = adViewLayout.nextRation;
		RelativeLayout.LayoutParams layoutParams;
		layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adViewLayout.addView(ad, layoutParams);
		
	}

public void onAdClicked()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onAdClicked");

	AdViewLayout adViewLayout = adViewLayoutReference.get();
	if(adViewLayout == null) {
		return;
	}
	
	adViewLayout.reportClick();
}

public void onAdLoadFailed()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onAdLoadFailed");	

	ad.setListener(null);

	AdViewLayout adViewLayout = adViewLayoutReference.get();
	if(adViewLayout == null) {
		return;
	}
	adViewLayout.adViewManager.resetRollover_pri();
	adViewLayout.rotateThreadedPri();	
}

public void onAdLoaded()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onAdLoaded");

	ad.setListener(null);
	
	AdViewLayout adViewLayout = adViewLayoutReference.get();
	if(adViewLayout == null) {
		return;
	}

	adViewLayout.adViewManager.resetRollover();
	//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, ad));
	adViewLayout.rotateThreadedDelayed();

	adViewLayout.reportImpression();
}

public void onExitButtonClicked()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		 Log.d(AdViewUtil.ADVIEW, "Wiyun onExitButtonClicked");	
}


public void onAppDownloadFailed()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onAppDownloadFailed");	
}

public void onAppDownloaded()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onAppDownloaded");	
}
 
public void onMiniSiteClosed()
{
	if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Wiyun onMiniSiteClosed");	
}


}
