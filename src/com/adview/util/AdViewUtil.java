

package com.adview.util;


import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;


public class AdViewUtil {
	public static final String urlConfig = "http://config.adview.cn/agent/agent1_android.php?appid=%s&appver=%d&client=0&simulator=%d&location=%s";
	public static  String urlImpression = "http://www.adview.cn/agent/agent2.php?appid=%s&nid=%s&type=%d&uuid=%s&country_code=%s&appver=%d&client=0&simulator=%d&keydev=%s";
	
	public static  String urlClick = "http://www.adview.cn/agent/agent3.php?appid=%s&nid=%s&type=%d&uuid=%s&country_code=%s&appver=%d&client=0&simulator=%d&keydev=%s";
	public static  String appReport = "http://www.adview.cn/agent/appReport.php?keyAdView=%s&keyDev=%s&typeDev=%s&osVer=%s&resolution=%s&servicePro=%s&netType=%s&channel=%s&platform=%s";
		
	// Don't change anything below this line
	/***********************************************/ 
	 
	public static final int VERSION = 180;

	public static final String ADVIEW = "AdView SDK v1.8.0";
	
	// Could be an enum, but this gives us a slight performance improvement
	//abroad
	public static final int NETWORK_TYPE_ADMOB = 1;
	public static final int NETWORK_TYPE_GREYSTRIP = 2;
	public static final int NETWORK_TYPE_INMOBI = 3;
	public static final int NETWORK_TYPE_MDOTM = 4;
	public static final int NETWORK_TYPE_ZESTADZ = 5;
	public static final int NETWORK_TYPE_MILLENNIAL = 6;
	public static final int NETWORK_TYPE_SMAATO = 7;
	
	//the below is china
	public static final int NETWORK_TYPE_WOOBOO=21;
	public static final int NETWORK_TYPE_YOUMI=22;
	public static final int NETWORK_TYPE_KUAIYOU=23;
	public static final int NETWORK_TYPE_CASEE=24;
	public static final int NETWORK_TYPE_WIYUN=25;
	public static final int NETWORK_TYPE_ADCHINA=26;
	public static final int NETWORK_TYPE_ADVIEWAD=28;
	public static final int NETWORK_TYPE_SMARTAD=29;
	public static final int NETWORK_TYPE_DOMOB=30;
	public static final int NETWORK_TYPE_VPON=31;
	public static final int NETWORK_TYPE_ADTOUCH=32;
	public static final int NETWORK_TYPE_ADWO=33;
	public static final int NETWORK_TYPE_AIRAD=34;
	public static final int NETWORK_TYPE_WQ=35;
	public static final int NETWORK_TYPE_APPMEDIA=36;
	public static final int NETWORK_TYPE_TINMOO=37;
	public static final int NETWORK_TYPE_BAIDU=38;
	public static final int NETWORK_TYPE_LSENSE=39;
//	public static final int NETWORK_TYPE_YINGGAO=40;
	public static final int NETWORK_TYPE_IZPTEC=41;
	public static final int NETWORK_TYPE_ADSAGE=42;
	public static final int NETWORK_TYPE_UMENG=43;
	public static final int NETWORK_TYPE_FRACTAL=44;
	public static final int NETWORK_TYPE_LMMOB=45;	
	public static final int NETWORK_TYPE_MOBWIN=46;
	public static final int NETWORK_TYPE_SUIZONG=47;	
	public static final int NETWORK_TYPE_ADUU=48;
	public static final int NETWORK_TYPE_MOMARK=49;
	
	public static final int NETWORK_TYPE_CUSTOMIZE=999;
	
	private static int[] widthAndHeight;
	
	public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;	
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }

	public static int convertToScreenPixels(int dipPixels, double density)
	{
		double pix=0;
		
	 	pix = density > 0.0D ? dipPixels * density : dipPixels;

		return (int)pix;
	}
	
	public static boolean checkPermission(Context context, String paramString)
	{
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(paramString, context.getPackageName()) == 0;
	}

	public static String getImsi(Context context)
	{
		TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
		StringBuffer ImsiStr = new StringBuffer();
		try
		{
			if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
				ImsiStr.append(tm.getSubscriberId() == null ? "" : tm.getSubscriberId());
			}
			while (ImsiStr.length() < 15)
				ImsiStr.append("0");
		}
		catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
			ImsiStr.append("000000000000000");
		}
		return ImsiStr.toString();
	}

	public static String getImei(Context context)
	{
		TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
		StringBuffer tmDevice = new StringBuffer();
		try
		{
			if (checkPermission(context, "android.permission.READ_PHONE_STATE")) {
				tmDevice.append(tm.getDeviceId());
			}
			while (tmDevice.length() < 15)
				tmDevice.append("0");
		}
		catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}
		
		return tmDevice.toString().replace("null", "0000");
	}

	public static String getIDByMAC(Context context)
	{
		String str = null;
		try {
			WifiManager localWifiManager = (WifiManager)context.getSystemService("wifi");
			WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
			str = localWifiInfo.getMacAddress();
		} catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}
		return str;
	}

	public static String getAduuNetworkType(Context context)
	{
		String networkType = "0";
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService("connectivity");
		NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
		
		if (networkinfo != null) {
			networkType = networkinfo.getTypeName();
			if (networkType.equalsIgnoreCase("wifi")) {
				networkType = "4";
			} else {
				String IMSI = getImsi(context);
				if ((IMSI.startsWith("46000")) || (IMSI.startsWith("46002")))
					networkType = "1";
				else if (IMSI.startsWith("46001"))
					networkType = "2";
				else if (IMSI.startsWith("46003"))
					networkType = "3";
			}
		}
		else {
			String IMSI = getImsi(context);
			if ((IMSI.startsWith("46000")) || (IMSI.startsWith("46002")))
			networkType = "1";
			else if (IMSI.startsWith("46001"))
			networkType = "2";
			else if (IMSI.startsWith("46003")) {
			networkType = "3";
			}
		}
		return networkType;
	}

	public static int[] getWidthAndHeight(Context context)
	{
		//private static int[] widthAndHeight;
		
		if ((widthAndHeight != null) && (widthAndHeight.length > 1)) {
			return widthAndHeight;
		}

		WindowManager WM = (WindowManager)context.getSystemService("window");
		int width = WM.getDefaultDisplay().getWidth();
		int height = WM.getDefaultDisplay().getHeight();
		if (height > width)
			widthAndHeight = new int[] { width, height };
		else {
			widthAndHeight = new int[] { height, width };
		}
		return widthAndHeight;
	}

}
