package com.kyview;
 
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kyview.util.AdViewUtil;


public class AdviewWebView extends Activity 
{
	private static final int TOOLBAR_ID = 88;
	private static final String WEBVIEW_IMAGE_BASE_PATH = "/com/kyview/assets/";
   
	private static final int BTN_TO_PREV = 1;
	private static final int BTN_TO_NEXT = 2;
	private static final int BTN_DO_REFRESH = 3;
	private static final int BTN_DO_SHARE = 4;
	private static final int BTN_DO_CLOSE = 5;
	private static final int BTN_DO_STOP = 6;

	WebView adWebView;
	WebViewProgressBar adWebViewProgressBar;

	String adLink;
	boolean isLoading = false;

	ImageView btnToPrev;
	ImageView btnToNext;
	ImageView btnDoRefresh;

	ArrayList<BtnOnTouchListener> touchList;
	int screenWidth = -1;

	Handler setWedViewScaleHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			boolean canZoomOut = AdviewWebView.this.adWebView.zoomOut();
			while (canZoomOut) {
				canZoomOut = AdviewWebView.this.adWebView.zoomOut();
			}
			super.handleMessage(msg);
		}
	};

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		double density = AdViewUtil.getDensity(this);
		int toolbar_height = AdViewUtil.convertToScreenPixels(48, density);
		int progressbar_height = AdViewUtil.convertToScreenPixels(5, density);

		FrameLayout frameLayout = new FrameLayout(this);
		FrameLayout.LayoutParams webFrameParams = new FrameLayout.LayoutParams(-1, -1);
		FrameLayout.LayoutParams porFrameParams = new FrameLayout.LayoutParams(-1, progressbar_height);
		adWebViewProgressBar = new WebViewProgressBar(this);
		adWebViewProgressBar.setVisibility(8);

		RelativeLayout relativeLayout = new RelativeLayout(this);

		RelativeLayout.LayoutParams webViweLayoutParams = new RelativeLayout.LayoutParams(-1, -1);
		webViweLayoutParams.addRule(2, TOOLBAR_ID);
		RelativeLayout.LayoutParams barLayoutParams = new RelativeLayout.LayoutParams(-1, toolbar_height);
		barLayoutParams.addRule(12);

		adWebView = new WebView(this);
		adWebView.setVerticalScrollBarEnabled(false);

		LinearLayout barLayout = new LinearLayout(this)
		{
			protected void onSizeChanged(int w, int h, int ow, int oh)
			{
				setBackgroundDrawable(getBarBackground(h));
			}
		};
   
		barLayout.setId(TOOLBAR_ID);
		barLayout.setOrientation(0);

		LinearLayout.LayoutParams btnImgLayoutParams = new LinearLayout.LayoutParams(-2, toolbar_height, 1.0F);
		btnImgLayoutParams.gravity = 16;

		setImgBtn(barLayout, btnImgLayoutParams);

		relativeLayout.addView(barLayout, barLayoutParams);
		relativeLayout.addView(this.adWebView, webViweLayoutParams);

		frameLayout.addView(relativeLayout, webFrameParams);
		frameLayout.addView(this.adWebViewProgressBar, porFrameParams);
		ViewGroup.LayoutParams viewGrLayoutParams = new ViewGroup.LayoutParams(-1, -1);
		addContentView(frameLayout, viewGrLayoutParams);

		adWebView.setWebViewClient(new webViewClient());

		adLink = getIntent().getExtras().getString("adviewurl");
		if (checkFilter(adLink) != -1) {
			finish();
			return;
		}

		adWebView.setDownloadListener(new AdviewDownloadListener());
		adWebView.setWebChromeClient(new ProWebChromeClient());

		WebSettings webSettings = this.adWebView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		adWebView.addJavascriptInterface(new AdViewJsObj(), "local_obj");
		webSettings.setSupportZoom(true);
		adWebView.loadUrl(this.adLink);
		
	}

	private BitmapDrawable getBarBackground(int height)
	{
		try
		{
			InputStream barBgStream = getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_bg.png");  
			BitmapDrawable barbgBitmapDrawable = new BitmapDrawable(barBgStream);
			Bitmap barBg = barbgBitmapDrawable.getBitmap();
			Matrix barBgMatrix = new Matrix();
			barBgMatrix.setScale(1.0F, height * 1.0F / barBg.getHeight());
			barBg = Bitmap.createBitmap(barBg, 0, 0, barBg.getWidth(), 
			barBg.getHeight(), barBgMatrix, false);
			barbgBitmapDrawable = new BitmapDrawable(barBg);
			barbgBitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			barbgBitmapDrawable.setDither(true);
			if (barBgStream != null) {
				barBgStream.close();
			}
			return barbgBitmapDrawable; 
		} catch (Exception e) {
			Log.i(AdViewUtil.ADVIEW, e.toString());
		}
		
		return null;
	}

	private void setImgBtn(LinearLayout linearLayout, LinearLayout.LayoutParams btnImgLayoutParams)
	{
		BitmapDrawable btnToPrevImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_back.png"));
		BitmapDrawable btnToPrevHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_back_hover.png"));
		BitmapDrawable btnToPrevGreyImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_back_grey.png"));

		BitmapDrawable btnToNextImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_next.png"));
		BitmapDrawable btnToNextHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_next_hover.png"));
		BitmapDrawable btnToNextGreyImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_next_grey.png"));

		BitmapDrawable btnDoRefreshImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_refresh.png"));
		BitmapDrawable btnDoRefreshHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_refresh_hover.png"));

		BitmapDrawable btnDoStopImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_pause.png"));
		BitmapDrawable btnDoStopHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_pause_hover.png"));

		BitmapDrawable btnDoShareImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_share.png"));
		BitmapDrawable btnDoShareHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_share_hover.png"));

		BitmapDrawable btnDoCloseImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_close.png"));
		BitmapDrawable btnDoCloseHoverImg = new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_close_hover.png"));

		btnToPrev = new ImageView(this);
		btnToPrev.setId(BTN_TO_PREV);
		btnToPrev.setImageDrawable(btnToPrevGreyImg);
		btnToPrev.setTag(new BtnOnTouchListener(btnToPrevHoverImg, btnToPrevImg));

		btnToNext = new ImageView(this);
		btnToNext.setId(BTN_TO_NEXT);
		btnToNext.setImageDrawable(btnToNextGreyImg);
		btnToNext.setTag(new BtnOnTouchListener(btnToNextHoverImg, btnToNextImg));

		this.btnDoRefresh = new ImageView(this);
		this.btnDoRefresh.setId(BTN_DO_REFRESH);
		this.btnDoRefresh.setImageDrawable(btnDoStopImg);
		touchList = new ArrayList<BtnOnTouchListener>();
		touchList.add(new BtnOnTouchListener(btnDoRefreshHoverImg, btnDoRefreshImg));
		touchList.add(new BtnOnTouchListener(btnDoStopHoverImg, btnDoStopImg));
		btnDoRefresh.setTag(touchList);

		ImageView btnDoShare = new ImageView(this);
		btnDoShare.setId(BTN_DO_SHARE);
		btnDoShare.setImageDrawable(btnDoShareImg);
		btnDoShare.setOnTouchListener(new BtnOnTouchListener(btnDoShareHoverImg, btnDoShareImg));
		ImageView btnDoClose = new ImageView(this);
		btnDoClose.setId(BTN_DO_CLOSE);
		btnDoClose.setImageDrawable(btnDoCloseImg);
		btnDoClose.setOnTouchListener(new BtnOnTouchListener(btnDoCloseHoverImg, btnDoCloseImg));

		linearLayout.addView(btnToPrev, btnImgLayoutParams);
		linearLayout.addView(btnToNext, btnImgLayoutParams);
		linearLayout.addView(btnDoRefresh, btnImgLayoutParams);
		linearLayout.addView(btnDoShare, btnImgLayoutParams);
		linearLayout.addView(btnDoClose, btnImgLayoutParams);
	}

	private int checkFilter(String url)
	{
		if (url.indexOf("tel:") > 0) {
			try {
				Intent callIntent = new Intent("android.intent.action.DIAL", Uri.parse(url));
				startActivity(callIntent);
			} catch (Exception e) {
				Log.i(AdViewUtil.ADVIEW, e.toString());
			}
			
			return 0;
		}
		if (url.contains("market://")) {
			Uri uri = Uri.parse(url);
			Intent marketIntent = new Intent("android.intent.action.VIEW", uri);

			List <ResolveInfo> packList = getPackageManager().queryIntentActivities(marketIntent, 0);
			if (packList.size() > 0)
				startActivity(marketIntent);
			else {
				Toast.makeText(this, "¡¥Ω”¥ÌŒÛ", 200).show();
			}
			return 0;
		}
		
		return -1;
	}
	 
	@SuppressWarnings("rawtypes")
	private void onWebViewLoad()
	{
		isLoading = true;

		btnDoRefresh.setImageDrawable(new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_pause.png")));
		btnDoRefresh.setId(BTN_DO_STOP);
		btnDoRefresh.setOnTouchListener((View.OnTouchListener)((ArrayList)btnDoRefresh.getTag()).get(1));
	}
 
	@SuppressWarnings("rawtypes")
	private void loadComplete()
	{
		isLoading = false;

		if (adWebView.canGoBack()) {
			btnToPrev.setImageDrawable(new BitmapDrawable(getClass()
				.getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_back.png")));
			btnToPrev.setOnTouchListener((View.OnTouchListener)this.btnToPrev.getTag());
		} else {
			btnToPrev.setImageDrawable(new BitmapDrawable(getClass()
				.getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_back_grey.png")));
			btnToPrev.setOnTouchListener(null);
		}

		if (adWebView.canGoForward()) {
			btnToNext.setImageDrawable(new BitmapDrawable(
				getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_next.png")));
			btnToNext.setOnTouchListener((View.OnTouchListener)this.btnToNext.getTag());
		} else {
			btnToNext.setImageDrawable(new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_next_grey.png")));
			btnToNext.setOnTouchListener(null);
		}

		btnDoRefresh.setImageDrawable(new BitmapDrawable(
			getClass().getResourceAsStream(WEBVIEW_IMAGE_BASE_PATH+"webview_bar_refresh.png")));
		btnDoRefresh.setId(BTN_DO_REFRESH);
		btnDoRefresh.setOnTouchListener((View.OnTouchListener)((ArrayList)this.btnDoRefresh.getTag()).get(0));
	}

	private void shareConent()
	{
		Intent intent = new Intent("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra("android.intent.extra.SUBJECT", "");
		intent.putExtra("android.intent.extra.TEXT", this.adLink);
		intent.setFlags(268435456);
		startActivity(Intent.createChooser(intent, "∑÷œÌƒ⁄»›"));
	} 
 
	protected void onPause()
	{
		super.onPause();
	}

	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}

	protected void onDestroy()
	{
		super.onDestroy();
	}

	class AdViewJsObj
	{
		AdViewJsObj()
		{
		}

		public void setWinth(int width)
		{
			AdviewWebView.this.screenWidth = (AdviewWebView.this.screenWidth == -1 ? AdviewWebView.this
			.getWindowManager().getDefaultDisplay().getWidth() : 
			AdviewWebView.this.screenWidth);

			Message msg = new Message();
			msg.arg1 = (int)(AdviewWebView.this.screenWidth * 1.0F / width * 100.0F);
			if (AdviewWebView.this.screenWidth < width - 100)
				Log.d(AdViewUtil.ADVIEW, "sedWinth, "+(width - 100));
		}
	}


	class AdviewDownloadListener implements DownloadListener
	{
		AdviewDownloadListener()
		{
		}

		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
		{
			// remove download service
//			Intent updateIntent = new Intent(AdviewWebView.this, DownloadService.class);
//			updateIntent.putExtra("adview_url", url);
//			AdviewWebView.this.startService(updateIntent);
//			AdviewWebView.this.finish();
		}
	}

	class BtnOnTouchListener implements View.OnTouchListener
	{
		Drawable downImg;
		Drawable upImg;

		public BtnOnTouchListener(Drawable downImg, Drawable upImg)
		{
			this.downImg = downImg;
			this.upImg = upImg;
		}

		public boolean onTouch(View view, MotionEvent event)
		{
			if (event.getAction() == 0) {
				((ImageView)view).setImageDrawable(this.downImg);
			}
			else if (event.getAction() == 1) {
				((ImageView)view).setImageDrawable(this.upImg);

				if (adWebView != null) {
					int viewId = view.getId();
					if (viewId == BTN_TO_PREV) {
						AdviewWebView.this.adWebView.goBack();
					} else if (viewId == BTN_TO_NEXT) {
						AdviewWebView.this.adWebView.goForward();
					} else if (viewId == BTN_DO_SHARE) {
						AdviewWebView.this.shareConent();
					} else if (viewId == BTN_DO_REFRESH) {
						AdviewWebView.this.adWebView.reload();
					} else if (viewId == BTN_DO_STOP) {
						AdviewWebView.this.adWebView.stopLoading();
						AdviewWebView.this.loadComplete();
					} else if (viewId == BTN_DO_CLOSE) {
						AdviewWebView.this.finish();
					}
				}
			}

			return true;
		}
	}

	class ProWebChromeClient extends WebChromeClient
	{
		ProWebChromeClient()
		{
		}

		public void onProgressChanged(WebView view, int newProgress)
		{
			if (newProgress > 0) {
				AdviewWebView.this.adWebViewProgressBar.setProgress(newProgress);

				if (newProgress < 100) {
					if (!AdviewWebView.this.isLoading)
						AdviewWebView.this.onWebViewLoad();
				}
				else {
					AdviewWebView.this.loadComplete();
				}
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	class webViewClient extends WebViewClient
	{
		webViewClient()
		{
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			int param = AdviewWebView.this.checkFilter(url);

			if (param == 0)
				return true;
			if (param == 1) {
				return false;
			}

			return super.shouldOverrideUrlLoading(view, url);
		}

		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
		}
	}

	class WebViewProgressBar extends View
	{
		int width;
		int height;
		Context context;
		Bitmap sourBg;
		Bitmap barBg;
		boolean isInit = false;

		int currProgress = 0;
	 
		Handler closeViewHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				if (WebViewProgressBar.this.currProgress == 100) {
					WebViewProgressBar.this.setVisibility(8);
					WebViewProgressBar.this.currProgress = 0;
				}
				super.handleMessage(msg);
			}
		};

		public WebViewProgressBar(Context ctx)
		{
			super(ctx);
			context = ctx;
		}

		private void initialize()
		{
			this.width = getWidth();
			this.height = getHeight();

			this.sourBg = new BitmapDrawable(getClass().getResourceAsStream(
				WEBVIEW_IMAGE_BASE_PATH+"progressbarbg.png")).getBitmap();
			this.sourBg = conBitmapSize(this.sourBg, this.width, this.height);

			this.isInit = true;

			if (this.currProgress > 0) {
				setProgress(this.currProgress);
			}
		}
	 
		protected void onSizeChanged(int w, int h, int oldw, int oldh)
		{
			initialize();
			super.onSizeChanged(w, h, oldw, oldh);
		}

		private Bitmap conBitmapSize(Bitmap bitmap, int width, int height)
		{
			Matrix matrix = new Matrix();
			matrix.setScale(1.0F * width / bitmap.getWidth(), 1.0F * height / 
			bitmap.getHeight());
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 
			bitmap.getHeight(), matrix, false);

			return bitmap;
		}

		private Bitmap cutBitmap(Bitmap bitmap, int width, int height)
		{
			Bitmap cutBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
			return cutBitmap;
		}

		private Bitmap toRoundCorner(Bitmap bitmap, int pixels)
		{
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
			bitmap.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			int color = -12434878;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);
			float roundPx = pixels;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}
	 
		protected void onDraw(Canvas canvas)
		{
			if ((this.barBg != null) && (this.isInit)) {
				canvas.drawBitmap(this.barBg, 0.0F, 0.0F, null);
			}
			super.onDraw(canvas);
		}

		public void setProgress(int progress)
		{
			this.currProgress = progress;

			if (!this.isInit) {
				Log.i(AdViewUtil.ADVIEW, "setProgress, not init");
				setVisibility(0);
				return;
			}
			int vis = getVisibility();

			if (vis == 8) {
				setVisibility(0);
			}

			int ferWidth = (int)(progress / 100.0F * this.width);

			if (this.sourBg != null) {
				this.barBg = cutBitmap(this.sourBg, ferWidth, this.height);
				this.barBg = toRoundCorner(this.barBg, 5);
				invalidate();
			} else {
				Log.i(AdViewUtil.ADVIEW, "setProgress, bg is null");
				setVisibility(8);
			}
			if (progress >= 100)
				this.closeViewHandler.sendMessageDelayed(this.closeViewHandler.obtainMessage(1000), 0L);
		}
	}

}
