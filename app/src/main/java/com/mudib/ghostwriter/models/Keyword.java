package com.mudib.ghostwriter.models;

import android.content.Context;

import com.mudib.ghostwriter.constant.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diana on 17/02/2018.
 */

public class Keyword {

    private String word;

    private boolean isSelected;

    private int page;
    private int perPage;

    private List<SearchResultFlickr> searchResultFlickrs;

    public Keyword(String word){
        this.word = word;
        this.page = Constant.defaultPage;
        this.perPage=Constant.detfaultPerPage;
        isSelected = false;
        searchResultFlickrs = new ArrayList<SearchResultFlickr>();
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
}
