package com.mudib.ghostwriter.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jordi on 30/04/2018.
 */
@IgnoreExtraProperties
public class FireBaseData {

    public String locale;
    public String appKeywords;
    public String createdOn;

    public FireBaseData(){
    }

    public FireBaseData(String locale,String appKeywords,String createdOn){
        this.locale = locale;
        this.appKeywords = appKeywords;
        this.createdOn = createdOn;
    }
}

