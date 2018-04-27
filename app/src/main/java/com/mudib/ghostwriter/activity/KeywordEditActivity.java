package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;
import com.mudib.ghostwriter.dialog.AddKeywordDialog;
import com.mudib.ghostwriter.dialog.KeywordPickerDialog;
import com.mudib.ghostwriter.dialog.WarningDialogFragment;
import com.mudib.ghostwriter.manager.TimePreferencesManager;
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jordi on 26/04/2018.
 */

public class KeywordEditActivity extends BaseActivity implements WarningDialogFragment.WarningDialogListener,AddKeywordDialog.AddKeywordDialogListener{

    @BindView(R.id.delete_button_layout)
    LinearLayout layoutDeleteButton;

    @BindView(R.id.keywordListView)
    ListView listViewKeyword;

    private KeywordListAdapter keywordListAdapter;
    private List<Keyword> keywords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keywordedit);
        initToolbar(getResources().getString(R.string.edit_keyword),true);
        initListViewKeyword();
    }

    private void initListViewKeyword(){
        keywords = TimePreferencesManager.with(this).getKeywordList(TimePreferencesManager.DEFAULT_KEYWORD_KEYS);
        if(keywords.size()==0) {
            String[] keyword_array = getResources().getStringArray(R.array.keyword_array);
            for (int i = 0; i < keyword_array.length; i++) {
                Keyword keyword = new Keyword(keyword_array[i]);
                keywords.add(keyword);
            }
        }

        keywordListAdapter = new KeywordListAdapter(this, new KeywordListAdapter.KeywordListAdapterListener() {
            @Override
            public void onCheckedKeyword(Keyword keyword) {
                initDeleteButtonLayout();
            }

            @Override
            public void onUnCheckedKeyword(Keyword keyword) {
                initDeleteButtonLayout();
            }
        });

        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    private void initDeleteButtonLayout(){
        if(keywordListAdapter.getSelectedItems().size()>0){
            layoutDeleteButton.setVisibility(View.VISIBLE);
        }else{
            layoutDeleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.button_add)
    public void onAddButtonClick() {
        AddKeywordDialog dialogFragment = AddKeywordDialog.newInstance();
        dialogFragment.show(getSupportFragmentManager(), AddKeywordDialog.TAG);
    }

    @OnClick(R.id.button_delete)
    public void onDeleteButtonClick() {
        if(keywordListAdapter.getSelectedItems().size()>0) {
            WarningDialogFragment dialogFragment = WarningDialogFragment.newInstance(getResources().getString(R.string.delete_keyword_warning_title), getResources().getString(R.string.delete_keyword_warning_message), false);
            dialogFragment.show(getSupportFragmentManager(), WarningDialogFragment.TAG);
        }
    }

    @OnClick(R.id.button_default)
    public void onDefaultButtonClick() {
        keywords = new ArrayList<>();
        String[] keyword_array = getResources().getStringArray(R.array.keyword_array);
        for (int i = 0; i < keyword_array.length; i++) {
            Keyword keyword = new Keyword(keyword_array[i]);
            keywords.add(keyword);
        }
        keywordListAdapter.setItems(this.keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
        TimePreferencesManager.with(this).saveKeyword(this.keywords,TimePreferencesManager.DEFAULT_KEYWORD_KEYS);
    }


    @Override
    public void onDialogOkButtonClicked(){
        deleteSelectedKeywords();
        layoutDeleteButton.setVisibility(View.GONE);
    }

    @Override
    public void onAddKeywordDialogOkButtonClicked(List<Keyword> keywords){
        for(Keyword keyword:keywords){
            this.keywords.add(keyword);
        }
        sortKeywordList();
        keywordListAdapter.setItems(this.keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
        TimePreferencesManager.with(this).saveKeyword(this.keywords,TimePreferencesManager.DEFAULT_KEYWORD_KEYS);
    }

    private void deleteSelectedKeywords(){
        keywords = new ArrayList<>();
        keywords = keywordListAdapter.getUnSelectedItems();
        sortKeywordList();
        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
        TimePreferencesManager.with(this).saveKeyword(keywords,TimePreferencesManager.DEFAULT_KEYWORD_KEYS);
    }

    private void sortKeywordList(){
        Collections.sort(keywords, new Comparator<Keyword>() {
            @Override
            public int compare(Keyword s1, Keyword s2) {
                return s1.getEnWord().compareToIgnoreCase(s2.getEnWord());
            }
        });

    }
}
