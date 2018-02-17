package com.mudib.ghostwriter.models;

/**
 * Created by diana on 17/02/2018.
 */

public class Keyword {

    private String word;

    private boolean isSelected;

    public Keyword(String word){
        this.word = word;
        isSelected = false;
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
}
