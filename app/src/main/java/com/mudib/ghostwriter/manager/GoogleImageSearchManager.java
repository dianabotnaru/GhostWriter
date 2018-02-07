package com.mudib.ghostwriter.manager;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by diana on 07/02/2018.
 */

public class GoogleImageSearchManager {

    private static final String BASE_URL = "https://www.googleapis.com/customsearch/v1?";
    private static final String API_KEY = "AIzaSyCqRZgz6YJJwCclmfNX__L74mpLf_LFiOQ";
    private static final String SE_ID = "009423356643998779857:dn_ippdxhnw";

    static volatile GoogleImageSearchManager singleton = null;

    private AsyncHttpClient client;

    private Context context;

    public static GoogleImageSearchManager with(Context context) {
        if (singleton == null) synchronized (GoogleImageSearchManager.class) {
            if (singleton == null) {
                singleton = new GoogleImageSearchManager(context);
            }
        }
        return singleton;
    }


    protected GoogleImageSearchManager(Context context){
        this.context = context;
        this.client = new AsyncHttpClient();
    }

    public void startSearchAsync(final String textToSearch, int startPage, JsonHttpResponseHandler handler) {
        String url = BASE_URL +"key="+API_KEY+"&cx="+SE_ID+"&searchType=image"+"&q="+textToSearch;
        client.get(url, handler);
    }

}
