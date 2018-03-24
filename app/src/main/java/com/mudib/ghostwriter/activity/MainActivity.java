package com.mudib.ghostwriter.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.manager.SearchImagesCacheManager;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            getSupportActionBar().hide();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, ImageDisplayActivity.class));
                finish();
            }
        }, 3000);

//        SearchImagesCacheManager.with().startFetchFlickrImages();
    }

//    @OnClick(R.id.start_btn)
//    public void onStartClick() {
//        startActivity(new Intent(this, ImageDisplayActivity.class));
//    }
//
//    @OnClick(R.id.setting_btn)
//    public void onSettingClick() {
//        startActivity(new Intent(this, SettingActivity.class));
//    }
}
