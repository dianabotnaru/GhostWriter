package com.mudib.ghostwriter.models;

import java.io.Serializable;

/**
 * Created by diana on 09/02/2018.
 */

public class SynonymWord implements Serializable {

    private String word;
    private int score;

    public String getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }
}
