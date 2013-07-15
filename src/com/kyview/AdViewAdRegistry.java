package com.kyview;

import android.util.SparseArray;

import com.kyview.adapters.AdMobAdapter;
import com.kyview.adapters.AdViewAdapter;
import com.kyview.adapters.DomobAdapter;
import com.kyview.adapters.DoubleClickAdapter;
import com.kyview.adapters.EventAdapter;
import com.kyview.adapters.InmobiAdapter;


public class AdViewAdRegistry { 
	
	private static AdViewAdRegistry mInstance = null;
	  
//	private HashMap<Integer, Class<? extends AdViewAdapter>> mAdapterMap = null;
	private SparseArray<Class<? extends AdViewAdapter>> sparseArray=null;
	private AdViewAdRegistry() {
		sparseArray=new SparseArray<Class<? extends AdViewAdapter>>();
//		mAdapterMap = new HashMap<Integer, Class<? extends AdViewAdapter>>();
	} 
	
	public static AdViewAdRegistry getInstance() {
		if (null == mInstance) {
			mInstance = new AdViewAdRegistry();
			
			mInstance.loadAdapters();
		}
		
		return mInstance;
	}
	
	private void loadAdapters() {
//		try {AdViewHouseAdapter.load(this);}catch(Error e){}
//		try {AdBaiduAdapter.load(this);}catch(Error e){}
//		try {AdChinaAdapter.load(this);}catch(Error e){}
//		try {AdlantisAdapter.load(this);}catch(Error e){}
		try {AdMobAdapter.load(this);}catch(Error e){}
//		try {AdTouchAdapter.load(this);}catch(Error e){}
		//try {AduuInterfaceAdapter.load(this);}catch(Error e){}
//		try {AduuAdapter.load(this);}catch(Error e){}
//		try {AdwoAdapter.load(this);}catch(Error e){}		
//		try {AirAdAdapter.load(this);}catch(Error e){}
//		try {AppMediaAdapter.load(this);}catch(Error e){}
		try {DomobAdapter.load(this);}catch(Error e){}
		try {DoubleClickAdapter.load(this);}catch(Error e){}
		try {EventAdapter.load(this);}catch(Error e){}
//		try {FractalAdapter.load(this);}catch(Error e){}
//		try {GreystripeAdapter.load(this);}catch(Error e){}
		try {InmobiAdapter.load(this);}catch(Error e){}
//		try {IzpAdapter.load(this);}catch(Error e){}
//		try {LmMobAdapter.load(this);}catch(Error e){}
//		try {LsenseAdapter.load(this);}catch(Error e){}
//		try {MdotMAdapter.load(this);}catch(Error e){}			
//		try {MillennialAdapter.load(this);}catch(Error e){}
//		try {MobiSageAdapter.load(this);}catch(Error e){}
//		try {MobWinAdapter.load(this);}catch(Error e){}
//		try {MomarkAdapter.load(this);}catch(Error e){}
//		try {SmaatoAdapter.load(this);}catch(Error e){}
//		try {SmartAdAdapter.load(this);}catch(Error e){}	
//		try {SuizongInterfaceAdapter.load(this);}catch(Error e){}
//		try {TinmooAdapter.load(this);}catch(Error e){}
//		try {UmengAdapter.load(this);}catch(Error e){}
//		try {VponAdapter.load(this);}catch(Error e){}
//		try {WiyunAdapter.load(this);}catch(Error e){}
//		try {WoobooAdapter.load(this);}catch(Error e){}
//		try {WqAdapter.load(this);}catch(Error e){}
//		try {YoumiAdapter.load(this);}catch(Error e){}
//		try {ZestADZAdapter.load(this);}catch(Error e){}	
//		try {YunYunAdapter.load(this);}catch(Error e){}	
	}
	
	public void registerClass(Integer adType, Class<? extends AdViewAdapter> adapterClass) {
//		mAdapterMap.put(Integer.valueOf(adType), adapterClass);
		sparseArray.put(adType, adapterClass);
		
	}
	
	public Class<? extends AdViewAdapter> adapterClassForAdType(Integer adType) {
		return sparseArray.get(adType);
//		return mAdapterMap.get(Integer.valueOf(adType));
	}
	
}
