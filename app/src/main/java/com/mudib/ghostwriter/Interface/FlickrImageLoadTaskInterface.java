package com.mudib.ghostwriter.Interface;

import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.models.SearchResultFlickr;

import java.util.List;

/**
 * Created by jordi on 20/02/2018.
 */

public interface FlickrImageLoadTaskInterface {
    void onGetFlickrImageList(List<SearchResultFlickr> searchResultFlickrs);
}
