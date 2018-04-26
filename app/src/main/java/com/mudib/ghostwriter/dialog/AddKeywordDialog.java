package com.mudib.ghostwriter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.mudib.ghostwriter.R;

import butterknife.ButterKnife;

/**
 * Created by jordi on 26/04/2018.
 */

public class AddKeywordDialog extends BaseDialogFragment{
    public static final String TAG = AddKeywordDialog.class.getCanonicalName();

    public AddKeywordDialog() {
    }

    public static AddKeywordDialog newInstance() {
        Bundle arguments = new Bundle();
        AddKeywordDialog dialog = new AddKeywordDialog();
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_addkeyword);
        mUnbinder = ButterKnife.bind(this, dialog.getWindow().getDecorView());
        initViews();
        return dialog;
    }

    private void initViews(){
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
