package com.mudib.ghostwriter.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.daimajia.slider.library.SliderLayout;

/**
 * Created by diana on 07/02/2018.
 */

public class TimePreferencesManager {


    static volatile TimePreferencesManager singleton = null;

    private Context context;


    private String TIME_PREFERENCE_FILE_KEY = "com.mudib.ghostwriter.prefsfile";
    private String DISPLAY_TIME_KEY = "display_time_key";
    private String DISPLAY_TRANSFORM_KEY = "display_transform_key";
    private String KEYWORD_LANGUAGE_KEY = "keyword_language_key";

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

    public long getImageDisplayTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getLong(DISPLAY_TIME_KEY,DISPLAY_TIME_DEFAULT);

    }

    public String getImageDisplayTransformer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(DISPLAY_TRANSFORM_KEY, SliderLayout.Transformer.Accordion.toString());
    }

    public String getKeywordLangauge() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEYWORD_LANGUAGE_KEY, "English");
    }

    public void saveImageDisplayTime(long displaytime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putLong(DISPLAY_TIME_KEY, displaytime);
        preferencesEditor.apply();
    }

    public void saveImageDisplayTransformer(String transform) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(DISPLAY_TRANSFORM_KEY, transform);
        preferencesEditor.apply();
    }

    public void saveKeywordLangauge(String keywordLanguage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(KEYWORD_LANGUAGE_KEY, keywordLanguage);
        preferencesEditor.apply();
    }

}
