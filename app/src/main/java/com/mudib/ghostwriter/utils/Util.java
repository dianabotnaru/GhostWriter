package com.mudib.ghostwriter.utils;

import com.mudib.ghostwriter.models.SearchResultFlickr;

import java.util.ArrayList;

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

    public static ArrayList<SearchResultFlickr> clusterArraryList(ArrayList<SearchResultFlickr> orginalList,int perpage,int searchListCount){
        ArrayList<SearchResultFlickr> clusterList = new ArrayList<SearchResultFlickr>();
        if(searchListCount > 1){
            if(perpage<6) {
                return orginalList;
            }else {
                int sectionCount =  perpage/3;
                for(int k = 0; k<sectionCount; k++){
                    for (int i = 0; i < searchListCount; i++) {
                        for (int j = 0; j < 3; j++) {
                            SearchResultFlickr searchResultFlickr = orginalList.get(i * perpage + j+ 3*k);
                            clusterList.add(searchResultFlickr);
                        }
                    }
                }
                return clusterList;
            }
        }else {
            return orginalList;
        }
    }
}
