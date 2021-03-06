package com.mudib.ghostwriter.manager;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 * Created by diana on 09/02/2018.
 */

public class ApiClient {

    private static final String GOOGLE_SEARCH_BASE_URL = "https://www.googleapis.com/customsearch/v1?";
//    private static final String GOOGLE_API_KEY = "AIzaSyCqRZgz6YJJwCclmfNX__L74mpLf_LFiOQ";
    private static final String GOOGLE_API_KEY = "AIzaSyB7s_qatVZUJosR2ENEL9f_mFSyztNAnyk";

    private static final String GOOGLE_SE_ID = "013079244253699854793:seggni-nrhs";

    private static final String SYNONMS_SEARCH_BASE_URL = "https://api.datamuse.com/words?rel_syn=";


    static volatile ApiClient singleton = null;

    private AsyncHttpClient client;

    private Context context;

    public static ApiClient with(Context context) {
        if (singleton == null) synchronized (ApiClient.class) {
            if (singleton == null) {
                singleton = new ApiClient(context);
            }
        }
        return singleton;
    }

    protected ApiClient(Context context){
        this.context = context;
        this.client = new AsyncHttpClient();
    }

    public void startGoogleSearchAsync(String textToSearch, int startPage, int num,JsonHttpResponseHandler handler) {
        String url = GOOGLE_SEARCH_BASE_URL +"key="+GOOGLE_API_KEY+"&cx="+GOOGLE_SE_ID+"&searchType=image"+"&q="+textToSearch+"&num="+num+"&start="+startPage;
        client.get(url, handler);
    }

    public void startGetSynonymsAsync(String word, JsonHttpResponseHandler handler) {
//        String url = GOOGLE_SEARCH_BASE_URL +"key="+GOOGLE_API_KEY+"&cx="+GOOGLE_SE_ID+"&searchType=image"+"&q="+word;
        String url = SYNONMS_SEARCH_BASE_URL +word;
        client.get(url, handler);
    }

}
