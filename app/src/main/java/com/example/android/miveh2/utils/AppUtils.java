package com.example.android.miveh2.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AppUtils {

    public static String DEVICES_DB = "Devices";
    public static String ROOM_TABLE = "Room";
    public static String FCM_TOKEN = "FCMToken";



    //For Story Database
    public static String StoryDb = "Story";
    public static String StoryStorageFolder = "story_Image";
    public static String StoryVideoFolder = "Story_Video";





    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
//If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
