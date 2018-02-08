package com.mudib.ghostwriter.models;

import java.io.Serializable;

/**
 * Created by diana on 07/02/2018.
 */

public class SearchResultItem implements Serializable {
    private String kind;
    private String title;
    private String htmlTitle;
    private String link;
    private String displayLink;
    private String snippet;
    private String htmlSnippet;
    private String mime;
    private SearchResultImage image;

    public String getKind() {
        return kind;
    }

    public String getTitle() {
        return title;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public String getLink() {
        return link;
    }

    public String getDisplayLink() {
        return displayLink;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getHtmlSnippet() {
        return htmlSnippet;
    }

    public String getMime() {
        return mime;
    }

    public SearchResultImage getImage() {
        return image;
    }
}
