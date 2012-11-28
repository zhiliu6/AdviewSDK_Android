package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

import cn.immob.sdk.ImmobView;
import cn.immob.sdk.LMAdListener;
import android.widget.RelativeLayout;

import java.util.Hashtable;

public class LmMobAdapter extends AdViewAdapter implements LMAdListener{

	private ImmobView adView;
	
	public LmMobAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into LmMob");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		
		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		
		 Hashtable<String, String> ht = new Hashtable<String, String>();
		 ht.put("channelID", "adview_1.7.8");

		 if(AdViewTargeting.getRunMode()==RunMode.TEST) 
			adView = new ImmobView(activity, "6ae20a7ba74216c36d7270ebafcc3bc3", ht);
		 else
		 	adView = new ImmobView(activity, ration.key, ht);
		adView.setAdListener(this);

		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		RelativeLayout.LayoutParams layoutParams;
		layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adViewLayout.addView(adView, layoutParams);
	}

	@Override
	public void onAdReceived(ImmobView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "LmMob success");

		if(adView!=null)
		{
			adView.display();
		}

		adView.setAdListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();

		adViewLayout.reportImpression();
		
	}

	@Override
	public void onFailedToReceiveAd(ImmobView arg0, int arg1) {
		// TODO Auto-generated method stub

		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "LmMob failure, arg1="+arg1);
	    
		adView.setAdListener(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
	}
	
	@Override
	public void onDismissScreen(ImmobView arg0) {
		// TODO Auto-generated method stub
		Log.d(AdViewUtil.ADVIEW, "LmMob onDismissScreen");
	}

	@Override
	public void onLeaveApplication(ImmobView arg0) {
		// TODO Auto-generated method stub
		Log.d(AdViewUtil.ADVIEW, "LmMob onLeaveApplication");
	}

	@Override
	public void onPresentScreen(ImmobView arg0) {
		// TODO Auto-generated method stub
		Log.d(AdViewUtil.ADVIEW, "LmMob onPresentScreen");
	}
	
}
