package com.mudib.ghostwriter.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 07/02/2018.
 */

public class SharedPreferencesManager {

    public boolean isChangedLocale = false;
    public boolean isFirstLaunch = true;

    static volatile SharedPreferencesManager singleton = null;

    private Context context;


    public static String TIME_PREFERENCE_FILE_KEY = "com.mudib.ghostwriter.prefsfile";
    public static String DISPLAY_TIME_KEY = "display_time_key";
    public static String DISPLAY_TRANSFORM_KEY = "display_transform_key";
    public static String KEYWORD_LANGUAGE_KEY = "keyword_language_key";

    public static String SEARCH_KEYWORD_KEYS = "search_keyword_key";

    public static String APP_KEYWORD_KEYS = "app_keyword_key";

    private long DISPLAY_TIME_DEFAULT = 5000;

    public static SharedPreferencesManager with(Context context) {
        if (singleton == null) synchronized (SharedPreferencesManager.class) {
            if (singleton == null) {
                singleton = new SharedPreferencesManager(context);
            }
        }
        return singleton;
    }


    protected SharedPreferencesManager(Context context){
        this.context = context;
    }

    //Image Displaytime
    public long getImageDisplayTime() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getLong(DISPLAY_TIME_KEY,DISPLAY_TIME_DEFAULT);

    }

    public void saveImageDisplayTime(long displaytime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putLong(DISPLAY_TIME_KEY, displaytime);
        preferencesEditor.apply();
    }

    //Image Displaytransfer
    public String getImageDisplayTransformer() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(DISPLAY_TRANSFORM_KEY, SliderLayout.Transformer.Accordion.toString());
    }

    public void saveImageDisplayTransformer(String transform) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(DISPLAY_TRANSFORM_KEY, transform);
        preferencesEditor.apply();
    }

    //app Locale

    public String getKeywordLangauge() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEYWORD_LANGUAGE_KEY, "English");
    }

    public void saveKeywordLangauge(String keywordLanguage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putString(KEYWORD_LANGUAGE_KEY, keywordLanguage);
        preferencesEditor.apply();
    }

    //app Keyword Save
    public ArrayList<Keyword> getAppKeywordList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String keywordGson = sharedPreferences.getString(APP_KEYWORD_KEYS, "");
        if(isJSONValid(keywordGson)) {
            Gson gson = new Gson();
            Keyword[] keywords = gson.fromJson(keywordGson, Keyword[].class);
            if(keywords == null){
                return Util.getDefaultKeywordList(context);
            }else {
                return Util.getArrayFromList(keywords);
            }
        }else{
            return Util.getDefaultKeywordList(context);
        }
    }

    public void saveAppKeyword(List<Keyword> keywords) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(keywords);
        preferencesEditor.putString(APP_KEYWORD_KEYS, json);
        preferencesEditor.apply();
    }

    //search keyword save
    public ArrayList<Keyword> getSearchKeywordList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String keywordGson = sharedPreferences.getString(SEARCH_KEYWORD_KEYS, "");
        if(isJSONValid(keywordGson)){
            Gson gson = new Gson();
            Keyword[] keywords = gson.fromJson(keywordGson, Keyword[].class);
            if(keywords == null){
                return new ArrayList<>();
            }else {
                return Util.getArrayFromList(keywords);
            }
        }else{
            return new ArrayList<>();
        }
    }

    public void saveSearchKeyword(List<Keyword> keywords) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TIME_PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(keywords);
        preferencesEditor.putString(SEARCH_KEYWORD_KEYS, json);
        preferencesEditor.apply();
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


}
