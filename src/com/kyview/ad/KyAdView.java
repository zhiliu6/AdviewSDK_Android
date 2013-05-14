package com.kyview.ad;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader; 
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;


import com.kyview.DownloadService;
import com.kyview.ad.ApplyAdBean;
import com.kyview.ad.RetAdBean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyview.util.GifFrame;
import java.io.ByteArrayOutputStream;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.content.SharedPreferences;
import com.kyview.AdviewWebView;
import android.os.Bundle;
import com.kyview.AdViewTargeting;
import com.kyview.util.AdViewUtil;
	
public class KyAdView extends ViewGroup{
	
	static final String TAG = "AdViewAd";
	private String appId=null;
	private String address=null;
	private int topPadding = 0;
	private int internal=50;
	private int backGroundColor =Color.BLUE;
	private int textColor=Color.RED;
	private boolean isTestMode = false;
	private ApplyAdBean applyAdBean=null;
	private String agent1=null;
	private String agent2=null;
	private String agent1test=null;
	private RetAdBean retAdBean=null;
	private String getString=null;
	private String writeString=null;
	private String xmlCp=null;
	
	private Bitmap bitmap=null;	
	ImageView adImage=null;
	TextView adText = null;
	TextView logoText = null;
	
	private int screenWidth = 0;
	private int screenHeight = 0;

	//private Client mClient = new Client();
	private Thread		mThread = null;

	private ClientReport	mClientReport;
	private Thread	mReportThread = null;

	GifFrame mGifFrame = null;
	private Thread	mGifThread = null;

	public String keyDev=new String("000000000000000");
	private Intent i;
	  public class GifRefresh extends Thread {
		public void run() {
			super.run();
			
			while(!currentThread().isInterrupted()){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					currentThread().interrupt();
				}
				notifyRefreshGif();
		
			}
		}
	}
	  
	public class ClientReport implements Runnable{
		private String url;
		private String content;
		
		ClientReport(String url, String content){
			this.url = url;
			this.content = content;
		}
		public void run(){

		//Log.d(TAG, "ClientReport, url="+url);
		//Log.d(TAG, "ClientReport, content="+content);
		
	    	//String strResult=null;
	    	HttpPost httpRequest=new HttpPost(url);
	    	List<NameValuePair> params=new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("name",content));
	    	HttpClient httpClient = new DefaultHttpClient();

	    	try {
	    	
	    		//锟斤拷锟斤拷HTTP request
	    	     httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
	    
	    	     //取锟斤拷HTTP response
	    	     HttpResponse httpResponse=httpClient.execute(httpRequest);
	    	     if(httpResponse.getStatusLine().getStatusCode()==200){
	    	   
	    	    	 EntityUtils.toString(httpResponse.getEntity(),"UTF_8"); 
	    	        }else{
	    	        
	    	        }

	    	}catch(ClientProtocolException e){
	    	     	e.printStackTrace();
	    	} catch (UnsupportedEncodingException e) {
	    	    	e.printStackTrace();
	    	} catch (IOException e) {
	    	    	e.printStackTrace();
	    	}finally{
	    		httpClient.getConnectionManager().shutdown();
	    	}			
			
		}
	}
	
	public class Client implements Runnable{
		Context mcontext;
		String logo;
		Client(Context context,String logo ){
			mcontext = context;
			this.logo=logo;			
		}
		public void run(){	
			
			
						writeString=writeApplyAdXml();
						
						if(writeString!=null){
							if(applyAdBean.getTestMode() == 1){
								getString=getResponse(writeString,agent1test);								
							}else{
								getString=getResponse(writeString,agent1);
							}
						}
						if(getString!=null)
							
						{
							String xml=getString.replaceAll("\\n", "");
							
							
							
							xmlCp =xml.replaceAll("\\r", "");
							xmlCp=xmlCp.replace("&amp;", "#$amp;");
						
						}else{
							notifyConncetFail();
						}	
						if(xmlCp!=null){
							retAdBean=readXML(xmlCp);
							
						}else{
							retAdBean = null;
						}	
						if(retAdBean!=null)
						{
							
							{
								initPanal(logo);
								display();
								frushAd();
								if(applyAdBean.getTestMode() == 0){
								
									reportServer(0,1,0,agent2);
								}
							}
						}else{
							if(xmlCp != null){
								//if(mOnAdListener != null)
								//	mOnAdListener.onReceivedAd(KyAdView.this);
								notifyReceiveAdError();
							}
						}
						
			
			
		}

		private String writeApplyAdXml(){
    		XmlSerializer serializer = Xml.newSerializer();
    		StringWriter writer = new StringWriter();
    		String buffer;
    		try{
    			serializer.setOutput(writer);
    			serializer.startDocument(null, true);
    			
    			serializer.startTag("", "application");
    			serializer.startTag("", "idApp");
    			serializer.text(applyAdBean.getAppId());
    			serializer.endTag("", "idApp");
    			    			
    			serializer.startTag("", "system");
    			serializer.text(Integer.toString(applyAdBean.getSystem()));
    			serializer.endTag("", "system");
    			
    			serializer.endTag("","application");
    			serializer.endDocument();
    			buffer=writer.toString();
    		}
    		catch(Exception e)
    		{
    			throw new RuntimeException(e);
    		}
    		return buffer;
    	}
		
		private void reportServer(int type,int display,int click,String agent){
			String writeString=displayReport(type,display,click);
			
			getResponse(writeString,agent);
		}
		
		private String displayReport(int type,int display,int click){
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String buffer="";
			try{
				serializer.setOutput(writer);
				serializer.startDocument(null, true);
				
				serializer.startTag("", "application");
				serializer.startTag("", "idApp");
				serializer.text(applyAdBean.getAppId());
				serializer.endTag("", "idApp");
				
				serializer.startTag("", "idAd");
				serializer.text(retAdBean.getIdAd());
				serializer.endTag("", "idAd");
				
				
				serializer.startTag("", "system");
				serializer.text(Integer.toString(applyAdBean.getSystem()));
				serializer.endTag("", "system");

				serializer.startTag("", "reportType");
				serializer.text(Integer.toString(type));
				serializer.endTag("", "reportType");
				
				serializer.startTag("", "display");
				serializer.text(Integer.toString(display));
				serializer.endTag("", "display");
				
				serializer.startTag("", "click");
				serializer.text(Integer.toString(click));
				serializer.endTag("", "click");
				
				serializer.startTag("", "keyDev");
				serializer.text(keyDev);
				serializer.endTag("", "keyDev");
				
				serializer.endTag("","application");
				serializer.endDocument();
				buffer=writer.toString();
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
			return buffer;
		}
		
		private void display(){
			switch(retAdBean.getAdShowType())
			{
				case 0:
			 		break;
			 	case 1:
			 	case 2:		 		
				case 3:	
			 		clientBitMap(retAdBean);
			 		break;
			 	default:
			 		break;
			 }
		}

		private byte[] fileConnect(InputStream is) {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int ch = 0;
				while((ch = is.read()) != -1){
					baos.write(ch);
				}
				byte[] datas = baos.toByteArray();
				baos.close();
				baos = null;
				is.close();
				is = null;
				return datas;
			} catch (Exception e) {
				return null;
			}
		}
		
		@SuppressWarnings("unused")
		private void clientBitMap(RetAdBean retAdBean){
			String url=retAdBean.getAdShowPic();
			URL myFileUrl=null;
				
			try{
				myFileUrl=new URL(url);
			}catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try{
				long modifysince=0;
				SharedPreferences imagemodifysince = mcontext.getSharedPreferences("imagemodifysince", Context.MODE_PRIVATE);

				int pos=url.lastIndexOf("/");
				String filename = url.substring(pos+1);
					
				File updateDir = null;
				File updateFile = null;
				updateDir = new File(Environment.getExternalStorageDirectory(), "Adview/ad/");
				updateFile = new File(updateDir.getPath(), filename);
					
				if ("mounted".equals(Environment.getExternalStorageState())) 
				{
					if (updateDir.exists()) 
					{
						if (updateFile.exists())
						{
							modifysince = imagemodifysince.getLong(filename, 0);
						}
					}
					else
					{
						updateDir.mkdirs();
					}
				}

				//Log.d(TAG, "modifysince="+modifysince); 

				HttpURLConnection conn =(HttpURLConnection)myFileUrl.openConnection();
				if (modifysince>0)
					conn.setIfModifiedSince(modifysince);
				conn.setDoInput(true);
				conn.connect();

				int respCode;
				respCode = conn.getResponseCode();
				//Log.d(TAG, "respCode="+respCode); 

				long lastModify;
				lastModify = conn.getLastModified();
				//Log.d(TAG, "lastModify="+lastModify); 

				InputStream is=null;
				
				if (respCode == 304)
				{
					is = new FileInputStream(updateFile);
				}
				else if (respCode == 200)
				{
					is = conn.getInputStream();

					if (updateDir.exists())
					{
						SharedPreferences.Editor editor = imagemodifysince.edit();
						editor.putLong(filename, lastModify);
						editor.commit();

						FileOutputStream fileOutputStream = null;	
						fileOutputStream = new FileOutputStream(updateFile);

						byte[] buf = new byte[1024*8];
						int ch = -1;
						
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
						}
						fileOutputStream.flush();
						fileOutputStream.close();

						is = new FileInputStream(updateFile);
					}
				}

				if (is == null)
				{
					Log.d(TAG, "is null, respCode="+respCode);
					return;
				}
				else if (retAdBean.getAdShowType()==3)
				{
					//is = getContext().getResources().openRawResource(R.drawable.t3);
					mGifFrame = GifFrame.GreateGifImage(fileConnect(is));
					//Log.d(TAG, "mGifFrame size="+mGifFrame.size());
					if (mGifThread != null)
					{		
						mGifThread.interrupt();
						mGifThread=null;
					}	

					if (mGifFrame.size()==0)
					{
						is = conn.getInputStream();
						bitmap=BitmapFactory.decodeStream(is);
					}
					else
					{
						bitmap = mGifFrame.getImage();
						mGifFrame.nextFrame();
					}
					if (mGifFrame.size()>1)
					{
						mGifThread = new GifRefresh();	
						mGifThread.start();
					}
				}
				else
					bitmap=BitmapFactory.decodeStream(is);
				is.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private RetAdBean readXML(String xmlStr){
	    	RetAdBean retAdBean=new RetAdBean();
	    	if(xmlStr==null)
		    	return null;
		    StringReader sr = new StringReader(xmlStr);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;
			try{   
				db = dbf.newDocumentBuilder();   
			}catch (ParserConfigurationException pce) {   
				System.err.println(pce); // 锟斤拷锟届常时锟斤拷锟斤拷斐ｏ拷锟较拷锟饺伙拷锟斤拷顺锟斤拷锟斤拷锟酵��   
		        System.exit(1);   
			}
		    Document doc = null; 
		    try{   
		    	try{
					doc = db.parse(is);
					if(doc == null)
					{
						Log.d(TAG, "readXML, xmlStr="+xmlStr);
						return null;
					}
		        }catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d(TAG, "readXML, xmlStr="+xmlStr);
					return null;
				}   
		    }catch(DOMException dom){   
		    	System.err.println(dom.getMessage());   
		        System.exit(1);   
		    }catch(IOException ioe){   
		        System.err.println(ioe);   
		        System.exit(1);   
		    } 

		    
		    //org.w3c.dom.Element root = doc.getDocumentElement(); 
		    NodeList applications = doc.getElementsByTagName("application");
		    for (int i = 0; i < applications.getLength(); i++){ 
		    	org.w3c.dom.Element application = (org.w3c.dom.Element)applications.item(i);   
		        retAdBean.setIdApp(application.getAttribute("idApp"));
		        	 
		          
		        NodeList idAds = application.getElementsByTagName("idAd");   
		        if (idAds.getLength() == 1){   
		           	org.w3c.dom.Element e = (org.w3c.dom.Element) idAds.item(0);
		            Text t = (Text) e.getFirstChild();
		            if(t!=null)
		            retAdBean.setIdAd(t.getNodeValue());
	            }
		           
		        NodeList adShowTypes = application.getElementsByTagName("adShowType");   
		        if (adShowTypes.getLength() == 1) {   
		        	org.w3c.dom.Element e = (org.w3c.dom.Element) adShowTypes.item(0);
		            Text t = (Text) e.getFirstChild();
		            if(t!=null)
		            retAdBean.setAdShowType(Integer.parseInt(t.getNodeValue()));
		        }
		             
		        NodeList adShowTexts = application.getElementsByTagName("adShowText");   
		        if (adShowTexts.getLength() == 1) {   
		        	org.w3c.dom.Element e = (org.w3c.dom.Element) adShowTexts.item(0);
		            Text t = (Text) e.getFirstChild();   
		            if(t!=null)
		            retAdBean.setAdShowText(t.getNodeValue());
		        }
		             
		        NodeList adShowPics = application.getElementsByTagName("adShowPic"); 
		        if (adShowPics.getLength() == 1) {   
			       	org.w3c.dom.Element e = (org.w3c.dom.Element) adShowPics.item(0);
			       	Text t = (Text) e.getFirstChild();
			        if(t!=null)
			       	retAdBean.setAdShowPic(t.getNodeValue());
		        }
		            
		        NodeList adLinkTypes = application.getElementsByTagName("adLinkType");   
		        if (adLinkTypes.getLength() == 1) {   
		           	org.w3c.dom.Element e = (org.w3c.dom.Element) adLinkTypes.item(0);
		            Text t = (Text) e.getFirstChild();
		            if(t!=null)
		            retAdBean.setAdLinkType(Integer.parseInt(t.getNodeValue()));
		        }
		            
		        NodeList adLinks = application.getElementsByTagName("adLink");   
		        if (adLinks.getLength() == 1) {   
		        	org.w3c.dom.Element e = (org.w3c.dom.Element) adLinks.item(0);
		            Text t = (Text) e.getFirstChild();
		            if(t!=null)
		            retAdBean.setAdLink((t.getNodeValue()));
		            String link=retAdBean.getAdLink();
		            if(link!=null)
		            {
		            	link=link.replace("#$amp;", "&");
		            	
		            }
		            retAdBean.setAdLink(link);
	            }
		        
		    }
		    
		    return retAdBean;
		}
		
		private String getResponse(String writeStr,String agent){
			String strResult=null;
		   	HttpPost httpRequest=new HttpPost(agent);
		    List<NameValuePair> params=new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("name",writeStr));
		    HttpClient httpClient = new DefaultHttpClient();	
		    try{
		    	httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		    	HttpResponse httpResponse = httpClient.execute(httpRequest);
		    	if(httpResponse.getStatusLine().getStatusCode()==200){
		    		strResult=EntityUtils.toString(httpResponse.getEntity(),"UTF_8"); 
		    	}else{
		    	}

	    	}catch(ClientProtocolException e){
		    	e.printStackTrace();
	    	}catch(UnsupportedEncodingException e){
		    	e.printStackTrace();
		    }catch(IOException e){
		    	e.printStackTrace();
		    }finally{
		    	httpClient.getConnectionManager().shutdown();
		    }
		    return strResult;
	    }
	}	

	
	
	public KyAdView(Context context, String kyAdID, String addr,String logo,int refreshInterval, boolean istestMode, int backgroundColorVal, int textColorVal, String devid)
	{
		super(context);
		appId=new String(kyAdID);
		address=new String(addr);
		keyDev = new String(devid);
		internal=refreshInterval;
		if(internal < 30){
			internal = 30;
		}else if(internal > 300){
			internal = 300;
		}
		isTestMode = istestMode;
		backGroundColor = backgroundColorVal;
		textColor=textColorVal;
		//backGroundTransparent = backgroundTransparent;				
		initAd(context,logo);

	}	

	protected void onDetachedFromWindow ()
	{
		//Log.i(TAG, "onDetachedFromWindow");

		if (mGifThread != null)
		{	
			mGifThread.interrupt();
			mGifThread=null;
		}
	}

	public void setTopPadding(int TopPadding){
		topPadding = TopPadding;
		this.setPadding(0, topPadding, 0, 0);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		
	if (retAdBean.getAdShowType()==1)
		{
		int picLayoutWidth = 0;
		int picLayoutHeight = 0;		
		
		final View child=getChildAt(0);
	    child.measure(screenWidth, screenHeight);
	    child.setVisibility(View.VISIBLE);
	    int picWidth = child.getMeasuredWidth();
	    int picHeight = child.getMeasuredHeight();
		
	    
	    final View child1=getChildAt(1);
	    child1.measure(screenWidth, screenHeight);
	    child1.setVisibility(View.VISIBLE);
	    int textWidth = child1.getMeasuredWidth();
	    int textHeight = child1.getMeasuredHeight();
	    if(textWidth == 0){
	    	textHeight = 0;
	    }
		
	    final View child2=getChildAt(2);
	    child2.measure(screenWidth, screenHeight);
	    child2.setVisibility(View.VISIBLE);
	    int logoWidth = child2.getMeasuredWidth();
	    int logoHeight = child2.getMeasuredHeight();
	    if(logoWidth == 0){
	    	logoHeight = 0;
	    }
		
		
		if(textWidth + picWidth == 0){
			this.setVisibility(INVISIBLE);			
		}else{
			this.setVisibility(VISIBLE);			
		}

		if(picHeight > 0){
			picLayoutWidth = Math.min((this.getHeight() - 2)*picWidth/picHeight, this.getWidth()) - 2;
			picLayoutHeight = this.getHeight() - 2;
			child.layout(1, 1, picLayoutWidth + 1, picLayoutHeight +1);
			child1.layout(picLayoutWidth + 2, 0, this.getWidth(), this.getHeight());
		}
		else{
			if(textHeight > 0)
				child1.layout(0, 0 , this.getWidth(), this.getHeight());
		}
		if((logoWidth>0)&&(logoHeight>0))
			child2.layout(this.getWidth() - logoWidth, this.getHeight() -  logoHeight -1 , this.getWidth(), this.getHeight());
		
	}	
	else
	{
		//removeViewAt(2);
		//removeViewAt(1);

		int picLayoutWidth = 0;
		int picLayoutHeight = 0;		

		final View child=getChildAt(0);
		child.measure(screenWidth, screenHeight);
		child.setVisibility(View.VISIBLE);
		int picWidth = child.getMeasuredWidth();
		int picHeight = child.getMeasuredHeight();
		if (picWidth==0 || picHeight==0)
			return;
		
		picLayoutWidth = Math.min((this.getHeight())*picWidth/picHeight, this.getWidth()) ;
		picLayoutHeight = this.getHeight();
		child.layout((this.getWidth()-picLayoutWidth)/2, 0, picLayoutWidth + (this.getWidth()-picLayoutWidth)/2, picLayoutHeight);		
	}
	}
	
	private void initPanal(String logo){
		if(2==retAdBean.getAdShowType()||3==retAdBean.getAdShowType())
		this.setBackgroundColor(Color.TRANSPARENT);	
		else
		this.setBackgroundColor(backGroundColor);
		
		adImage=new ImageView(getContext());  
		addView(adImage);
		adText = new TextView(getContext());
		adText.setTextColor(textColor);
		adText.setTextSize(16);
		addView(adText);
		logoText = new TextView(getContext());
		logoText.setTextColor(textColor);
		logoText.setText(logo);
		logoText.setTextSize(10);		
		addView(logoText);	

		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(1000); 
		set.addAnimation(animation); 
	
		LayoutAnimationController controller =
		    new LayoutAnimationController(set, 0.25f);
		this.setLayoutAnimation(controller);	
	}
	
	private void initAd(Context context,String logo){

		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
			
		if (AdViewTargeting.getAdWidth() > 0)
		{
			screenWidth = AdViewTargeting.getAdWidth();
			screenHeight = AdViewTargeting.getAdHeight();

			double density = dm.density;
			screenHeight = AdViewUtil.convertToScreenPixels(screenHeight, density);
			screenWidth = AdViewUtil.convertToScreenPixels(screenWidth, density);
		}
		else
		{
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
		}
		
		initApplyAdInfo(context);
		
		mThread = new Thread(new Client(context,logo));	
		mThread.start();
		
	}	

	private void frushAd(){
 		Message message = new Message();       
 		message.what = 0; 
 		notifyReceiveAdOk();
 		
 
	}
	
	private void notifyConncetFail(){
 		Message message = new Message();       
 		message.what = 2;       
 		handler.sendMessage(message);
	}
	
	private void notifyReceiveAdError(){
 		Message message = new Message();       
 		message.what = 1;       
 		handler.sendMessage(message);
	}	

	private void notifyReceiveAdOk(){
 		Message message = new Message();       
 		message.what = 0;       
 		handler.sendMessage(message);
	}		

	private void notifyRefreshGif(){
 		Message message = new Message();       
 		message.what = 3;       
 		handler.sendMessage(message);
	}
	
	Handler handler = new Handler(){  
		 public void handleMessage(Message msg) {  
			 switch (msg.what) {       
	             	case 0: //
	             	{
	             		if(retAdBean.getAdShowText() == null){
	             			if(mOnAdListener != null)
								mOnAdListener.onReceivedAd(KyAdView.this);
	             			break;
	
	             		}else{ 		
	             			mOnAdListener.onReceivedAd(KyAdView.this);
							adText.setText(retAdBean.getAdShowText());

		            		if(bitmap != null){
		            			adImage.setImageBitmap(bitmap);	
		            		}	
		            		startLayoutAnimation();	             				
	             			
	            		}
	             	}
	             		break;
	             	case 1://
	             	{
	             		if(mOnAdListener != null)
							mOnAdListener.onConnectFailed(KyAdView.this);
					}	
	             		break;
	             	case 2://
	             	{
	             		if(mOnAdListener != null)
							mOnAdListener.onConnectFailed(KyAdView.this);
					}	
	             		break;
	             	case 3://
	             	{
				bitmap = mGifFrame.getImage();
				mGifFrame.nextFrame();
				
	             		if(bitmap != null){		
	            			adImage.setImageBitmap(bitmap);	
	            		}	
	            		startLayoutAnimation();	
			}	
	             		break;
	             	default:
	             		break;
	             }       
			 super.handleMessage(msg);   
		 }
	};
	
	private void initApplyAdInfo(Context context){
		applyAdBean=new ApplyAdBean();
		String id=getSysId(context);
		applyAdBean.setAppId(id);
		applyAdBean.setSystem(0);		
		if(isTestMode){
			applyAdBean.setTestMode(1);
		}else{
			applyAdBean.setTestMode(0);			
		}
		
		agent1=new String("http://"+address+"/nusoap/nusoap_agent1.php");
		agent2=new String("http://"+address+"/nusoap/nusoap_agent2.php");
		agent1test=new String("http://"+address+"/nusoap/nusoap_agent1_test.php");
		
		setOnClickListener(adView_listener);
		
	 }
/*	
	 private String clickResponse(String writeStr,String agent){
	    	String strResult=null;
	    	HttpPost httpRequest=new HttpPost(agent);
	    	List<NameValuePair> params=new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("name",writeStr));
	    	HttpClient httpClient = new DefaultHttpClient();
	    	try {
	    	
	    		//锟斤拷锟斤拷HTTP request
	    	     httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
	    
	    	     //取锟斤拷HTTP response
	    	     HttpResponse httpResponse=httpClient.execute(httpRequest);
	    	     if(httpResponse.getStatusLine().getStatusCode()==200){
	    	   
	    	    	 strResult=EntityUtils.toString(httpResponse.getEntity(),"UTF_8"); 
	    	        }else{
	    	        
	    	        }

	    	}catch(ClientProtocolException e){
	    	     	e.printStackTrace();
	    	} catch (UnsupportedEncodingException e) {
	    	    	e.printStackTrace();
	    	} catch (IOException e) {
	    	    	e.printStackTrace();
	    	}finally{
	    		httpClient.getConnectionManager().shutdown();
	    	}

	        return strResult;
	 }
*/
	 private String clickReport(int type,int display,int click){
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			String buffer;
			
			try{
				serializer.setOutput(writer);
				serializer.startDocument(null, true);
				
				serializer.startTag("", "application");
				serializer.startTag("", "idApp");
				serializer.text(applyAdBean.getAppId());
				serializer.endTag("", "idApp");
				
				serializer.startTag("", "idAd");
				serializer.text(retAdBean.getIdAd());
				serializer.endTag("", "idAd");
				
				
				serializer.startTag("", "system");
				serializer.text(Integer.toString(applyAdBean.getSystem()));
				serializer.endTag("", "system");
				
				serializer.startTag("", "reportType");
				serializer.text(Integer.toString(type));
				serializer.endTag("", "reportType");
				
				serializer.startTag("", "display");
				serializer.text(Integer.toString(display));
				serializer.endTag("", "display");
				
				serializer.startTag("", "click");
				serializer.text(Integer.toString(click));
				serializer.endTag("", "click");
				
				serializer.startTag("", "keyDev");
				serializer.text(keyDev);
				serializer.endTag("", "keyDev");
				
				serializer.endTag("","application");
				serializer.endDocument();
				buffer=writer.toString();
				
			}
			catch(Exception e)
			{
				throw new RuntimeException(e);
			}
			return buffer;
	}
	 
	private void clickServer(int type,int display,int click,String agent){
			String report=clickReport(type,display,click);
			//clickResponse(report,agent);
			//adViewLayout.kyAdviewReport(agent, report);
			mClientReport = new ClientReport(agent, report);
			mReportThread = new Thread(mClientReport);	
			mReportThread.start();
	}
	
	private View.OnClickListener adView_listener = new View.OnClickListener(){
			public void onClick(View v){
				Context context=v.getContext();
				String url=retAdBean.getAdLink();
				if(url!=null)
				{
					Log.i(TAG, "url="+url);
					
					int len=(new String("http").length());
					if(url.length()>=len)
					{	
						String tmpUrl=url.substring(0,len);
						int res=tmpUrl.compareTo(new String("http"));
					
						if(res==0)
						{
							if(applyAdBean.getTestMode() == 0){
								clickServer(1,0,1,agent2);
							}
							Log.i(TAG, "Begin OnClick action.....");
							openWebBrowser(url,context);
						}
					}
				}
			}
	};
	
		
	void openWebBrowser(final String url,final Context context){
			
		if(url.toLowerCase().endsWith(".apk")){
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setMessage("��ȷ������ô?");
			builder.setTitle("������ʾ");
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override 
				public void onClick(DialogInterface dialog, int which) {
					 i=new Intent(context,DownloadService.class);
						i.putExtra("adview_url",url);
						context.startService(i);	 
					
				}
			});
			builder.setNegativeButton("ȡ��", null);
			builder.show();
		}else{
			Intent intent = new Intent(context, AdviewWebView.class);
			Bundle bundle = new Bundle();
			bundle.putString("adviewurl", url);
			intent.putExtras(bundle);
			context.startActivity(intent);
			Log.e(TAG, "e");
		}	
		
		
	}
	
		
	 private String getSysId(Context context){
		 if(appId!=null)
			 return appId;
		 ApplicationInfo ai;
		 String id=null;
		 try {
				ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
				id=ai.metaData.getString("APP_ID");
		 }catch(NameNotFoundException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }
		 return id;
	 }
	
	 /**
	  * @see android.view.View#measure(int, int)
	  */
	 @Override
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		 int measuredHeight = measureHeight(heightMeasureSpec);
		 int measuredWidth = measureWidth(widthMeasureSpec);
		 setMeasuredDimension(measuredWidth, measuredHeight);


	 }
		
	private int measureWidth(int measureSpec) {
		final View child=getChildAt(0);
		child.measure(screenWidth, screenHeight);
		final View child1=getChildAt(1);
	    child1.measure(screenWidth, screenHeight);

	    if (AdViewTargeting.getAdWidth() > 0)
			return screenWidth;
		
	    int result=0;
	    int specMode = MeasureSpec.getMode(measureSpec);
	    int specSize = MeasureSpec.getSize(measureSpec);

	    if (specMode == MeasureSpec.EXACTLY) {
	         // We were told how big to be
	        result = specSize;
	    }else{
	         // Measure the text
	        result = child.getMeasuredWidth()+child1.getMeasuredWidth()+getPaddingLeft() + getPaddingRight()+2;
	        if (result>screenWidth)
	        	result=screenWidth;
	        if(specMode == MeasureSpec.AT_MOST) {
	             // Respect AT_MOST value if that was what is called for by measureSpec
	       	 result = Math.min(result, specSize);
	        }
	    }
	    return result;
	 }
	private int measureHeight(int measureSpec) {
		final View child=getChildAt(0);
		child.measure(screenWidth, screenHeight);

	    if (AdViewTargeting.getAdWidth() > 0)
			return screenHeight;
		
		int result = 0;
	    int specMode = MeasureSpec.getMode(measureSpec);
	    int specSize = MeasureSpec.getSize(measureSpec);
	    
	    if (specMode == MeasureSpec.EXACTLY) {
	         // We were told how big to be
	        result = specSize;
	    } else {
	         // Measure the text (beware: ascent is a negative number)
	    	if(screenHeight>=800 || screenWidth>=800)
	    		result=75;
	    	else if(screenHeight>=480 || screenWidth>=480)
	    		result=50;
	    	else if(screenHeight>=320)
	    		result=38;
	    	else
	    		result=50;
	    }
	    return result; 
	}   
	
	 private onAdListener mOnAdListener = null;
		public interface onAdListener {
			void onReceivedAd(KyAdView view);
			void onConnectFailed(KyAdView view);
		}
		public void setAdListener(onAdListener listener){
			mOnAdListener = listener;
		}
	
	
}
	