package com.tobbetu.MobileECG.activities.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tobbetu.MobileECG.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kadir Anil Turgut
 * Date: 15.03.2014
 * Time: 00:17
 */
public class GcmBroadcastReceiver extends BroadcastReceiver {

    private GoogleCloudMessaging gcm = null;
    private NotificationManager mNotificationManager = null;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (gcm == null)
            gcm = GoogleCloudMessaging.getInstance(context);

        Bundle extras = intent.getExtras();
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)
                && !extras.isEmpty()) {
            Log.e("GcmBroadcastReceiver", extras.getString("msg"));
            sendNotification(context, extras);
        }
    }

    private void sendNotification(Context context, Bundle extras) {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.icon);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setLargeIcon(icon)
                .setContentTitle(extras.getString("title"))
                .setContentText(extras.getString("msg"))
                .setAutoCancel(true);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}

