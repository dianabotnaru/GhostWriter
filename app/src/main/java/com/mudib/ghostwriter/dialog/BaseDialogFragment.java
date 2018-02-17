package com.mudib.ghostwriter.dialog;

import android.support.v4.app.DialogFragment;

import butterknife.Unbinder;

/**
 * Created by jordi on 17/02/2018.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    protected Unbinder mUnbinder;

    public BaseDialogFragment() {
        //required empty constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
