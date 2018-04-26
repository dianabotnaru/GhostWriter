package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;
import com.mudib.ghostwriter.dialog.AddKeywordDialog;
import com.mudib.ghostwriter.dialog.KeywordPickerDialog;
import com.mudib.ghostwriter.dialog.WarningDialogFragment;
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jordi on 26/04/2018.
 */

public class KeywordEditActivity extends BaseActivity implements WarningDialogFragment.WarningDialogListener{

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
        String[] keyword_array = getResources().getStringArray(R.array.keyword_array);
        for (int i=0; i<keyword_array.length; i++){
            Keyword keyword = new Keyword(keyword_array[i]);
            keywords.add(keyword);
        }

        keywordListAdapter = new KeywordListAdapter(this, new KeywordListAdapter.KeywordListAdapterListener() {
            @Override
            public void onCheckedKeyword(Keyword keyword) {

            }

            @Override
            public void onUnCheckedKeyword(Keyword keyword) {
            }
        });

        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
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
        if(keywordListAdapter.getUnSelectedItems().size()>0) {
            WarningDialogFragment dialogFragment = WarningDialogFragment.newInstance(getResources().getString(R.string.delete_keyword_warning_title), getResources().getString(R.string.delete_keyword_warning_message), false);
            dialogFragment.show(getSupportFragmentManager(), WarningDialogFragment.TAG);
        }
    }

    @Override
    public void onDialogOkButtonClicked(){
        deleteSelectedKeywords();
    }

    private void deleteSelectedKeywords(){
        keywords = new ArrayList<>();
        keywords = keywordListAdapter.getUnSelectedItems();
        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }
}
