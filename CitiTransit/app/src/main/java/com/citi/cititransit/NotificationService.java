package com.citi.cititransit;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by nick on 11/15/17.
 */

public class NotificationService extends Service {


    private NotificationCompat.Builder notification;
    private final static int uniqueId = 45612;
    private NotificationManager nManager;

    public NotificationService(){

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_notif_small);
        notification.setTicker("Regular route is disrupted");
        notification.setContentTitle("ALERT: Your commute may be disrupted.");
        notification.setContentText("Click to re-route.");
        notification.setWhen(System.currentTimeMillis());

        Intent i = new Intent(this, RouteChoices.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        nManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Log.d("NoticationService", "Service is running");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    nManager.notify(uniqueId, notification.build());
                }

            }
        };

        Thread thread = new Thread(runnable);
        thread.run();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
