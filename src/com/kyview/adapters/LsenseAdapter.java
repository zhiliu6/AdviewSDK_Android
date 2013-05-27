package com.kyview.adapters;

import android.app.Activity;
import android.graphics.Color;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.l.adlib_android.AdListenerEx;
import com.l.adlib_android.AdView;
//import android.util.DisplayMetrics;


public class LsenseAdapter extends AdViewAdapter implements AdListenerEx{
	//static private AdView adView = null;
	private AdView adView = null;

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_LSENSE;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.l.adlib_android.AdView") != null) {
				registry.registerClass(networkType(), LsenseAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public LsenseAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}
	

	@SuppressWarnings("unused")
	@Override
	public void handle() {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Into Lsense");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}

		Extra extra = adViewLayout.extra;
	    int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	    int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
		Activity activity = adViewLayout.activityReference.get();
		if (activity == null) {
			return;
		}
		try {
			//adView = new AdView(activity);
			adView = null;
//			adView = new AdView(
//					activity, 
//					Integer.valueOf(ration.key), 
//					Color.rgb(65, 65, 65), 
//					bgColor, 
//					fgColor, 
//					255,5,
//					true);	
			adView = new AdView(activity, Integer.valueOf(ration.key), AdView.ROTATE3D);
		/*	
	        DisplayMetrics dm = new DisplayMetrics();
	        dm = adViewLayout.getContext().getApplicationContext().getResources().getDisplayMetrics();
	        int screenHeight = dm.heightPixels;	
	        if(screenHeight > 320){
	        	adView.setBannerSize(640, 96); 
	        }else{
	        	adView.setBannerSize(640, 76); 	        	
	        }
	   */     
			adView.setOnAdListenerEx(this);		
			adViewLayout.adViewManager.resetRollover();
			adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
			adViewLayout.rotateThreadedDelayed();
			 
		} catch (IllegalArgumentException e) {
			adViewLayout.rollover();
			return; 
		}
	}
	@Override
	public void OnConnectFailed(String arg0) {
		AdViewUtil.logInfo("Lsense failure");
		adView.setOnAdListenerEx(null);

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			 return;
		}
		adViewLayout.adViewManager.resetRollover_pri();
		//adViewLayout.rotateThreadedPri();
	}
	
	@Override
	public void OnAcceptAd(int arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Lsense success");

		adView.setOnAdListenerEx(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if (adViewLayout == null) {
			return;
		}
		
		//adViewLayout.adViewManager.resetRollover();
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		//adViewLayout.rotateThreadedDelayed();
		
	}



}
