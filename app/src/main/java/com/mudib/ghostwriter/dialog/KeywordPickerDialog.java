package com.mudib.ghostwriter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.mudib.ghostwriter.Interface.KeywordPickerDialogInterface;
import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.views.ContactsCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diana on 17/02/2018.
 */

public class KeywordPickerDialog extends BaseDialogFragment{


    private KeywordListAdapter keywordListAdapter;
    private List<Keyword> keywords = new ArrayList<>();

    @BindView(R.id.searchView)
    ContactsCompletionView completionView;

    @BindView(R.id.listview_keyword)
    ListView listViewKeyword;

    @OnClick(R.id.button_ok)
    void onOkClicked() {
        dismiss();
        if (completionView.getObjects().size()>0) {
            try {
                ((KeywordPickerDialogInterface) getActivity()).onSelectedKeywords(completionView.getObjects());
            } catch (ClassCastException cce) {
                throw new ClassCastException("DateTimePickerListener getTargetFragment is not set");
            }
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClicked() {
        dismiss();
    }

    public static final String TAG = KeywordPickerDialog.class.getCanonicalName();

    public static KeywordPickerDialog newInstance() {
        return new KeywordPickerDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_keywords);
        mUnbinder = ButterKnife.bind(this, dialog.getWindow().getDecorView());
        initViews();
        return dialog;
    }

    private void initViews(){
        for (String word: Constant.allSearchkeys){
            Keyword keyword = new Keyword(word);
            keywords.add(keyword);
        }
        keywordListAdapter = new KeywordListAdapter(getContext(), new KeywordListAdapter.KeywordListAdapterListener() {
            @Override
            public void onCheckedKeyword(Keyword keyword) {
                completionView.addObject(keyword);
//                keywords.add(keyword);
            }

            @Override
            public void onUnCheckedKeyword(Keyword keyword) {
//                keywords.add(keyword);
                completionView.removeObject(keyword);

            }
        });

        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
        completionView.setTokenListener(new TokenCompleteTextView.TokenListener<Keyword>() {
            @Override
            public void onTokenAdded(Keyword token) {

            }

            @Override
            public void onTokenRemoved(Keyword token) {
                Keyword tokenKeyword = (Keyword)token;
                for(int i=0;i<keywords.size();i++){
                    Keyword keyword = keywords.get(i);
                    if(keyword.getWord().equalsIgnoreCase(tokenKeyword.getWord())){
                        keyword.setSelected(false);
                        keywords.set(i,keyword);
                    }
                }
                keywordListAdapter.setItems(keywords);
                listViewKeyword.setAdapter(keywordListAdapter);

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
