package com.example.android.miveh2.firebaseservices;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;


import com.example.android.miveh2.AppUtils;
import com.example.android.miveh2.CustomListActivity;
import com.example.android.miveh2.R;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


public class FirebaseNotificationReceiver extends FirebaseMessagingService {

    private final static String TAG = FirebaseNotificationReceiver.class.getSimpleName();

    @Override
    public void onNewToken(String token) {
        Log.d("TAG", "Refreshed token: " + token);

        if (TextUtils.isEmpty(PreferenceUtils.getInstance(getApplicationContext()).get(AppUtils.FCM_TOKEN))) {
            PreferenceUtils.getInstance(getApplicationContext()).save(AppUtils.FCM_TOKEN, token);
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        try {
            String msg = remoteMessage.getData().get("message");
            Log.e("Firebase", "msg : " + msg);

            JSONObject jsonObject = new JSONObject(msg);

            int badge = 0;
            badge = jsonObject.getInt("badge");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, CustomListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1 , notificationBuilder.build());
    }

    protected int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.user_icon : R.drawable.user_icon;
    }

}
