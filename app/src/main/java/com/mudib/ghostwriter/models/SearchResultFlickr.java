package com.mudib.ghostwriter.models;

import java.io.Serializable;

/**
 * Created by diana on 20/02/2018.
 */

public class SearchResultFlickr implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String date;
    private String owner;
    private String url;
    private String keyword;

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    public String getOwner(){
        return owner;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }

    public String getUrl(){
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
