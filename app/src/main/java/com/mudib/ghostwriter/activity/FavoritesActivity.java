package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.adapter.FavoriteImageAdapter;
import com.mudib.ghostwriter.manager.SharedPreferencesManager;

import butterknife.BindView;

public class FavoritesActivity extends BaseActivity {

    @BindView(R.id.gridview)
    GridView gridView;

    private FavoriteImageAdapter favoriteImageAdapter;
    private String[] favoriteUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initToolbar(getResources().getString(R.string.favorite),true);
        initUi();
    }

    public void initUi(){
        favoriteUrlList = SharedPreferencesManager.with(this).getFavoriteUrlList();
        favoriteImageAdapter = new FavoriteImageAdapter(this);
        favoriteImageAdapter.setItems(favoriteUrlList);
        gridView.setAdapter(favoriteImageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
