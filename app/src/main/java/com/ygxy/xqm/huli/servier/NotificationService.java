package com.ygxy.xqm.huli.servier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ygxy.xqm.huli.MyNotificationActivity;
import com.ygxy.xqm.huli.R;

/**
 * Created by XQM on 2017/4/3.
 */

public class NotificationService extends Service{
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent1 = new Intent(this, MyNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent1,0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setContentTitle("新消息")
                .setContentText("pk")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .build();
        startForeground(1,notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }).start();
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotification(String title){
        Intent intent = new Intent(this, MyNotificationActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle("新消息");
        builder.setContentText(title);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        return builder.build();
    }

    private NotificationBinder mBinder = new NotificationBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

     public class NotificationBinder extends Binder{
        public void showNotification(String title){
//            stopForeground(true);
            Log.d("servier","show");
//            getNotificationManager().notify(1,getNotification(title));
            startForeground(1,getNotification(title));
        }
    }
}
