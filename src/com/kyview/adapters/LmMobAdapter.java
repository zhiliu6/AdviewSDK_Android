package com.kyview.adapters;

import java.util.Hashtable;

import android.app.Activity;
import android.widget.RelativeLayout;
import cn.immob.sdk.ImmobView;
import cn.immob.sdk.LMAdListener;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
//import com.kyview.AdViewLayout.ViewAdRunnable;

public class LmMobAdapter extends AdViewAdapter implements LMAdListener{

	private ImmobView adView;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_LMMOB;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("cn.immob.sdk.ImmobView") != null) {
				registry.registerClass(networkType(), LmMobAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public LmMobAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		AdViewUtil.logInfo("Into LmMob");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		
		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		
		 Hashtable<String, String> ht = new Hashtable<String, String>();
		 ht.put("channelID", "adview_1.9.2");

		 if(AdViewTargeting.getRunMode()==RunMode.TEST) 
			adView = new ImmobView(activity, "6ae20a7ba74216c36d7270ebafcc3bc3", ht);
		 else
		 	adView = new ImmobView(activity, ration.key, ht);
		adView.setAdListener(this);

		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		RelativeLayout.LayoutParams layoutParams;
		layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		adViewLayout.addView(adView, layoutParams);
	}

	@Override
	public void onAdReceived(ImmobView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("LmMob success");
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

		AdViewUtil.logInfo("LmMob failure, arg1="+arg1);
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
		AdViewUtil.logInfo("LmMob onDismissScreen");
	}

	@Override
	public void onLeaveApplication(ImmobView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("mMob onLeaveApplication");
	}

	@Override
	public void onPresentScreen(ImmobView arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("LmMob onPresentScreen");
	}


	
}
