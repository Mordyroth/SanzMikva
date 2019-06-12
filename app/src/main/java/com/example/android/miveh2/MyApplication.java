package com.example.android.miveh2;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.example.android.miveh2.model.FireBaseDBInstanceModel;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyApplication extends Application {
    private FirebaseDatabase mFirebaseInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("lato_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        FireBaseDBInstanceModel model = FireBaseDBInstanceModel.getInstance();
        model.setmFirebaseInstance(mFirebaseInstance);
    }
}
