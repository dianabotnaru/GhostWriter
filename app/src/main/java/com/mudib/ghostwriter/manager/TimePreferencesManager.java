package com.mudib.ghostwriter.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by diana on 07/02/2018.
 */

public class TimePreferencesManager {


    static volatile TimePreferencesManager singleton = null;

    private Context context;


    private String TIME_PREFERENCE_FILE_KEY = "com.mudib.ghostwriter.prefsfile";
    private String DISPLAY_TIME_KEY = "display_time_key";

    private long DISPLAY_TIME_DEFAULT = 5000;

    public static TimePreferencesManager with(Context context) {
        if (singleton == null) synchronized (TimePreferencesManager.class) {
            if (singleton == null) {
                singleton = new TimePreferencesManager(context);
            }
        }
        return singleton;
    }


    protected TimePreferencesManager(Context context){
        this.context = context;
    }

    public  long getImageDisplayTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getLong(DISPLAY_TIME_KEY,DISPLAY_TIME_DEFAULT);

    }

    public  void saveImageDisplayTime(long displaytime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putLong(DISPLAY_TIME_KEY, displaytime);
        preferencesEditor.apply();
    }
}
