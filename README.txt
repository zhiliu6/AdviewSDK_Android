欢迎使用AdView Android SDK，开发者可以在自己的程序中嵌入AdView，实现众多广告公司播放的聚合，优化！如果你愿意，还有激动人心的应用互推功能！

注意：
1.集成友盟(umeng)广告的时候，需要把umeng_res目录下的资源文件也放到项目中；

2.支持本地默认缓存功能，从adview后台点击“离线配置下载”下载配置文件，保存在<assets>文件夹中就可以了，确保任何情况下都能展示广告；

3.Adview SDK支持的android最小版本升级到1.6;

4.支持帷千,亿赞普的时候，需要把<uses-sdk android:minSdkVersion="4"/>给删除掉，否则广告显示的会缩小；如果去掉<uses-sdk android:minSdkVersion="4"/>的话，对于高分辨率的手机后台统计的就不是准了；

5.腾讯聚赢平台，包括Tencent_MobWIN_BASIC_1.4.jar,Tencent_MobWIN_SDK_1.4.jar, 为了更好的支持lbs可以加上<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>；

6.admob升级到4.3.1版本以后，需要android 3.2以上才能编译通过；AndroidManifest.xml中需要配置android:configChanges="keyboard|keyboardHidden|orientation|screenLayout|uiMode|screenSize|smallestScreenSize"/>, sample中这部分注释掉了，需要支持的时候打开; android:configChanges="orientation|keyboard|keyboardHidden"是为了在低版本编译用的，到时去掉；

7.如果你是一个新手，对广告平台不怎么了解，也不知道选择哪家广告平台，或者不知道哪家广告平台收入稳定，没关系，Adview建议你可以先使用百度，亿动智道，Inmobi几家平台看看效果。

8.在sample的Manifest文件中把GreyStripe广告的声明给注释掉了，需要的开发者自己打开；

9.微云平台，包括WiAd.jar,WiCommon.jar, 需要把wiyun_res目录下的资源文件也放到项目中；

10.从1.8.0以后，必须在AndroidManifest.xml文件中声明com.adview.DownloadService，互推和内嵌广告下载的时候要用；

11.开发者可以设置渠道，在Manifest文件中<meta-data android:name="AdView_CHANNEL" android:value="GFAN"></meta-data> (AdViewTargeting.setChannel接口已经作废); 目前支持的渠道包括下面列出的，移动广告观察(http://t.adview.cn/)中有各个市场的链接：
EOE(优亿市场)
GOOGLEMARKET(谷歌电子市场)
APPCHINA(应用汇)
HIAPK(安卓市场)
GFAN(机锋)
GOAPK(安智)
NDUOA(N多网)
91Store(手机91)
LIQUCN(历趣)
WAPTW(天网)
ANDROIDCN(安卓中国)
GGDWON(G友网)
ANDROIDAI(安卓之家)
STARANDROID(安卓星空)
ANDROIDD(安致)
YINGYONGSO(应用搜)
IMOBILE(手机之家)
SOUAPP(搜应用)
MUMAYI(木蚂蚁)
MOBIOMNI(欧米)
PAOJIAO(泡椒网)
AIBALA(爱扒拉市场)
COOLAPK(酷安网)
ANFONE(安丰)
APKOK(乐致网)

如果不配置，或配置其他的值，一律作为"OTHER"处理；



当前AdView更新版本:1.8.3
目录结构介绍：
libs：包括AdView的SDK和各个广告公司的SDK,其中：
	库名					广告平台				版本
	AdViewSDK_Android.jar:			AdView的SDK				1.8.3
	adchina_android_sdk.jar:		易传媒广告公司的SDK			2.6.2
	adlib_android.jar:			哇棒广告公司的SDK			2.3.0	
	adOn-3.2.5.jar:				Vpon广告公司的SDK			3.2.5
	adtouch-embed-sdk-1.1.0.jar: 		AdTouch公司的SDK				1.1.0
	Adwo_Android_SDK3.1.jar:  		AdWo广告公司的SDK			3.1	
	ad-fractalist-sdk-android.jar:		飞云广告公司的SDK			2.0
	airAD_sdk.jar: 				AirAD广告公司的SDK			1.3.3
	Baidu_MobAds_SDK_Agg_3.0.jar: 		百度移动联盟的sdk			3.0
	AppMediaAdAndroidSdk-1.1.0.jar: 	AppMedia广告公司的sdk			1.1.0
	casee-ad-sdk-3.0.jar:			架势无线广告公司的SDK			3.0
	domob_android_sdk.jar:			多盟广告公司的SDK			3.1.4
	GoogleAdMobAdsSdk-6.2.1.jar:		AdMOB广告公司的SDK			6.2.1	
	gssdk_1.6.1.jar:			GreyStripe广告公司的SDK			1.6.1
	IZPView.jar:				易赞普广告公司的SDK			1.0.3
	l_android_adsdk.jar:			百分通联的SDK				2.1.0.0
	immobSDK.jar:				力美广告公司的SDK			2.2
	mdotm-sdk-android.jar:			MdotM广告公司的SDK			3.0.0
	MMAdView.jar:				MillennialMedia 广告公司的SDK		4.6.0
	mobisageSDK.jar:			艾德斯奇广告公司的SDK			2.3.0
	momark_android_sdk2012_v1.2.0.jar:	Momark广告公司的SDK			1.2.0
	smartmad-sdk-android.jar:		亿动智道广告公司的SDK			2.0.2
	SOMAAndroidSDK2.5.4.jar:		SOMA广告公司的SDK			2.5.4

	Tencent_MobWIN_BASIC_1.4.jar:		腾讯广告公司的SDK			1.4
	Tencent_MobWIN_SDK_1.4.jar:		腾讯广告公司的SDK			1.4

	tinmoo-android-sdk_v3.1.0.jar:		天幕(Tinmoo)广告公司的sdk		2.0.2
	umeng_ad_android_sdk_v1.4.4.jar:	友盟广告公司的SDK			1.4.4

	WiAd.jar:				微云广告公司的SDK			4.0.0
	WiCommon.jar:				微云广告公司的SDK			4.0.0
	
	WQAndroidSDK_2.0.2.jar: 		惟千广告公司sdk				2.0.2
	youmi-android.jar:			有米广告公司的SDK			3.08
	zestadz_sdk_androidv1.2.jar:		ZestADZ广告公司的SDK			1.2
	
注：随踪,优友传媒(aduu)和Inmobi不需要单独的jar，都集成在AdViewSDK_Android.jar里了；


AdView SDK 安装指南
AdView应用推广使用指南
java_doc:详细的java接口说明



Android sdk源代码地址：https://github.com/adview/AdviewSDK_Android


联系AdView

欢迎您用以下方式和我们沟通，

Tel:   010-52911111  QQ：1263811658
Email: threadxyz@gmail.com
Blog： http://t.sina.com.cn/adview


                                                                                          
                                                                              北京快友世纪（Adview）
                                                                              http://www.adview.cn
