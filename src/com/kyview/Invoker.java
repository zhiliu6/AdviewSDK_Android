package com.kyview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;


//import com.kyview.AdViewTargeting.Channel;
import com.kyview.AdViewTargeting.RunMode;
import com.kyview.AdViewTargeting.UpdateMode;






public class Invoker extends Activity implements AdViewInterface {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout_main);
        if (layout == null) 
            return; 
          
        AdViewTargeting.setUpdateMode(UpdateMode.EVERYTIME);
        AdViewTargeting.setRunMode(RunMode.TEST);
        AdViewLayout adViewLayout = new AdViewLayout(this, "SDK20112217380924nvvrv9w6cifnlq3");
        //AdViewLayout adViewLayout = new AdViewLayout(this, "SDK201116280212248a0qiillp884xpg");		
        adViewLayout.setAdViewInterface(this);      
        layout.addView(adViewLayout);
        //RelativeLayout.LayoutParams adViewLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //layout.addView(adViewLayout, adViewLayoutParams);
		
        layout.invalidate();
    }

	

	@Override
	public void onClickAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayAd() {
		// TODO Auto-generated method stub
		
	}
}
