package com.mudib.ghostwriter.Interface;

import com.mudib.ghostwriter.models.Keyword;

import java.util.List;

/**
 * Created by diana on 17/02/2018.
 */

public interface KeywordPickerDialogInterface {
    void onSelectedKeywords(List<Keyword> keywords);
    void onEditKeywordClicked();
}
