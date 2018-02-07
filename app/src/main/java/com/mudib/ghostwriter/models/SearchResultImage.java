package com.mudib.ghostwriter.models;

import java.io.Serializable;

/**
 * Created by diana on 07/02/2018.
 */

public class SearchResultImage implements Serializable {

    private String contextLink;
    private int height;
    private int width;
    private int byteSize;
    private String thumbnailLink;
    private int thumbnailHeight;
    private int thumbnailWidth;

    public String getContextLink() {
        return contextLink;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getByteSize() {
        return byteSize;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public int getThumbnailHeight() {
        return thumbnailHeight;
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }
}

