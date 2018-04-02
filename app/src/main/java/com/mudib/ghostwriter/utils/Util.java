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
        }else if(lang.equalsIgnoreCase("Nepali") ){
            locale = new Locale("ne","IN");
        }else if(lang.equalsIgnoreCase("Filipino") ){
            locale = new Locale("fil","PH");
        }else if(lang.equalsIgnoreCase("Aze1rbaijani") ){
            locale = new Locale("az","AZ");
        }else if(lang.equalsIgnoreCase("French") ){
            locale = new Locale("fr");
        }else if(lang.equalsIgnoreCase("Spanish") ){
            locale = new Locale("es","MX");
        }else if(lang.equalsIgnoreCase("German") ){
            locale = new Locale("de","DE");
        }else if(lang.equalsIgnoreCase("Portugese") ){
            locale = new Locale("pt","BR");
        }else if(lang.equalsIgnoreCase("Africaans") ){
            locale = new Locale("af","ZA");
        }else if(lang.equalsIgnoreCase("Amharic") ){
            locale = new Locale("am","ET");
        }else if(lang.equalsIgnoreCase("Sudanese") ){
            locale = new Locale("su");
        }else if(lang.equalsIgnoreCase("Polish") ){
            locale = new Locale("pl","PL");
        }else if(lang.equalsIgnoreCase("Turkish") ){
            locale = new Locale("tr","TR");
        }else if(lang.equalsIgnoreCase("Italian") ){
            locale = new Locale("it","IT");
        }else if(lang.equalsIgnoreCase("Danish") ){
            locale = new Locale("da","DK");
        }else if(lang.equalsIgnoreCase("Dutch") ){
            locale = new Locale("nl","NL");
        }else if(lang.equalsIgnoreCase("Czech") ){
            locale = new Locale("cs","CZ");
        }else if(lang.equalsIgnoreCase("Chineese") ){
            locale = new Locale("zh","CN");
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
