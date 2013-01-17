
欢迎使用AdView Android SDK。开发者通过使用AdView，可以实现众多广告公司广告的排列组合优化；使用应用互推功能可以实现应用推广。

版本说明
====================================
当前AdView支持的android最低版本为1.6版。

====================================
平台版本（AdView和各家广告平台的SDK均在libs目录下）：
	库名				广告平台					版本
AdViewSDK_Android.jar			AdView原生聚合：随踪、优友（Aduu）、Inmobi	1.8.6
		
adlantis.jar				Adlantis					1.3.7
adchina_android_sdk.jar			易传媒						2.6.2
adlib_android.jar			哇棒						2.3.0
adOn-3.2.7.jar				Vpon						3.2.7
adtouch-embed-sdk-1.1.0.jar		AdTouch						1.1.0
Adwo_Android_SDK3.1.jar			AdWo						3.1
ad-fractalist-sdk-android.jar		飞云						2.0
airAD_sdk.jar				AirAD						1.3.3
Baidu_MobAds_SDK_Agg_3.0.jar		百度						3.0
AppMediaAdAndroidSdk-1.1.0.jar		AppMedia					1.1.0
domob_android_sdk.jar			多盟						3.1.5
GoogleAdMobAdsSdk-6.2.1.jar		AdMOB						6.2.1
gssdk_2.0.2.jar				GreyStripe					2.0.2
IZPView.jar				易赞普						1.0.3
l_android_adsdk.jar			百分通联					2.1.0.0
immobSDK.jar				力美						2.2
mdotm-sdk-android.jar			MdotM						3.0.0
MMAdView.jar				MillennialMedia					4.6.0
mobisageSDK.jar				艾德思奇					5.1.0
momark_android_sdk2012_v1.3.0.jar	Momark						1.3.0
smartmad-sdk-android.jar		亿动智道					2.0.2
SOMAAndroidSDK2.5.4.jar			Smaato						2.5.4
Tencent_MobWIN_SDK_2.0.jar		腾讯(MobWin)					2.0
tinmoo-android-sdk_v3.1.0.jar		天幕(Tinmoo)					3.1.0
umeng_ad_android_sdk_v1.4.4.jar		友盟						1.4.4
WiAd.jar				微云						4.0.0
WiCommon.jar				微云						4.0.0
		
wqmobile_android_sdk.jar		帷千						3.0.2
youmi-android.jar			有米						3.08
zestadz_sdk_androidv1.2.jar		ZestADZ						1.2


平台选择
====================================
如果你是一个新手，对广告平台不够了解，不知道如何选择广告平台，平台是否收入稳定。
Adview建议您使用百度，亿动智道，Inmobi平台。




注意事项
====================================
广告平台	描述
友盟		需要把umeng_res目录下的资源文件也放到项目中
微云		包括WiAd.jar,WiCommon.jar，需要把wiyun_res目录下的资源文件也放到项目中
帷千、亿赞普	需要把<uses-sdk android:minSdkVersion="4"/>去掉，否则广告显示尺寸会缩小；
		如果去掉<uses-sdk android:minSdkVersion="4"/>的话，对于高分辨率的手机后台统计的可能不准；
聚赢		若需要lbs功能，请加上<uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>
Admob		使用admob 4.3.1以上版本，需要android 3.2以上版本才能正确编译, 
		且需要在AndroidManifest.xml中配置为android:configChanges="keyboard|keyboardHidden|orientation|		screenLayout|uiMode|screenSize|smallestScreenSize"/>。（sample中缺省为android:configChanges="orientation|keyboard|keyboardHidden"）
GreyStripe	在sample中的Manifest注释掉了GreyStripe广告的声明，需要使用请自行打开

其他：
名词		描述
离线配置功能	需要使用离线配置功能，请从adview后台点击“离线配置下载”下载配置文件，保存在<assets>文件夹中，离线配置确保		任何情况下都能展示广告
内置下载功能	请在AndroidManifest.xml文件中声明com.kyview.DownloadService，互推和内嵌广告下载的时候要用

市场渠道
====================================
开发者可以设置渠道，在Manifest文件中设置<meta-data android:name="AdView_CHANNEL" android:value="GFAN"></meta-data> 
(AdViewTargeting.setChannel接口已经作废); 目前支持的渠道包括下面列出的，各个市场的链接见移动广告观察(http://t.adview.cn )
EOE		优亿市场
GOOGLEMARKET	谷歌电子市场
APPCHINA	应用汇
HIAPK		安卓市场
GFAN		机锋
GOAPK		安智
NDUOA		N多网
91Store		手机91
LIQUCN		历趣
WAPTW		天网
ANDROIDCN	安卓中国
GGDWON		G友网
ANDROIDAI	安卓之家
STARANDROID	安卓星空
ANDROIDD	安致
YINGYONGSO	应用搜
IMOBILE		手机之家
SOUAPP		搜应用
MUMAYI		木蚂蚁
MOBIOMNI	欧米
PAOJIAO		泡椒网
AIBALA		爱扒拉市场
COOLAPK		酷安网
ANFONE		安丰
APKOK		乐致网
如果不配置，或配置其他的值，一律作为"OTHER"处理。

相关文档
====================================
AdView SDK 安装指南
AdView应用推广使用指南
java_doc:详细的java接口说明

源代码开放
====================================
Android sdk已经开源，源代码地址：
https://github.com/adview/AdviewSDK_Android

联系方式
====================================
欢迎联系AdView，您可以用以下方式和我们沟通：

Tel: 010-52911111  QQ: 1263811658
Email: threadxyz@gmail.com
Blog: http://t.sina.com.cn/adview
www: http://www.adview.cn

