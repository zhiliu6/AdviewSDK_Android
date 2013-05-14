package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;

import com.greystripe.sdk.GSAd;
import com.greystripe.sdk.GSAdErrorCode;
import com.greystripe.sdk.GSFullscreenAd;
import com.greystripe.sdk.GSMobileBannerAdView;
import com.greystripe.sdk.GSAdListener;



public class GreystripeAdapter extends AdViewAdapter implements GSAdListener,GSAd 
{

	private GSFullscreenAd myFullscreenAd;


	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_GREYSTRIP;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.greystripe.sdk.GSMobileBannerAdView") != null) {
				registry.registerClass(networkType(), GreystripeAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public GreystripeAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		 if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "Into Greystripe");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		try {
			Activity activity = adViewLayout.activityReference.get();
			if(activity == null) {
				return;
			}
			  myFullscreenAd  = new  GSFullscreenAd(activity.getApplicationContext() ,ration.key); 
			  GSMobileBannerAdView banner=new GSMobileBannerAdView(activity.getApplicationContext());
		    banner.addListener(this);
		    banner.refresh();
		}
		catch (Exception e) {               
			adViewLayout.rotatePriAd();                       
		}
		
	}

 


	@Override
	public void onAdClickthrough(GSAd arg0) {
		
	}

	@Override
	public void onAdDismissal(GSAd arg0) {
	 
		
	}



	@Override
	public void onFailedToFetchAd(GSAd arg0, GSAdErrorCode arg1) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Greystripe fail");
		arg0.removeListener(this);
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	    	return;
	    }
	    adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
	}



	@Override
	public void onFetchedAd(GSAd arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
		Log.d(AdViewUtil.ADVIEW, "Greystripe success");
		arg0.removeListener(this);
		
	    AdViewLayout adViewLayout = adViewLayoutReference.get();
	    if (adViewLayout == null) {
	      return;
	    }
	    adViewLayout.adViewManager.resetRollover();
//	    adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
	    adViewLayout.rotateThreadedDelayed();
		
	}



	@Override
	public void addListener(GSAdListener arg0) {
		 
		
	}



	@Override
	public boolean isAdReady() {
		 
		return false;
	}



	@Override
	public void removeListener(GSAdListener arg0) {
		 
		
	}
	  public void fetchFullscreenClicked(View v) {
	        Log.d("log.d", "Fetch fullscreen clicked.");
	        if (!myFullscreenAd.isAdReady()) {
	            myFullscreenAd.fetch();
	        } else {
	        	  Log.d("log.d", "Ad ready!  Display it");
	            
	        }
	    }
	    
	    
	    public void displayFullscreenClicked(View v) {
	        Log.d("log.d", "Fullscreen display clicked.");
	        
	        if (!myFullscreenAd.isAdReady()) {
	           
	        } else {
	            myFullscreenAd.display();
	        }
	    }


}
