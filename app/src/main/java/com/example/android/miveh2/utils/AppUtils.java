package com.example.android.miveh2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AppUtils {

    public static String DEVICES_DB = "Devices";
    public static String ROOM_TABLE = "Room";
    public static String NEXT_ROOM_HELP = "NextRoomHelp";
    public static String HELP_TABLE = "Help";
    public static String HELP_HISTORY_TABLE = "Help_History";
    public static String SETTING_TABLE = "Setting";
    public static String FEEDBACK_TABLE = "Feedback";
    public static String ROOM_NUMBER = "room_number";
    public static String ROOM_RESET = "room_set";
    public static String FCM_TOKEN = "FCMToken";
    public static String TIME_FOR_DELETE_ROOM = "time_for_delete_room";


    //For Story Database
    public static String StoryDb = "Story";
    public static String StoryStorageFolder = "story_Image";
    public static String StoryVideoFolder = "Story_Video";
    private static String language;
    private static String dateAsString;
    private static String formattedDate;


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

    public static String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;

    }

    public static String getDateD() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        String formattedDate = df.format(c);
        return formattedDate;

    }

    public static void setLanguage(Activity activity, ImageView ivVolumeDown, ImageView ivVolumeUp) {


        Locale current = activity.getResources().getConfiguration().locale;

        //Locale current = Resources.getSystem().getConfiguration().locale;
        boolean locale = current.toString().contains("en");
        if (locale) {
            language = "iw";
            setMirroredEnable(true, ivVolumeDown, ivVolumeUp);
        } else {
            language = "en";
        }

        LocaleHelper.setLocale(activity, language);

        activity.recreate();


    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }


    //Convert Local time
    public static String getLocalDate(String dateStr) {


        SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm:ss a", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm", Locale.ENGLISH);
            outputFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return formattedDate;

    }

    public static void setMirroredEnable(boolean enabled, ImageView... view) {
        for (ImageView v : view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                v.getDrawable().setAutoMirrored(enabled);
            }
        }
    }
}
