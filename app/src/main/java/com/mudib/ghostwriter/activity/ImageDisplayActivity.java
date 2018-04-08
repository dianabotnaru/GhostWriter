package com.mudib.ghostwriter.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mudib.ghostwriter.Interface.FlickrImageLoadTaskInterface;
import com.mudib.ghostwriter.Interface.KeywordPickerDialogInterface;
import com.mudib.ghostwriter.R;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.dialog.KeywordPickerDialog;
import com.mudib.ghostwriter.dialog.WarningDialogFragment;
import com.mudib.ghostwriter.manager.FlickrImageLoadTask;
import com.mudib.ghostwriter.manager.SearchImagesCacheManager;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import butterknife.OnClick;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.models.SearchResultFlickr;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by diana on 06/02/2018.
 */

public class ImageDisplayActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,KeywordPickerDialogInterface,WarningDialogFragment.WarningDialogListener{

    @BindView(R.id.slider_images)
    SliderLayout imageSlider;

    @BindView(R.id.slider_layout)
    RelativeLayout sliderLayout;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.textView_keyword)
    TextView keywordTextView;

    private ArrayList<Keyword> searchKeyList;

    private int currentSearchIndex;

    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        initDatas();
        initSliderView();
        if(TimePreferencesManager.with(this).isFirstLaunch){
            TimePreferencesManager.with(this).isFirstLaunch = false;
            searchKeyList = TimePreferencesManager.with(this).getKeywordList();
            if(searchKeyList.size() == 0){
                showAlertDialog();
            }else{
                showLoading();
                fetchFlickImage();
            }
        }
        setRTLState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_contact:
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={"mrawappt1@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.putExtra(Intent.EXTRA_CC,"");
                intent.setType("text/html");
                startActivity(Intent.createChooser(intent, ""));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item, menu);
        return true;
    }

    private void initDatas(){
        currentSearchIndex = 0;
        searchKeyList = new ArrayList<Keyword>();
    }

    private void initSliderView(){
        imageSlider.setPresetTransformer(TimePreferencesManager.with(this).getImageDisplayTransformer());
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        long displayTime = TimePreferencesManager.with(this).getImageDisplayTime();
        imageSlider.setDuration(displayTime);
        imageSlider.addOnPageChangeListener(this);
    }

    private void setSliderViewImages(ArrayList<SearchResultFlickr> searchResultItems){
        for(int i = 0;i<searchResultItems.size();i++){
            SearchResultFlickr searchResultFlickr = searchResultItems.get(i);
            TextSliderView textSliderView = new TextSliderView(this);
            Bundle arguments = new Bundle();
            arguments.putString("Keyword_key",searchResultFlickr.getKeyword());
            textSliderView
                    .description(searchResultFlickr.getTitle())
                    .image(searchResultFlickr.getUrl())
                    .bundle(arguments)
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            imageSlider.addSlider(textSliderView);
        }
        imageSlider.startAutoCycle();
    }

    @OnClick(R.id.fab)
    public void onkeysFabClick() {
        KeywordPickerDialog keywordPickerDialog = KeywordPickerDialog.newInstance();
        keywordPickerDialog.show(getSupportFragmentManager(), KeywordPickerDialog.TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolbar("Ghost Writer",false);
        if(imageSlider!=null){
            initSliderView();
            imageSlider.startAutoCycle();
        }
        if(TimePreferencesManager.with(this).isChangedLocale){
            TimePreferencesManager.with(this).isChangedLocale = false;
            restartActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        imageSlider.stopAutoCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageSlider.stopAutoCycle();
        imageSlider.removeAllSliders();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if(isFullScreen){
            showToolbar();
            float density = getResources().getDisplayMetrics().density;
            sliderLayout.setPadding((int)(16*density),(int)(32*density),(int)(16*density),(int)(16*density));
            floatingActionButton.setVisibility(View.VISIBLE);
            isFullScreen = false;
        }else{
            hideToolbar();
            sliderLayout.setPadding(0,0,0,0);
            floatingActionButton.setVisibility(View.GONE);
            isFullScreen = true;
        }
        imageSlider.startAutoCycle();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        if(state == 2) {
            Bundle bundle = imageSlider.getCurrentSlider().getBundle();
            String keyword = bundle.getString("Keyword_key");
            keywordTextView.setText(keyword);
        }
    }

    @Override
    public void onSelectedKeywords(List<Keyword> keywords) {
        initSliderFlags();
        keywordTextView.setText("");
        for(Keyword keyword:keywords){
            searchKeyList.add(keyword);
        }
        
        TimePreferencesManager.with(this).saveSearchKeyword(searchKeyList);
        showLoading();
        fetchFlickImage();
    }

    private void fetchFlickImage(){
        if(currentSearchIndex == searchKeyList.size()){
            clusteringSliders();
            dismissLoading();
            return;
        }

        final Keyword keyword = searchKeyList.get(currentSearchIndex);
        if(SearchImagesCacheManager.with().getCachedSearchImages(keyword.getWord()).size()==0) {
            FlickrImageLoadTask task = new FlickrImageLoadTask();
            task.setFlickrImageLoadTaskInterface(new FlickrImageLoadTaskInterface() {
                @Override
                public void onGetFlickrImageList(List<SearchResultFlickr> searchResultFlickrs) {
                    ArrayList<SearchResultFlickr> updatedSearchResultFlickrs = new ArrayList<SearchResultFlickr>();
                    for(SearchResultFlickr searchResultFlickr:searchResultFlickrs){
                        searchResultFlickr.setKeyword(keyword.getEnWord());
                        updatedSearchResultFlickrs.add(searchResultFlickr);
                    }

                    SearchImagesCacheManager.with().setSearchImages(keyword.getWord(),updatedSearchResultFlickrs);
                    keyword.setSearchResultFlickrs(updatedSearchResultFlickrs);
                    nextFetchFlckImage();
                }

                @Override
                public void onFailedGetFlickrImageList(String message) {
                    nextFetchFlckImage();
                }
            });
            task.execute(new String[]{keyword.getEnWord(), String.valueOf(keyword.getPerPage()), String.valueOf(keyword.getPage())});
        }else{
            keyword.setSearchResultFlickrs(SearchImagesCacheManager.with().getCachedSearchImages(keyword.getWord()));
            nextFetchFlckImage();
        }
    }

    private void nextFetchFlckImage(){
        currentSearchIndex++;
        fetchFlickImage();
    }

    private void clusteringSliders(){
        ArrayList<SearchResultFlickr> clusterSearchImages = new ArrayList<SearchResultFlickr>();
        int totalClusterCount = Constant.defaulSliderCount/(searchKeyList.size()*Constant.clusterCount);
        if(totalClusterCount>1) {
            for (int k = 0; k < totalClusterCount - 1; k++) {
                for (int i = 0; i < searchKeyList.size(); i++) {
                    Keyword keyword = searchKeyList.get(i);
                    for (int j = 0; j < Constant.clusterCount; j++) {
                        int index = j + k * Constant.clusterCount;
                        if(index<keyword.getSearchResultFlickrs().size()) {
                            clusterSearchImages.add(keyword.getSearchResultFlickrs().get(index));
                        }
                    }
                }
            }
        }else {
            for (int i = 0; i < searchKeyList.size(); i++) {
                Keyword keyword = searchKeyList.get(i);
                for (int j = 0; j < Constant.clusterCount; j++) {
                    clusterSearchImages.add(keyword.getSearchResultFlickrs().get(j));
                }
            }
        }
        setSliderViewImages(clusterSearchImages);
    }

    private void initSliderFlags(){
        searchKeyList.clear();
        currentSearchIndex = 0;
        imageSlider.stopAutoCycle();
        imageSlider.removeAllSliders();
    }

    private void showAlertDialog(){
        WarningDialogFragment dialogFragment = WarningDialogFragment.newInstance(getResources().getString(R.string.choose_keyword),getResources().getString(R.string.show_keyword_dialog_message),false);
        dialogFragment.show(getSupportFragmentManager(), WarningDialogFragment.TAG);
    }

    @Override
    public void onDialogOkButtonClicked(){
        KeywordPickerDialog keywordPickerDialog = KeywordPickerDialog.newInstance();
        keywordPickerDialog.show(getSupportFragmentManager(), KeywordPickerDialog.TAG);
    }
}
