package com.adview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service
{
   private String title = "adview";

   private String path = "";

   private File updateDir = null;
  private File updateFile = null;

   private static int index = 0;

  private NotificationManager updateNotificationManager = null;
   private Notification updateNotification = null;
 
   private Intent updateIntent = null;

  private PendingIntent updatePendingIntent = null;

  private Handler updateHandler = new Handler()
   {
    public void handleMessage(Message msg)
     {
       switch (msg.what)
       {
      case 0:
       Uri uri = Uri.fromFile(DownloadService.this.updateFile);
     Intent installIntent = new Intent();
     installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      installIntent.setAction("android.intent.action.VIEW");
       installIntent.setDataAndType(uri, 
          "application/vnd.android.package-archive");
       DownloadService.this.updatePendingIntent = PendingIntent.getActivity(
        DownloadService.this, 0, installIntent, 0);

       DownloadService.this.updateNotification.tickerText = ("下载完成");

         DownloadService.this.updateNotificationManager.notify(DownloadService.index, DownloadService.this.updateNotification);
        DownloadService.this.updateNotificationManager.cancel(DownloadService.index);

         DownloadService.this.startActivity(installIntent);

        DownloadService.this.stopService(DownloadService.this.updateIntent);
        break;
       case 1:
        String description = msg.getData().getString("faild");
        Toast.makeText(DownloadService.this, description, 0).show();
         //DownloadService.this.updateNotification.icon = icon;
        DownloadService.this.updateNotification.tickerText = description;
        DownloadService.this.updateNotification.flags |= 16;
         DownloadService.this.updateNotification.setLatestEventInfo(DownloadService.this, 
           DownloadService.this.title, description, DownloadService.this.updatePendingIntent);
         DownloadService.this.updateNotificationManager.notify(DownloadService.index, DownloadService.this.updateNotification);
        DownloadService.this.stopService(DownloadService.this.updateIntent);
        break;
       default:
      DownloadService.this.stopService(DownloadService.this.updateIntent);
      }
    }
  };

 public IBinder onBind(Intent intent)
   {
     return null;
   }

	public void onStart(Intent intent, int startId)
	{
		index += 1;
		if (index>5)
			index = 1;
		
		path = intent.getStringExtra("adview_url");

		if ("mounted".equals(Environment.getExternalStorageState())) {
			updateDir = new File(Environment.getExternalStorageDirectory(), "Adview/download/");
			updateFile = new File(this.updateDir.getPath(), this.title + index + ".apk");
		}

		updateNotificationManager = ((NotificationManager)getSystemService("notification"));
		long when = System.currentTimeMillis();
		updateNotification =  new Notification(17301633, "开始下载应用",  when);

		updateIntent = new Intent(this, intent.getClass());
		updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		updateNotification.setLatestEventInfo(this, "正在下载应用",  "0%", updatePendingIntent);
		updateNotification.flags |= 2;

		updateNotificationManager.notify(index, this.updateNotification);

		new Thread(new DownloadRunnable()).start();
		super.onStart(intent, startId);
	}
// downloadUrl下载链接        saveFile  
   public long downloadFile(String downloadUrl, File saveFile) throws Exception
  {
   int downloadCount = 0;
     int currentSize = 0;
     long totalSize = 0L;
     int updateTotalSize = 0;
     HttpURLConnection httpConnection = null;
     InputStream is = null;
     FileOutputStream fos = null;
     try {
       URL url = new URL(downloadUrl);
      httpConnection = (HttpURLConnection)url.openConnection();
       httpConnection
        .setRequestProperty("User-Agent", "PacificHttpClient");
       if (currentSize > 0) {
         httpConnection.setRequestProperty("RANGE", "bytes=" + 
          currentSize + "-");
     }
       httpConnection.setConnectTimeout(10000);
       httpConnection.setReadTimeout(20000);
      updateTotalSize = httpConnection.getContentLength();
      if (httpConnection.getResponseCode() == 404) {
         throw new Exception("fail!");
     }
       is = httpConnection.getInputStream();
       fos = new FileOutputStream(saveFile, false);
       byte[] buffer = new byte[4096];
       int readsize = 0;
       do
       {
         fos.write(buffer, 0, readsize);
        totalSize += readsize;
 
        if ((downloadCount == 0) || 
          ((int)(totalSize * 100L / updateTotalSize) - 10 > downloadCount)) {
           downloadCount += 10;
           this.updateNotification.setLatestEventInfo(this, 
           "正在下载应用", (int)totalSize * 100 / 
            updateTotalSize + "%", 
             this.updatePendingIntent);
          this.updateNotificationManager.notify(index, this.updateNotification);
         }
        if ((readsize = is.read(buffer)) <= 0) break; 
       }
       while (this != null);
     }
    finally
    {
       if (httpConnection != null) {
        httpConnection.disconnect();
       }
       if (is != null) {
         is.close();
       }
       if (fos != null) {
        fos.close();
       }
    }
     return totalSize;
   }

class DownloadRunnable  implements Runnable
  {
   Message message = DownloadService.this.updateHandler.obtainMessage();

    DownloadRunnable() {  }

   public void run() { this.message.what = 0;
      try {
         if (!DownloadService.this.updateDir.exists()) {
           DownloadService.this.updateDir.mkdirs();
       }
         if (!DownloadService.this.updateFile.exists())
           DownloadService.this.updateFile.createNewFile();
       }
     catch (Exception e) {
        e.printStackTrace();
       this.message.what = 1;
       Bundle bundle = new Bundle();
         bundle.putString("faild", "没有存储卡");
        this.message.setData(bundle);

         DownloadService.this.updateHandler.sendMessage(this.message);
         return;
       }
       try {
        long downloadSize = DownloadService.this.downloadFile(DownloadService.this.path, DownloadService.this.updateFile);
        if (downloadSize > 0L)
        {
          DownloadService.this.updateHandler.sendMessage(this.message);
         }
      } catch (Exception e) {
         e.printStackTrace();
         this.message.what = 1;
         Bundle bundle = new Bundle();
        bundle.putString("faild", "下载失败");
         this.message.setData(bundle);

       DownloadService.this.updateHandler.sendMessage(this.message);
        return;
      }
     }
  }
 }
