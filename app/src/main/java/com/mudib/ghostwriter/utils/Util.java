package com.mudib.ghostwriter.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.models.SearchResultFlickr;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by diana on 09/02/2018.
 */

public class Util {
    public static String[] initStringsFromList(ArrayList<String> stringList) {
        String[] strings = new String[stringList.size()];
        for(int i = 0; i<stringList.size();i++ ){
            strings[i] = stringList.get(i);
        }
        return strings;
    }

    public static ArrayList<String> initListFromStrings(String[] strings){
        ArrayList<String> stringList = new ArrayList<String>();
        for(int i = 0; i<strings.length;i++ ){
            stringList.add(i,strings[i]);
        }
        return stringList;
    }

    public static void setLocale(Context context,String lang) {
        Locale locale;
        if(lang.equalsIgnoreCase("English") ){
            locale = new Locale("en");
        }else if(lang.equalsIgnoreCase("arbic") ){
            locale = new Locale("ar");
        }else if(lang.equalsIgnoreCase("Hindi") ){
            locale = new Locale("hi","IN");
        }else if(lang.equalsIgnoreCase("Mahratti") ){
            locale = new Locale("mr","IN");
        }else if(lang.equalsIgnoreCase("Urdu") ){
            locale = new Locale("ur","IN");
        }else if(lang.equalsIgnoreCase("Bangla") ){
            locale = new Locale("bn","BD");
        }else{
            locale = new Locale("en");
        }
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(locale);
        } else{
            config.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }

}
