package com.mudib.ghostwriter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mudib.ghostwriter.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diana on 28/10/16.
 */

public class WarningDialogFragment extends BaseDialogFragment {

    private static String KEY_TITLE_STRING = "title";
    private static String KEY_DESCRIPTION_STRING = "description";
    private static String KEY_HIDDEN_CANCEL = "hidden_cancel_button";

    private String mTitleString;
    private String mDescriptionString;
    private boolean mIsHiddenCancelButton;

    @BindView(R.id.button_cancel)
    Button mCancelButton;

    @BindView(R.id.text_dialog_title)
    TextView mDialogTitleText;

    @BindView(R.id.text_dialog_description)
    TextView mDialogDescriptionText;

    public interface WarningDialogListener {
        void onDialogOkButtonClicked();
    }

    @OnClick(R.id.button_ok)
    void onOkClicked() {
        dismiss();
        try{
            ((WarningDialogListener) getActivity()).onDialogOkButtonClicked();
        }catch (ClassCastException cce){
            throw new ClassCastException("ScanNearbyDialogListener getTargetFragment is not set");
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClicked() {
        dismiss();
    }

    public static final String TAG = WarningDialogFragment.class.getCanonicalName();

    public WarningDialogFragment() {
    }

    public static WarningDialogFragment newInstance(String title, String description, boolean isHiddenCancel) {
        Bundle arguments = new Bundle();
        arguments.putString(KEY_TITLE_STRING, title);
        arguments.putString(KEY_DESCRIPTION_STRING, description);
        arguments.putBoolean(KEY_HIDDEN_CANCEL, isHiddenCancel);

        WarningDialogFragment dialog = new WarningDialogFragment();
        dialog.setArguments(arguments);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitleString = arguments.getString(KEY_TITLE_STRING);
        mDescriptionString = arguments.getString(KEY_DESCRIPTION_STRING);
        mDescriptionString = arguments.getString(KEY_DESCRIPTION_STRING);
        mIsHiddenCancelButton = arguments.getBoolean(KEY_HIDDEN_CANCEL);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_warning);
        mUnbinder = ButterKnife.bind(this, dialog.getWindow().getDecorView());
        initViews();
        return dialog;
    }

    private void initViews(){
        mDialogTitleText.setText(mTitleString);
        mDialogDescriptionText.setText(mDescriptionString);
        if (mIsHiddenCancelButton){
            mCancelButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
