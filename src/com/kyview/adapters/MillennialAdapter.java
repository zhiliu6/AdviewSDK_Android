

package com.kyview.adapters;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;
import com.kyview.AdViewTargeting.Gender;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
import com.millennialmedia.android.MMAd;
import com.millennialmedia.android.MMAdView;
import com.millennialmedia.android.MMException;
import com.millennialmedia.android.MMRequest;
import com.millennialmedia.android.MMSDK;
import com.millennialmedia.android.RequestListener;

public class MillennialAdapter extends AdViewAdapter implements RequestListener {
	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_MILLENNIAL;
	}
	
	public static void load(AdViewAdRegistry registry) {
		try {
			if(Class.forName("com.millennialmedia.android.MMAdView") != null) {
				registry.registerClass(networkType(), MillennialAdapter.class);
			}
		} catch (ClassNotFoundException e) {}
	}

	public MillennialAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void handle() {
		AdViewLayout adViewLayout = adViewLayoutReference.get();
		if(adViewLayout == null) {
			return;
	 	}
		Activity activity = adViewLayout.activityReference.get();
		
	 	Hashtable<String, String> map = new Hashtable<String, String>();	
	 	
	 	map.putAll(getAdSize(activity));
	 	final AdViewTargeting.Gender gender = AdViewTargeting.getGender();
	    if (gender == Gender.MALE) {
	      map.put("gender", "male");
	    } else if (gender == Gender.FEMALE) {
	      map.put("gender", "female");
	    }
	    final int age = AdViewTargeting.getAge();
	    if (age != -1) {
	      map.put("age", String.valueOf(age));
	    }
	    final String postalCode = AdViewTargeting.getPostalCode();
	    if (!TextUtils.isEmpty(postalCode)) {
	      map.put("zip", postalCode);
	    }
	    final String keywords = AdViewTargeting.getKeywordSet() != null ? TextUtils
	            .join(",", AdViewTargeting.getKeywordSet())
	            : AdViewTargeting.getKeywords();
	        if (!TextUtils.isEmpty(keywords)) {
	          map.put("keywords", keywords);
	    }
	    map.put("vendor", "adwhirl");
	    
        // Instantiate an ad view and add it to the view
	    MMAdView adView = new MMAdView(activity);
	    adView.setApid(ration.key);
	    adView.setId(MMSDK.getDefaultAdId());
		MMRequest request = new MMRequest();
		request.setMetaValues(map);
		adView.setMMRequest(request);
	    adView.setListener(this);
	    adView.getAd();
	    adViewLayout.AddSubView(adView);

	    adView.setHorizontalScrollBarEnabled(false);
	    adView.setVerticalScrollBarEnabled(false);


	}
	static boolean isTablet(Context context)
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return (metrics.heightPixels >= 728 && metrics.widthPixels >= 728);
	}
	static Map<String, String> getAdSize(Context context)
	{
		Map<String, String> metaData = new HashMap<String, String>();
		if(isTablet(context))
		{
			metaData.put("width", "728");
			metaData.put("height", "90");
		}
		else
		{
			metaData.put("width", "480");
			metaData.put("height", "60");
		}
		return metaData;
	}

	

	@Override
	public void MMAdOverlayLaunched(MMAd arg0) {
		// TODO Auto-generated method stub
		AdViewUtil.logInfo("Millennial Ad Overlay Launched");
	}

	@Override
	public void MMAdRequestIsCaching(MMAd arg0) {
		AdViewUtil.logInfo("MMAdRequestIsCaching");
	}

	@Override
	public void requestCompleted(MMAd arg0) {
		AdViewUtil.logInfo("Millennial success");
		arg0.setListener(null);

	 	AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		return;
	 	}
		adViewLayout.reportImpression();
 		adViewLayout.adViewManager.resetRollover();
// 		adViewLayout.handler.post(new ViewAdRunnable(adViewLayout, adView));
		adViewLayout.rotateThreadedDelayed();

	}

	@Override
	public void requestFailed(MMAd arg0, MMException arg1) {
		AdViewUtil.logInfo("Millennial failure");
		arg0.setListener(null);
		AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	if(adViewLayout == null) {
	 		return;
	 	}
	 	 
	 	adViewLayout.adViewManager.resetRollover_pri();
		adViewLayout.rotateThreadedPri();
		
	}
	
}
