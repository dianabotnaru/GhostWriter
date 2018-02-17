package com.mudib.ghostwriter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diana on 17/02/2018.
 */

public class KeywordPickerDialog extends BaseDialogFragment{

    private KeywordListAdapter keywordListAdapter;

    @BindView(R.id.listview_keyword)
    ListView listViewKeyword;

    @OnClick(R.id.button_ok)
    void onOkClicked() {
        dismiss();
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
        keywordListAdapter = new KeywordListAdapter(getContext());
        listViewKeyword.setAdapter(keywordListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
