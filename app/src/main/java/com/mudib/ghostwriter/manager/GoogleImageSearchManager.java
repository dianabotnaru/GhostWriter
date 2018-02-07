package com.mudib.ghostwriter.manager;

import android.content.Context;

/**
 * Created by diana on 07/02/2018.
 */

public class GoogleImageSearchManager {

    static volatile GoogleImageSearchManager singleton = null;

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
    }



}
