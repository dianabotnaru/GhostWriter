package com.mudib.ghostwriter.models;

import android.content.Context;
import android.content.res.Resources;

import com.mudib.ghostwriter.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 17/02/2018.
 */

public class Keyword {

    private String enWord;

    private String word;

    private int resourceId;

    private boolean isSelected;

    private int page;
    private int perPage;

    private List<SearchResultFlickr> searchResultFlickrs;

    public Keyword(Context context,String word){
        this.word = word;
        this.enWord = word;
        this.page = Constant.defaultPage;
        this.perPage=Constant.detfaultPerPage;
        this.isSelected = false;
        searchResultFlickrs = new ArrayList<SearchResultFlickr>();
        resourceId = context.getResources().getIdentifier("logo", "drawable",
                context.getPackageName());
    }

    public Keyword(Context context,String word,String enWord){
        this.word = word;
        this.enWord = enWord;
        this.page = Constant.defaultPage;
        this.perPage=Constant.detfaultPerPage;
        this.isSelected = false;
        searchResultFlickrs = new ArrayList<SearchResultFlickr>();
        setResourceIdfromName(context);
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public List<SearchResultFlickr> getSearchResultFlickrs() {
        return searchResultFlickrs;
    }

    public void setSearchResultFlickrs(List<SearchResultFlickr> searchResultFlickrs) {
        this.searchResultFlickrs = searchResultFlickrs;
    }

    public String getEnWord() {
        return enWord;
    }

    public void setEnWord(String enWord) {
        this.enWord = enWord;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setResourceIdfromName(Context context){
        Resources resources = context.getResources();
        if (enWord.equalsIgnoreCase("Alpha&Omega")){
            resourceId = resources.getIdentifier("alpha", "drawable",
                    context.getPackageName());

        }else if (enWord.equalsIgnoreCase("walled garden")){
            resourceId = resources.getIdentifier("garden", "drawable",
                    context.getPackageName());
        }else if (enWord.equalsIgnoreCase("wild man")){
            resourceId = resources.getIdentifier("wildman", "drawable",
                    context.getPackageName());
        }else{
            String identifier = "";
            if (enWord.length() <= 1) {
                identifier = enWord.toLowerCase();
            } else {
                identifier = enWord.substring(0, 1).toLowerCase() + enWord.substring(1);
            }

            resourceId = resources.getIdentifier(identifier, "drawable",
                    context.getPackageName());
            if(resourceId == 0){
                resourceId = resources.getIdentifier("logo", "drawable",
                        context.getPackageName());
            }
        }
    }
}
