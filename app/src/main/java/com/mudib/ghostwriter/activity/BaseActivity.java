package com.mudib.ghostwriter.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.constant.Constant;

import butterknife.ButterKnife;

/**
 * Created by diana on 07/02/2018.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog mProgressDialog ;
    public boolean isLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void initToolbar(String title, boolean isDisplayHomeEnabled){
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isDisplayHomeEnabled);
    }

    public void hideToolbar(){
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showToolbar(){
        getSupportActionBar().show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public void showLoading(){
        if(!isLoading) {
            isLoading = true;
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_progress, null));
            dialog.setCancelable(false);
            mProgressDialog = dialog;
            mProgressDialog.show();
        }
    }


    public void dismissLoading(){
        if(mProgressDialog != null) {
            if(isLoading) {
                isLoading = false;
                mProgressDialog.dismiss();
            }
        }
    }

    public void showAlertDialog(String title,String msg){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void setRTLState(){
        Configuration config = getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            Constant.isRTL = true;
        }else{
            Constant.isRTL = false;
        }
    }
}
