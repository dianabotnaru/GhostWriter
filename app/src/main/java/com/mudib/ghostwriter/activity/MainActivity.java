package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.manager.SharedPreferencesManager;
import com.mudib.ghostwriter.utils.Util;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends BaseActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, Constant.ADMOB_APP_ID);
        mAuth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            getSupportActionBar().hide();
        }
        setLocale();
        signInFirebaseUser();
    }

    private void setLocale(){
        Util.setLocale(getApplicationContext(), SharedPreferencesManager.with(this).getKeywordLangauge());
    }

    private void signInFirebaseUser(){
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            launchImageDisplayActivity(2000);
        } else {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            launchImageDisplayActivity(1000);
                        }
                    });
        }
    }

    private void launchImageDisplayActivity(long delaytime){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, ImageDisplayActivity.class));
                finish();
            }
        }, delaytime);
    }
}
