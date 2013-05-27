package com.kyview;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kyview.AdViewTargeting.RunMode;
import com.kyview.AdViewTargeting.UpdateMode;
//import com.kyview.AdViewTargeting.Channel;






public class Invoker extends Activity implements AdViewInterface ,OnClickListener{
	public static AdViewLayout adViewLayout;
	public static LinearLayout layout;
	public static String sdkKey="SDK201310111003303e4rx5msd7cn1pa";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        layout = (LinearLayout)findViewById(R.id.layout_main);
        Button ceshi=(Button) findViewById(R.id.ceshi_btn);
        Button normal=(Button) findViewById(R.id.normal_btn);
        ceshi.setOnClickListener(this);
        normal.setOnClickListener(this);
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
        AdViewTargeting.setRunMode(RunMode.TEST);

    }

	

	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.normal_btn:

	        if (layout == null) 
	            return; 
			AdViewLayout.isTest=false;
	        adViewLayout = new AdViewLayout(this, sdkKey);   		
			break;
		case R.id.ceshi_btn:
			AdViewLayout.isTest=true;		
	        adViewLayout = new AdViewLayout(this, sdkKey);		
			Intent intent=new Intent();
			intent.setClass(this, TestModeActivity.class);
			startActivity(intent);
			break;
		}
        adViewLayout.setAdViewInterface(this); 
        layout.removeAllViews();
        layout.addView(adViewLayout);
        layout.invalidate();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("onDestroy", "onDestroy");
//		adViewLayout.release();
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
