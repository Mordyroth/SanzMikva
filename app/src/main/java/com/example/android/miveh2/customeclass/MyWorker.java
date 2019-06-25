package com.example.android.miveh2.customeclass;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.android.miveh2.R;
import com.example.android.miveh2.model.Settings;
import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyWorker extends Worker {
    Context context;
    private FirebaseDatabase rootRef;
    private DatabaseReference dbSetting;


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        //  displayNotification("My Worker", "Hey I finished my work");
        addDataInDb();
        return Result.success();
    }

    private void addDataInDb() {
        rootRef = FirebaseDatabase.getInstance();
        dbSetting = rootRef.getReference(AppUtils.SETTING_TABLE);

        String roomNumber = PreferenceUtils.getInstance(context).get(AppUtils.ROOM_NUMBER);
        String mUUID = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        if (roomNumber != null && !roomNumber.equalsIgnoreCase("")) {


            Settings settings = new Settings();
            settings.setLast_time_date(AppUtils.getDateD());
            settings.setLast_time_millis(System.currentTimeMillis());
            settings.setRoom_key(roomNumber);
            settings.setUuid(mUUID);


            dbSetting.child(roomNumber).setValue(settings).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
    }

    private void displayNotification(String title, String task) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("simplifiedcoding", "simplifiedcoding", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(), "simplifiedcoding")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(1, notification.build());
    }
}
