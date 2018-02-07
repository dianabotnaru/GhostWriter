package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Bundle;

import com.mudib.ghostwriter.R;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick(R.id.start_btn)
    public void onStartClick() {
        startActivity(new Intent(this, ImageDisplayActivity.class));
    }

    @OnClick(R.id.setting_btn)
    public void onSettingClick() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
