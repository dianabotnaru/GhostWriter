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
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diana on 17/02/2018.
 */

public class KeywordPickerDialog extends BaseDialogFragment{

    public static String[] allSearchkeys = {"Alpha", "Omega", "Anchor", "Angel","Ant","Apple","Baby","Beehive","Bird","Bread","Bull", "Camel", "Candle", "Cauldron","Chameleon","Compass","Cornucopia","Crocodile","Dolphin","Elephant"
            ,"Globe", "Griffin", "Helmet", "Horse","Hourglass","Lute","Madonna","Marionette","Moon", "Owl", "Serpent", "Sun","Sword","Thunderbolt","Tree","Walled Garden","Wild Man"};

    private KeywordListAdapter keywordListAdapter;
    private List<Keyword> keywords = new ArrayList<>();

    @BindView(R.id.listview_keyword)
    ListView listViewKeyword;

    @OnClick(R.id.button_ok)
    void onOkClicked() {
        dismiss();
        if (keywordListAdapter.getSelectedItems().size()>0) {
            try {
                ((KeywordPickerDialogInterface) getActivity()).onSelectedKeywords(keywordListAdapter.getSelectedItems());
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
        for (String word:allSearchkeys){
            Keyword keyword = new Keyword(word);
            keywords.add(keyword);
        }
        keywordListAdapter = new KeywordListAdapter(getContext());
        keywordListAdapter.setItems(keywords);
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
