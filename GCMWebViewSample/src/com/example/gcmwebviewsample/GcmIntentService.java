package com.example.gcmwebviewsample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
 
    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM IntentService";
    
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
 
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), extras.getString("url"));
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString(), extras.getString("url"));
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                //for (int i = 0; i < 5; i++) {
            	for (int i = 0; i < 0; i++) {
                    Log.i(TAG, "Working... " + (i + 1) + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
 
                // Post notification of received message.
                sendNotification("Received: " + extras.getString("message"), extras.getString("url"));
 
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
 
        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
 
    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, String url) {
        // IDを変更することで通知が複数になる
        int count = getNotificationId();

        mNotificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, FullscreenActivity.class);
        intent.putExtra("update_url", url);
        // Intentの第２引数をuniqueに設定することで通知毎のurlが指定できるようなる
        PendingIntent contentIntent = PendingIntent.getActivity(this, count, intent, 0 );
        long[] pattern = {0,500,250,500};
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
        .setTicker(msg)
        .setVibrate(pattern)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("GCM WebViewSample")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText(msg))
        .setWhen(0)
        .setAutoCancel(true)
        .setContentText(msg);
 
        mBuilder.setContentIntent(contentIntent);
        // 第１引数をuniqueに設定することで通知の度に新しい通知が出来上がる
        mNotificationManager.notify(count, mBuilder.build());
    }
    
    private int getNotificationId(){
    	// とりあえずファイルに書き込んでIdを管理してみた
    	String FILENAME = "noficationid";
        FileInputStream fis;
        Integer count = 0;
		try {
			fis = openFileInput(FILENAME);
	        byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
	        String string = new String(buffer);
	        fis.close();
	        
	        count = Integer.parseInt(string);
	        count++;
	        string = count.toString();
	        
	        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
	        fos.write(string.getBytes());
	        fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	        FileOutputStream fos;
			try {
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
	        String defaultCount = "0";
	        fos.write(defaultCount.getBytes());
	        fos.close();
	        count = 1;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return count;
    }
}
