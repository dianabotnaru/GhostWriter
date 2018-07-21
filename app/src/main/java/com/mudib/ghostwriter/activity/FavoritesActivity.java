package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;

import com.mudib.ghostwriter.R;

public class FavoritesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initToolbar(getResources().getString(R.string.favorite),true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
