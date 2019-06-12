package com.example.android.miveh2;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.example.android.miveh2.model.FireBaseDBInstanceModel;
import com.google.firebase.database.FirebaseDatabase;


public class MivehApp extends Application {

    private FirebaseDatabase mFirebaseInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        FireBaseDBInstanceModel model = FireBaseDBInstanceModel.getInstance();
        model.setmFirebaseInstance(mFirebaseInstance);
    }
}
