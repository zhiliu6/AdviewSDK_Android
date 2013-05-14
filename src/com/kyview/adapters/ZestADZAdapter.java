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
import com.zestadz.android.AdManager;
import com.zestadz.android.ZestADZAdView;
import com.zestadz.android.ZestADZAdView.ZestADZListener;

public class ZestADZAdapter extends AdViewAdapter implements ZestADZListener{

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ZESTADZ;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.zestadz.android.ZestADZAdView") != null) {
				registry.registerClass(networkType(), ZestADZAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public ZestADZAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		try {
			AdManager.setadclientId(ration.key);    
		}
		// Thrown on invalid client id.
		catch(IllegalArgumentException e) {
			adViewLayout.rollover();
			return;
		}
		try {
			Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
			
			ZestADZAdView adView = new ZestADZAdView(activity);
			adView.setListener(this);
			adView.displayAd();          
		}
		catch (Exception e) {               
			adViewLayout.rollover();                       
		}
		
	}

	@Override
	public void AdFailed(ZestADZAdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "ZestADZ fail");
		arg0.setListener(null);
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	    	return;
	    }
	    adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
	}

	@Override
	public void AdReturned(ZestADZAdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "ZestADZ success");
		arg0.setListener(null);
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	      return;
	    }
	    adViewLayout.adViewManager.resetRollover();
	    adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
	    adViewLayout.rotateThreadedDelayed();
		
	}



}
