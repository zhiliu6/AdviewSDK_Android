

package com.kyview.adapters;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;

import com.kyview.AdViewAdRegistry;
import com.kyview.AdViewLayout;
import com.kyview.AdViewTargeting;

import com.kyview.AdViewTargeting.RunMode;
import com.kyview.obj.Ration;
import com.kyview.util.AdViewUtil;

import java.util.Timer;
import java.util.TimerTask;


public abstract class AdViewAdapter {
	protected WeakReference<AdViewLayout> adViewLayoutReference;
	protected Ration ration;
	static AdViewAdapter adapter;
	private Timer reqTimeListenerTimer;
	
	public AdViewAdapter() {
		
	}
	
	public AdViewAdapter(AdViewLayout adViewLayout, Ration ration) {
		this.setParamters(adViewLayout, ration);
	}
	
	public void setParamters(AdViewLayout adViewLayout, Ration ration) {
		this.adViewLayoutReference = new WeakReference<AdViewLayout>(adViewLayout);
		this.ration = ration;
	}
	
	private static AdViewAdapter getAdapter(AdViewLayout adViewLayout,Ration ration) {	
		
		//new codes of new logic find adapter.
		Class<? extends AdViewAdapter> adapterClass = AdViewAdRegistry.getInstance().adapterClassForAdType(ration.type);
		
		if (null != adapterClass) {
			return getNetworkAdapter(adapterClass, adViewLayout, ration);
		}
		
		return unknownAdNetwork(adViewLayout, ration);
		
		//new codes end.
		
		/*

		try {
			switch(ration.type) {
				case AdViewUtil.NETWORK_TYPE_ADMOB:
					if(Class.forName("com.google.ads.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdMobAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}  
				
				case AdViewUtil.NETWORK_TYPE_ADLANTIS:
					 if(Class.forName("jp.adlantis.android.AdlantisView") != null) {
							return getNetworkAdapter("com.kyview.adapters.AdlantisAdapter", adViewLayout, ration);
						}
						else  
						{
							return unknownAdNetwork(adViewLayout, ration);
						}
				case AdViewUtil.NETWORK_TYPE_DoubleClick:
					if(Class.forName("com.google.ads.AdView")!=null ){
						return getNetworkAdapter("com.kyview.adapters.DoubleClickAdapter",adViewLayout,ration);
					}else{
						return unknownAdNetwork(adViewLayout,ration);
					}	
				case AdViewUtil.NETWORK_TYPE_MILLENNIAL:
					if(Class.forName("com.millennialmedia.android.MMAdView") != null) {
                        return getNetworkAdapter("com.kyview.adapters.MillennialAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				
				case AdViewUtil.NETWORK_TYPE_WOOBOO:
					if(Class.forName("com.wooboo.adlib_android.WoobooAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.WoobooAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_YOUMI:
					if(Class.forName("net.youmi.android.banner.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.YoumiAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
			
				case AdViewUtil.NETWORK_TYPE_CASEE:
					//if(Class.forName("cn.casee.adsdk.CaseeAdView") != null) {
					//	return getNetworkAdapter("com.kyview.adapters.CaseeAdapter", adViewLayout, ration);
					//}
					//else 
					{
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_WIYUN:
					if(Class.forName("com.wiyun.ad.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.WiyunAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_ADCHINA:
					if(Class.forName("com.adchina.android.ads.views.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdChinaAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_ADVIEWAD:
					if(Class.forName("com.kyview.ad.KyAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdViewHouseAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_DOMOB:
					if(Class.forName("cn.domob.android.ads.DomobAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.DomobAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_SMARTAD:
					if(Class.forName("cn.smartmad.ads.android.SMAdBannerView") != null) {
						return getNetworkAdapter("com.kyview.adapters.SmartAdAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_VPON:
					if(Class.forName("com.vpon.adon.android.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.VponAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_ADTOUCH:
					if(Class.forName("com.energysource.szj.embeded.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdTouchAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_ADWO:
					if(Class.forName("com.adwo.adsdk.AdwoAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdwoAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_AIRAD:
					if(Class.forName("com.mt.airad.AirAD") != null) {
						return getNetworkAdapter("com.kyview.adapters.AirAdAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_WQ:
					if(Class.forName("com.wqmobile.sdk.WQAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.WqAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_APPMEDIA:
					if(Class.forName("cn.appmedia.ad.BannerAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AppMediaAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_TINMOO:
					if(Class.forName("com.ignitevision.android.ads.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.TinmooAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_BAIDU:
					if(Class.forName("com.baidu.mobads.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.AdBaiduAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_LSENSE:
					if(Class.forName("com.l.adlib_android.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.LsenseAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				//case AdViewUtil.NETWORK_TYPE_YINGGAO:
				//	if(Class.forName("com.winad.android.ads.AdView") != null) {
				//		return getNetworkAdapter("com.kyview.adapters.WinAdAdapter", adViewLayout, ration);
				//	}
				//	else {
				//		return unknownAdNetwork(adViewLayout, ration);
				//	}
				case AdViewUtil.NETWORK_TYPE_ZESTADZ:
					if(Class.forName("com.zestadz.android.ZestADZAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.ZestADZAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_SMAATO:
					if(Class.forName("com.smaato.SOMA.SOMABanner") != null) {
						return getNetworkAdapter("com.kyview.adapters.SmaatoAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_GREYSTRIP:
					if(Class.forName("com.greystripe.sdk.GSMobileBannerAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.GreystripeAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_MDOTM:
					if(Class.forName("com.mdotm.android.ads.MdotMView") != null) {
						return getNetworkAdapter("com.kyview.adapters.MdotMAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}		
				case AdViewUtil.NETWORK_TYPE_INMOBI:
					if(Class.forName("com.inmobi.androidsdk.IMAdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.InmobiAdapter", adViewLayout, ration);
					}	
					//if(Class.forName("com.kyview.adapters.InmobiInterfaceAdapter") != null) {
					//	return getNetworkAdapter("com.kyview.adapters.InmobiInterfaceAdapter", adViewLayout, ration);
					//}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}	
				case AdViewUtil.NETWORK_TYPE_ADSAGE:
					if(Class.forName("com.mobisage.android.MobiSageAdBanner") != null) {
						return getNetworkAdapter("com.kyview.adapters.MobiSageAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_IZPTEC:
					if(Class.forName("com.izp.views.IZPView") != null) {
						return getNetworkAdapter("com.kyview.adapters.IzpAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}	
				case AdViewUtil.NETWORK_TYPE_UMENG:
					if(Class.forName("com.umengAd.android.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.UmengAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}	
				case AdViewUtil.NETWORK_TYPE_FRACTAL:
					if(Class.forName("com.fractalist.android.ads.ADView") != null) {
						return getNetworkAdapter("com.kyview.adapters.FractalAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_LMMOB:
					if(Class.forName("cn.immob.sdk.ImmobView") != null) {
						return getNetworkAdapter("com.kyview.adapters.LmMobAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}	
				case AdViewUtil.NETWORK_TYPE_MOBWIN:
					if(Class.forName("com.tencent.exmobwin.MobWINManager") != null) {
                        			return getNetworkAdapter("com.kyview.adapters.MobWinAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_SUIZONG:
					//if(Class.forName("com.suizong.mobplate.ads.AdView") != null) {
					if(Class.forName("com.kyview.adapters.SuizongInterfaceAdapter") != null) {	
						return getNetworkAdapter("com.kyview.adapters.SuizongInterfaceAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_ADUU:
					if(Class.forName("com.kyview.adapters.AduuInterfaceAdapter") != null) {
						return getNetworkAdapter("com.kyview.adapters.AduuInterfaceAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_MOMARK:
					if(Class.forName("com.donson.momark.view.view.AdView") != null) {
						return getNetworkAdapter("com.kyview.adapters.MomarkAdapter", adViewLayout, ration);
					}
					else {
						return unknownAdNetwork(adViewLayout, ration);
					}
				case AdViewUtil.NETWORK_TYPE_CUSTOMIZE:
					//here, developer may add self-code
					return getNetworkAdapter("com.kyview.adapters.EventAdapter", adViewLayout, ration);
					
				default:
					return unknownAdNetwork(adViewLayout, ration);
			}
		}
		catch(ClassNotFoundException e) {
			return unknownAdNetwork(adViewLayout, ration);
		}
		catch(VerifyError e) {
			if(AdViewTargeting.getRunMode()==RunMode.TEST)
			  Log.e("AdView", "YYY - Caught VerifyError", e);
          return unknownAdNetwork(adViewLayout, ration);
		}
		*/
	}
	
	private static AdViewAdapter getNetworkAdapter(Class<? extends AdViewAdapter> adapterClass, AdViewLayout adViewLayout, Ration ration) {
		AdViewAdapter adViewAdapter = null;
		try {
			/*
			Class<?>[] parameterTypes = new Class[2];
			parameterTypes[0] = AdViewLayout.class;
			parameterTypes[1] = Ration.class;

			Constructor<? extends AdViewAdapter> constructor = adapterClass.getConstructor(parameterTypes);

			Object[] args = new Object[2];
			args[0] = adViewLayout;
			args[1] = ration;

			adViewAdapter = constructor.newInstance(args);*/
			Constructor<? extends AdViewAdapter> constructor = adapterClass.getConstructor();
			adViewAdapter = constructor.newInstance();
			adViewAdapter.setParamters(adViewLayout, ration);
			adViewAdapter.initAdapter(adViewLayout, ration);
		}
		catch(SecurityException e) {}
		catch(NoSuchMethodException e) {}
		catch(InvocationTargetException e) {}
		catch(IllegalAccessException e) {}
		catch(InstantiationException e) {}

		return adViewAdapter;
	}

	/*
	private static AdViewAdapter getNetworkAdapter(String networkAdapter, AdViewLayout adViewLayout, Ration ration) {
		AdViewAdapter adViewAdapter = null;

		try {
			@SuppressWarnings("unchecked")
			Class<? extends AdViewAdapter> adapterClass = (Class<? extends AdViewAdapter>) Class.forName(networkAdapter);
			adViewAdapter = getNetworkAdapter(adapterClass, adViewLayout, ration);
		}
		catch(ClassNotFoundException e) {}

		return adViewAdapter;
	}
*/	
	private static AdViewAdapter unknownAdNetwork(AdViewLayout adViewLayout, Ration ration) {
		if(AdViewTargeting.getRunMode()==RunMode.TEST)
			Log.w(AdViewUtil.ADVIEW, "Unsupported ration type: " + ration.type);
		return null;
	}
	
	public static void handleOne(AdViewLayout adViewLayout,Ration ration)  {
      adapter = AdViewAdapter.getAdapter(adViewLayout, ration);
      if(adapter != null) {
    	  if(AdViewTargeting.getRunMode()==RunMode.TEST)
    		  Log.d(AdViewUtil.ADVIEW, "Valid adapter, calling handle()");
         adapter.handle();
      }
      else {
    	adViewLayout.adViewManager.resetRollover();
    	adViewLayout.rotateThreadedNow();
       
        
      }
	}
	public static void onRelease(){
		if(null!=adapter)
		adapter.release(adapter.adViewLayoutReference.get());
	}
	public static void onClickAd() throws Throwable {
		if (adapter != null)
			adapter.click();
		else
			throw new Exception("On Click failed");
	}

	public void startTimer()
	{
		if (this.reqTimeListenerTimer != null) {
			this.reqTimeListenerTimer.cancel();
			this.reqTimeListenerTimer = null;
		}

		this.reqTimeListenerTimer = new Timer();

		this.reqTimeListenerTimer.schedule(new TimerTask()
			{
				public void run()
				{
					AdViewAdapter.this.requestTimeOut();
				}
			}
			, 20000L);
	}

	public void shoutdownTimer()
	{
		if (this.reqTimeListenerTimer != null) {
			this.reqTimeListenerTimer.cancel();
			this.reqTimeListenerTimer = null;
		}
	}

	public void requestTimeOut()
	{
	}


	public abstract void handle();
	public void release(AdViewLayout adViewLayout) {
	}
	public abstract void initAdapter(AdViewLayout adViewLayout, Ration ration);
	
	public void click()
	{
		//Log.d(AdViewUtil.ADVIEW, "adapter, click");
	}
	//public abstract void finish();
  }
