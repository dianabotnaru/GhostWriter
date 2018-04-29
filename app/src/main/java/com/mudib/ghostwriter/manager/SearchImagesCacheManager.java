package com.mudib.ghostwriter.manager;

import android.content.Context;
import android.util.Log;

import com.mudib.ghostwriter.Interface.FlickrImageLoadTaskInterface;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.models.SearchResultFlickr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 21/02/2018.
 */

public class SearchImagesCacheManager {

    static volatile SearchImagesCacheManager singleton = null;

    private ArrayList<Keyword> allSearchKeyList;
    private int currentSearchIndex;
    private Context context;


    public static SearchImagesCacheManager with(Context context) {
        if (singleton == null) synchronized (SearchImagesCacheManager.class) {
            if (singleton == null) {
                singleton = new SearchImagesCacheManager(context);
            }
        }
        return singleton;
    }

    protected SearchImagesCacheManager(Context context){
        this.context = context;
        allSearchKeyList = new ArrayList<Keyword>();
        allSearchKeyList = TimePreferencesManager.with(context).getKeywordList(TimePreferencesManager.DEFAULT_KEYWORD_KEYS);
        currentSearchIndex = 0;
    }

    public void setSearchImages(String word, List<SearchResultFlickr>searchResultFlickrs){
        for(int i=0; i<allSearchKeyList.size();i++){
            Keyword keyword = allSearchKeyList.get(i);
            if(keyword.getWord().equalsIgnoreCase(word)){
                if(keyword.getSearchResultFlickrs().size() == 0) {
                    keyword.setSearchResultFlickrs(searchResultFlickrs);
                    allSearchKeyList.add(i, keyword);
                }
            }
        }
    }

    public List<SearchResultFlickr> getCachedSearchImages(String word){
        for(Keyword keyword:allSearchKeyList){
            if(keyword.getWord().equalsIgnoreCase(word)){
                return keyword.getSearchResultFlickrs();
            }
        }
        return new ArrayList<SearchResultFlickr>();
    }

    public void startFetchFlickrImages(){
        if(currentSearchIndex<allSearchKeyList.size()) {
            Keyword keyword = allSearchKeyList.get(currentSearchIndex);
            if (keyword.getSearchResultFlickrs().size() == 0) {
                fetchFlickrImages(keyword);
            } else {
                nextKeywordFetch();
            }
        }else{
            Log.d("completed","dowload actions");

        }
    }

    public void fetchFlickrImages(final Keyword keyword){
        FlickrImageLoadTask task = new FlickrImageLoadTask();
        task.setFlickrImageLoadTaskInterface(new FlickrImageLoadTaskInterface() {
            @Override
            public void onGetFlickrImageList(List<SearchResultFlickr> searchResultFlickrs) {
                keyword.setSearchResultFlickrs(searchResultFlickrs);
                allSearchKeyList.set(currentSearchIndex,keyword);
                nextKeywordFetch();
            }

            @Override
            public void onFailedGetFlickrImageList(String message) {
                nextKeywordFetch();
            }
        });
        task.execute(new String[]{keyword.getWord(), String.valueOf(keyword.getPerPage()), String.valueOf(keyword.getPage())});
    }

    private void nextKeywordFetch(){
        currentSearchIndex++;
        startFetchFlickrImages();
    }
}
