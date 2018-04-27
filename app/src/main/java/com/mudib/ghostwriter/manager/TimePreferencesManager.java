package com.mudib.ghostwriter.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.daimajia.slider.library.SliderLayout;
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 07/02/2018.
 */

public class TimePreferencesManager {

    public boolean isChangedLocale = false;
    public boolean isFirstLaunch = true;

    static volatile TimePreferencesManager singleton = null;

    private Context context;


    public static String TIME_PREFERENCE_FILE_KEY = "com.mudib.ghostwriter.prefsfile";
    public static String DISPLAY_TIME_KEY = "display_time_key";
    public static String DISPLAY_TRANSFORM_KEY = "display_transform_key";
    public static String KEYWORD_LANGUAGE_KEY = "keyword_language_key";

    public static String SEARCH_KEYWORD_KEYS = "search_keyword_key";

    public static String DEFAULT_KEYWORD_KEYS = "default_keyword_key";


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

    public ArrayList<Keyword> getKeywordList(String key) {
        ArrayList<Keyword> keywordArray = new ArrayList<Keyword>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String keywords = sharedPreferences.getString(key, "");
        if (!keywords.equalsIgnoreCase("")){
            String[] keywordsList = keywords.split(",");
            for (int i = 0; i < keywordsList.length; i++) {
                Keyword keyword = new Keyword(keywordsList[i]);
                keyword.setEnWord(keywordsList[i]);
                keyword.setWord(keywordsList[i]);
                keywordArray.add(keyword);
            }
        }
        return keywordArray;
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


    public void saveKeyword(List<Keyword> keywords, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keywords.size(); i++) {
//            sb.append(keywords.get(i).getEnWord()).append(",");
             sb.append(keywords.get(i).getWord()).append(",");
        }

        preferencesEditor.putString(key, sb.toString());
        preferencesEditor.apply();
    }
}
