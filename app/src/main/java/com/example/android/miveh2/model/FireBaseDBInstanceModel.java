package com.example.android.miveh2.model;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by theone on 03/05/17.
 */

public class FireBaseDBInstanceModel {

    FirebaseDatabase mFirebaseInstance;
    static FireBaseDBInstanceModel model;

    public static FireBaseDBInstanceModel getInstance() {
        if (model == null) {
            model = new FireBaseDBInstanceModel();
        }
        return model;
    }

    public FirebaseDatabase getmFirebaseInstance() {
        return mFirebaseInstance;
    }

    public void setmFirebaseInstance(FirebaseDatabase mFirebaseInstance) {
        this.mFirebaseInstance = mFirebaseInstance;
    }
}
