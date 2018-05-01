package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;
import com.mudib.ghostwriter.dialog.AddKeywordDialog;
import com.mudib.ghostwriter.dialog.WarningDialogFragment;
import com.mudib.ghostwriter.manager.SharedPreferencesManager;
import com.mudib.ghostwriter.models.FireBaseData;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.utils.DataCovertUtil;
import com.mudib.ghostwriter.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.mudib.ghostwriter.utils.DataCovertUtil.getCurrentTimeStringforID;

/**
 * Created by diana on 26/04/2018.
 */

public class KeywordEditActivity extends BaseActivity implements WarningDialogFragment.WarningDialogListener,AddKeywordDialog.AddKeywordDialogListener{

    @BindView(R.id.delete_button_layout)
    LinearLayout layoutDeleteButton;

    @BindView(R.id.keywordListView)
    ListView listViewKeyword;

    private KeywordListAdapter keywordListAdapter;
    private List<Keyword> keywords = new ArrayList<>();

    private DatabaseReference mDatabase;

    private boolean isDeleteDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keywordedit);
        initToolbar(getResources().getString(R.string.edit_keyword),true);
        initListViewKeyword();
    }

    private void initListViewKeyword(){
        keywords = SharedPreferencesManager.with(this).getAppKeywordList();
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
        isDeleteDialog = true;
        if(keywordListAdapter.getSelectedItems().size()>0) {
            WarningDialogFragment dialogFragment = WarningDialogFragment.newInstance(getResources().getString(R.string.delete_keyword_warning_title), getResources().getString(R.string.delete_keyword_warning_message), false);
            dialogFragment.show(getSupportFragmentManager(), WarningDialogFragment.TAG);
        }
    }

    @OnClick(R.id.button_default)
    public void onDefaultButtonClick() {
        isDeleteDialog = false;
        WarningDialogFragment dialogFragment = WarningDialogFragment.newInstance(getResources().getString(R.string.default_keyword_warning_title), getResources().getString(R.string.default_keyword_warning_message), false);
        dialogFragment.show(getSupportFragmentManager(), WarningDialogFragment.TAG);
    }


    @Override
    public void onDialogOkButtonClicked(){
        if(isDeleteDialog) {
            deleteSelectedKeywords();
            layoutDeleteButton.setVisibility(View.GONE);
        }else{
            setDefaultKeywords();
        }
    }

    @Override
    public void onAddKeywordDialogOkButtonClicked(List<Keyword> keywords){
        for(Keyword keyword:keywords){
            this.keywords.add(keyword);
        }
        sortKeywordList();
        keywordListAdapter.setItems(this.keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    private void setDefaultKeywords(){
        keywords = Util.getDefaultKeywordList(this);
        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    private void deleteSelectedKeywords(){
        keywords = new ArrayList<>();
        keywords = keywordListAdapter.getUnSelectedItems();
        sortKeywordList();
        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    private void sortKeywordList(){
        Collections.sort(keywords, new Comparator<Keyword>() {
            @Override
            public int compare(Keyword s1, Keyword s2) {
                return s1.getEnWord().compareToIgnoreCase(s2.getEnWord());
            }
        });
    }

    @Override
    protected void onDestroy() {
        for(int i=0;i<keywords.size();i++){
            Keyword keyword = keywords.get(i);
            keyword.setSelected(false);
            keywords.set(i,keyword);
        }
        SharedPreferencesManager.with(this).saveAppKeyword(keywords);
        saveKeywordsOnFirebase();
        super.onDestroy();
    }

    private void saveKeywordsOnFirebase(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            String keywordString = DataCovertUtil.getKeywordStrings(keywords);
            String localeString = SharedPreferencesManager.with(this).getKeywordLangauge();
            String currentTime = DataCovertUtil.getCurrentTimeString();
            FireBaseData fireBaseData = new FireBaseData(localeString,keywordString,currentTime);
            mDatabase.child("AppKeywords").child(user.getUid()).child(getCurrentTimeStringforID()).setValue(fireBaseData);
        }
    }
}
