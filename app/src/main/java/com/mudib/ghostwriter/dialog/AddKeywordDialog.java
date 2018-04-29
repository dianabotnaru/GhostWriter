package com.mudib.ghostwriter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.Window;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.views.ContactsCompletionView;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diana on 26/04/2018.
 */

public class AddKeywordDialog extends BaseDialogFragment implements TokenCompleteTextView.TokenListener{

    public static final String TAG = AddKeywordDialog.class.getCanonicalName();

    private boolean isEditableAdded;
    public interface AddKeywordDialogListener {
        void onAddKeywordDialogOkButtonClicked(List<Keyword> keywords);
    }

    @BindView(R.id.searchView)
    ContactsCompletionView completionView;

    @OnClick(R.id.button_ok)
    void onOkClicked() {
        Editable editable = completionView.getText();
        String editableString = editable.toString();
        editableString = editableString.replace(",", "");
        editableString = editableString.replace(" ", "");
        if(editableString.equalsIgnoreCase("")){
            dismiss();
            try{
                ((AddKeywordDialog.AddKeywordDialogListener) getActivity()).onAddKeywordDialogOkButtonClicked(completionView.getObjects());
            }catch (ClassCastException cce){
                throw new ClassCastException("ScanNearbyDialogListener getTargetFragment is not set");
            }
        }else{
            Keyword keyword = new Keyword(getContext(),editableString);
            isEditableAdded = true;
            completionView.addObject(keyword);
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClicked() {
        dismiss();
    }

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
        isEditableAdded = false;
        initViews();
        return dialog;
    }

    private void initViews(){
        completionView.setTokenListener(this);
        char[] splitChar = {',', ';', ' '};
        completionView.setSplitChar(splitChar);
        completionView.setThreshold(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onTokenAdded(Object token) {
        if(isEditableAdded){
            dismiss();
            try{
                ((AddKeywordDialog.AddKeywordDialogListener) getActivity()).onAddKeywordDialogOkButtonClicked(completionView.getObjects());
            }catch (ClassCastException cce){
                throw new ClassCastException("ScanNearbyDialogListener getTargetFragment is not set");
            }
        }
    }

    @Override
    public void onTokenRemoved(Object token) {
    }
}
