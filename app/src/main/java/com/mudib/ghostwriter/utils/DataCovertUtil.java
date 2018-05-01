package com.mudib.ghostwriter.utils;

import com.mudib.ghostwriter.models.Keyword;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jordi on 30/04/2018.
 */

public class DataCovertUtil {
    public static String getKeywordStrings(List<Keyword> keywords){
        String keywordString = "";
        for(Keyword keyword:keywords){
            keywordString += keyword.getEnWord()+", ";
        }
        return keywordString;
    }

    public static String getCurrentTimeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static String getCurrentTimeStringforID(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",Locale.US);
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

}
