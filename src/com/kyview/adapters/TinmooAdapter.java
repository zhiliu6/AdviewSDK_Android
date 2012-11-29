package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.ignitevision.android.ads.AdManager;
import com.ignitevision.android.ads.AdView;

public class TinmooAdapter extends AdViewAdapter{

	public TinmooAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into Tinmoo");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
 		if(adViewLayout == null) {
 			return;
 		}
 		
 		Activity activity = adViewLayout.activityReference.get();
 		if(activity == null) {
		  return;
 		}
 		if(AdViewTargeting.getRunMode()==RunMode.TEST)
 			AdManager.setTest(true);
 		else
 			AdManager.setTest(false);
		AdManager.setPublisherKey(ration.key);
 		AdView adView = new AdView(activity);
     
        adViewLayout.adViewManager.resetRollover();
	    adViewLayout.handler.post(new AdViewLayout.ViewAdRunnable(adViewLayout, adView));
	    adViewLayout.rotateThreadedDelayed();
		
	}

	
}
