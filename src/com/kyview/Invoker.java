package com.kyview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.kyview.AdViewTargeting.RunMode;
import com.kyview.AdViewTargeting.UpdateMode;
//import com.kyview.AdViewTargeting.Channel;






public class Invoker extends Activity implements AdViewInterface {
	AdViewLayout adViewLayout;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout_main);

        if (layout == null) 
            return; 
          
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
//        AdViewTargeting.setRunMode(RunMode.TEST);
        adViewLayout = new AdViewLayout(this, "SDK20131513030520qwgmx5x6av08xym");		
        adViewLayout.setAdViewInterface(this);     
        layout.addView(adViewLayout);
        layout.invalidate();

    }

	

	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}

	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}
	
//	public void amazon_proc() {
//		// TODO Auto-generated method stub
//		Log.d("AdViewSample", "Into zmazon");
//
//		//娴嬭瘯妯″紡
//        AdRegistration.enableLogging(this, true);
//        AdRegistration.enableTesting(this, true);
//        AdRegistration.setAppKey(this, "sample-app-v1_pub-2");
//        
//		//鍒涘缓amazon鐨刟dview瀹炰緥	
//        AdLayout adView = new AdLayout(this, AdSize.AD_SIZE_320x50);
//		//鎸囧畾渚﹀惉鎺ュ彛
//		adView.setListener(new AdListener() {
//			
//			 @Override
//			    public void onAdFailedToLoad(AdLayout view, AdError error) {
//			        Log.w("AdViewSample", "Ad failed to load. Code: " + error.getResponseCode() + ", Message: " + error.getResponseMessage());
//
//					//澶辫触涔嬪悗寮�璇锋眰涓嬩竴涓箍鍛�//					adViewLayout.adViewManager.resetRollover_pri();
//					adViewLayout.rotateThreadedPri();	        
//			    }
//
//			    @Override
//			    public void onAdLoaded(AdLayout view, AdProperties adProperties) {
//			        Log.d("AdViewSample", adProperties.getAdType().toString() + " Ad loaded successfully.");
//
//					//鍙栨秷渚﹀惉鎺ュ彛
//			        view.setListener(null);
//
//					//骞垮憡璇锋眰鎴愬姛涔嬪悗锛屽惎鍔ㄥ畾鏃跺櫒锛屽埌鏃跺悗璇锋眰涓嬩竴涓箍鍛�//					adViewLayout.reportImpression();
//					adViewLayout.adViewManager.resetRollover();
//					adViewLayout.rotateThreadedDelayed();	        
//			    }
//
//			
//			@Override
//			public void onAdExpanded(AdLayout arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAdCollapsed(AdLayout arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//
//		//娓呴櫎褰撳墠鐨剉iew
//		adViewLayout.removeAllViews();
//		
//		RelativeLayout.LayoutParams layoutParams;
//		layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//		//娣诲姞amazon鐨剉iew鍒板綋鍓嶇殑adlayout涓�//		adViewLayout.addView(adView, layoutParams);
//		
//		AdTargetingOptions adOptions = new AdTargetingOptions();
//        adView.loadAd(adOptions);
//	}

	
	
}
