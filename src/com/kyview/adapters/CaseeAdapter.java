package com.kyview.adapters;

import android.graphics.Color;
import android.util.Log;

import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewLayout.ViewAdRunnable;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Extra;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import cn.casee.adsdk.CaseeAdView;
import cn.casee.adsdk.CaseeAdView.AdListener;

public class CaseeAdapter extends AdViewAdapter implements AdListener
{

	public CaseeAdapter(AdViewLayout adViewLayout, Ration ration) {
		super(adViewLayout, ration);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, "Into CASEE");
	 	AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		return;
	 	}
	 	 
	 	Extra extra = adViewLayout.extra;
	    int bgColor = Color.rgb(extra.bgRed, extra.bgGreen, extra.bgBlue);
	    int fgColor = Color.rgb(extra.fgRed, extra.fgGreen, extra.fgBlue);
	    CaseeAdView adView=null;
	    if(AdViewTargeting.getRunMode()==RunMode.TEST)
	    	adView=new CaseeAdView(adViewLayout.getContext(),"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",true,(extra.cycleTime)*1000,bgColor, fgColor,false);    
	    else if(AdViewTargeting.getRunMode()==RunMode.NORMAL)
	    	adView=new CaseeAdView(adViewLayout.getContext(),ration.key,false,(extra.cycleTime)*1000,bgColor, fgColor,false);    
	    else{
	    	adView=new CaseeAdView(adViewLayout.getContext(),ration.key,false,(extra.cycleTime)*1000,bgColor, fgColor,false);    
	    }
	    adView.setListener(this);
		
	}

	@Override
	public void onFailedToReceiveAd(CaseeAdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "CASEE fail");
		arg0.setListener(null);

	    AdViewLayout adViewLayout = adViewLayoutReference.get();

	    if (adViewLayout == null) {
	      return;
	    }

	    adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onFailedToReceiveRefreshAd(CaseeAdView arg0) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.d(AdViewUtil.ADVIEW, "success=>fail for fresh");
		arg0.setListener(null);

	    AdViewLayout adViewLayout = adViewLayoutReference.get();

	    if (adViewLayout == null) {
	      return;
	    }

	    adViewLayout.adViewManager.resetRollover_pri();
		  adViewLayout.rotateThreadedPri();
		
	}

	@Override
	public void onReceiveAd(CaseeAdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, " CASEE success");
		 AdViewLayout adViewLayout = adViewLayoutReference.get();
		arg0.setListener(null);
		
		    if (adViewLayout == null) {
		      return;
		    }
		    adViewLayout.adViewManager.resetRollover();
		 	adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
			adViewLayout.rotateThreadedDelayed();
	}

	@Override
	public void onReceiveRefreshAd(CaseeAdView arg0) {
		// TODO Auto-generated method stub
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.d(AdViewUtil.ADVIEW, " fail=>success for fresh");
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		arg0.setListener(null);
		
	    if (adViewLayout == null) {
	      return;
	    }
	    adViewLayout.adViewManager.resetRollover();
	 	adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, arg0));
		adViewLayout.rotateThreadedDelayed();
		
	}

}
