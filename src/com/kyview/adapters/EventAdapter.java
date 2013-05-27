
package com.kyview.adapters;

import java.lang.reflect.Method;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewInterface;
import com.kyview.AdViewLayout;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;
	
public class EventAdapter extends AdViewAdapter {

	private static int networkType() {
		return AdViewUtil.NETWORK_TYPE_CUSTOMIZE;
	}
	
	public static void load(AdViewAdRegistry registry) {
		registry.registerClass(networkType(), EventAdapter.class);
	}

	public EventAdapter() {
	}
	
	@Override
	public void initAdapter(AdViewLayout adViewLayout, Ration ration) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle() {
		AdViewUtil.logInfo("Event notification request initiated");
	 	 AdViewLayout adViewLayout = adViewLayoutReference.get();
	 	 if(adViewLayout == null) {
	 		 return;
	 	 }
	 	 
		//If the user set a handler for notifications, call it
		if(adViewLayout.adViewInterface != null) {
			String key = this.ration.key;
			if(key.length()<1) {
				AdViewUtil.logInfo("Event key is null");
				adViewLayout.rollover();
				return;
			}
			
			String method = key;
			
			Class<? extends AdViewInterface> listenerClass = adViewLayout.adViewInterface.getClass();
			Method listenerMethod;
			try {
				listenerMethod = listenerClass.getMethod(method, (Class[])null);
				listenerMethod.invoke(adViewLayout.adViewInterface, (Object[])null);
			} catch (Exception e) { 
				AdViewUtil.logError("Caught exception in handle()", e);
				//adViewLayout.rollover();
				adViewLayout.rotateThreadedPri();
				return;
			}
		}
		else {
			AdViewUtil.logInfo("Event notification would be sent, but no interface is listening");
			adViewLayout.rollover();
			return;
		}
	}


}
