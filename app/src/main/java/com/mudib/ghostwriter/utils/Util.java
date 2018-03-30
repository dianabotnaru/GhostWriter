package com.mudib.ghostwriter.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

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
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

}
