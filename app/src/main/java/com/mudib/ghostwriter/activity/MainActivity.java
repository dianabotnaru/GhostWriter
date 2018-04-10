package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.manager.TimePreferencesManager;
import com.mudib.ghostwriter.utils.Util;

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
                setLocale();
                startActivity(new Intent(MainActivity.this, ImageDisplayActivity.class));
                finish();
            }
        }, 2000);
    }

    private void setLocale(){
        Util.setLocale(getApplicationContext(), TimePreferencesManager.with(this).getKeywordLangauge());
    }
}
