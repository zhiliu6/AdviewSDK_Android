package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;

import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;

import com.kyview.obj.Extra;
import android.graphics.Color;

import com.mdotm.android.ads.MdotMManager;
import com.mdotm.android.ads.MdotMView;
import com.mdotm.android.ads.MdotMView.MdotMActionListener;

public class MdotMAdapter extends AdViewAdapter implements MdotMActionListener {
	
	//private AdView adView=null;
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_MDOTM;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.mdotm.android.ads.MdotMView") != null) {
				registry.registerClass(networkType(), MdotMAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public MdotMAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into MdotM");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}
		//adViewLayout.removeAllViews();

		Activity activity = adViewLayout.activityReference.get();
		if(activity == null) {
			return;
		}
		Extra extra = adViewLayout.extra;
		int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
		int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue); 

		MdotMManager.setPublisherId(ration.key);
		MdotMManager.setMediationLayerName(AdViewUtil.ADVIEW);
      		MdotMManager.setMediationLayerVersion(AdViewUtil.VERSION);
	  
		MdotMView mdotm = new MdotMView(activity, this);
		mdotm.setBackgroundColor(bgColor);
    		mdotm.setTextColor(fgColor);
    		mdotm.setListener(this);
	}

	@Override
	public void adRequestCompletedSuccessfully(MdotMView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "MdotM success");

		 adView.setListener(null);
		 
		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			  return;
		  }
		 // adViewLayout.removeAllViews();

		  adView.setVisibility(View.VISIBLE);	
		  adViewLayout.adViewManager.resetRollover();
		  adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		  adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void adRequestFailed(MdotMView adView) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "MdotM failure");
	    
		  adView.setListener(null);

		  AdViewLayout adViewLayout = adViewLayoutReference.get();
		  if(adViewLayout == null) {
			 return;
		  } 
		 adViewLayout.adViewManager.resetRollover_pri();
		 adViewLayout.rotateThreadedPri();
		
	}


	
}
