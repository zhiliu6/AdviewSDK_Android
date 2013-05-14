package com.kyview.adapters;

import android.app.Activity;
import android.util.Log;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
//import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.kyview.AdViewAdRegistry;
import com.mobisage.android.MobiSageAdBanner;
import com.mobisage.android.MobiSageAnimeType;
import com.mobisage.android.MobiSageEnviroment;
import com.mobisage.android.IMobiSageAdViewListener;
import android.widget.RelativeLayout;


public class MobiSageAdapter extends AdViewAdapter implements IMobiSageAdViewListener{
	private MobiSageAdBanner adv;
	
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_ADSAGE;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.mobisage.android.MobiSageAdBanner") != null) {
				registry.registerClass(networkType(), MobiSageAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public MobiSageAdapter() {
		
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into MobiSage");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}

		Activity activity = adViewLayout.activityReference.get();
		  if(activity == null) {
			  return;
		  }  	     

		adv = new MobiSageAdBanner(activity,ration.key,null,null);//MobiSageAdSize.Size_540X80
		adv.setAdRefreshInterval(MobiSageEnviroment.AdRefreshInterval.Ad_No_Refresh);//Ad_Refresh_15//Ad_No_Refresh
		adv.setAnimeType(MobiSageAnimeType.Anime_LeftToRight);
		adv.setMobiSageAdViewListener(this);
		//adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adv));
		adViewLayout.activeRation = adViewLayout.nextRation;
		adViewLayout.removeAllViews();
		adViewLayout.addView(adv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
		//adViewLayout.rotateThreadedDelayed();
	}

	public void onMobiSageAdViewShow(Object adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "onMobiSageAdViewShow");

		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adv.setMobiSageAdViewListener(null);
		
		adViewLayout.adViewManager.resetRollover();
		adViewLayout.rotateThreadedDelayed();
		  
		adViewLayout.reportImpression();
        }

	public void onMobiSageAdViewUpdate(Object adView) { 
		if(AdViewTargeting.getRunMode()==RunMode.TEST)	
			Log.d(AdViewUtil.ADVIEW, "onMobiSageAdViewUpdate");
        }
	
	public void onMobiSageAdViewHide(Object adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)		
			Log.d(AdViewUtil.ADVIEW, "onMobiSageAdViewHide");
       }

	public void onMobiSageAdViewError(Object adView) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)		
			Log.d(AdViewUtil.ADVIEW, "onMobiSageAdViewError");

		adv.setMobiSageAdViewListener(null);
		
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
		}

		adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();	
       }

	@Override
	public void onMobiSageAdViewClick(Object arg0) {
		 
		
	}


}
