package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.mudib.ghostwriter.R;

/**
 * Created by diana on 06/02/2018.
 */

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar("Setting");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
