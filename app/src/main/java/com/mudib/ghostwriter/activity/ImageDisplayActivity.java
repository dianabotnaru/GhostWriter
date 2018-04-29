package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
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
import com.mudib.ghostwriter.manager.SharedPreferencesManager;

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


    @BindView(R.id.adView)
    AdView adView;

    @BindView(R.id.slider_images)
    SliderLayout imageSlider;

    @BindView(R.id.slider_layout)
    RelativeLayout sliderLayout;

    @BindView(R.id.slider_top_layout)
    LinearLayout topMarginLayout;

    @BindView(R.id.slider_bottom_layout)
    LinearLayout bottomMarginLayout;

    @BindView(R.id.slider_left_layout)
    LinearLayout leftMarginLayout;

    @BindView(R.id.slider_right_layout)
    LinearLayout rightMarginLayout;


    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.textView_keyword)
    TextView keywordTextView;

    private ArrayList<Keyword> searchKeyList;

    private int currentSearchIndex;

    private boolean isFullScreen = false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        initDatas();
        initSliderView();
        if(SharedPreferencesManager.with(this).isFirstLaunch){
            SharedPreferencesManager.with(this).isFirstLaunch = false;
            searchKeyList = SharedPreferencesManager.with(this).getSearchKeywordList();
            if(searchKeyList.size() == 0){
                showAlertDialog();
            }else{
                setKeywordTextViewString(searchKeyList);
                showLoading();
                fetchFlickImage();
            }
        }
        setRTLState();
        adRequest();
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void initSliderView(){
        imageSlider.setPresetTransformer(SharedPreferencesManager.with(this).getImageDisplayTransformer());
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        long displayTime = SharedPreferencesManager.with(this).getImageDisplayTime();
        imageSlider.setDuration(displayTime);
        imageSlider.addOnPageChangeListener(this);
    }

    private void initAdView(){
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }

    private void adRequest(){
        AdRequest request = new AdRequest.Builder()
                .build();
        adView.loadAd(request);
    }

    private void setKeywordTextViewString(List<Keyword> keywords){
        String keywordStrings = "";
        keywordTextView.setText("");
        for(int i= 0; i<keywords.size();i++){
            Keyword keyword = keywords.get(i);
            if (i==0){
                keywordStrings = keyword.getWord();
            }else{
                keywordStrings += ","+keyword.getWord();
            }
        }
        keywordTextView.setText(keywordStrings);
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
    protected void onStop() {
        super.onStop();
        imageSlider.stopAutoCycle();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if(isFullScreen){
            showToolbar();
            floatingActionButton.setVisibility(View.VISIBLE);
            isFullScreen = false;

            topMarginLayout.setVisibility(View.VISIBLE);
            bottomMarginLayout.setVisibility(View.VISIBLE);
            leftMarginLayout.setVisibility(View.VISIBLE);
            rightMarginLayout.setVisibility(View.VISIBLE);
        }else{
            hideToolbar();
            floatingActionButton.setVisibility(View.GONE);
            isFullScreen = true;

            topMarginLayout.setVisibility(View.GONE);
            bottomMarginLayout.setVisibility(View.GONE);
            leftMarginLayout.setVisibility(View.GONE);
            rightMarginLayout.setVisibility(View.GONE);
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

    }

    @Override
    public void onSelectedKeywords(List<Keyword> keywords) {
        initSliderFlags();
        for(int i= 0; i<keywords.size();i++){
            Keyword keyword = keywords.get(i);
            searchKeyList.add(keyword);
        }
        setKeywordTextViewString(keywords);
        SharedPreferencesManager.with(this).saveSearchKeyword(searchKeyList);
//        showLoading();
//        fetchFlickImage();

//        getGoogleAnalyticsEvent();
    }

    @Override
    public void onEditKeywordClicked(){
        startActivity(new Intent(ImageDisplayActivity.this, KeywordEditActivity.class));
    }

    private void fetchFlickImage(){
        if(currentSearchIndex == searchKeyList.size()){
            clusteringSliders();
            dismissLoading();
            return;
        }

        final Keyword keyword = searchKeyList.get(currentSearchIndex);
        if(SearchImagesCacheManager.with(this).getCachedSearchImages(keyword.getWord()).size()==0) {
            FlickrImageLoadTask task = new FlickrImageLoadTask();
            task.setFlickrImageLoadTaskInterface(new FlickrImageLoadTaskInterface() {
                @Override
                public void onGetFlickrImageList(List<SearchResultFlickr> searchResultFlickrs) {
                    ArrayList<SearchResultFlickr> updatedSearchResultFlickrs = new ArrayList<SearchResultFlickr>();
                    for(SearchResultFlickr searchResultFlickr:searchResultFlickrs){
                        searchResultFlickr.setKeyword(keyword.getEnWord());
                        updatedSearchResultFlickrs.add(searchResultFlickr);
                    }

                    SearchImagesCacheManager.with(ImageDisplayActivity.this).setSearchImages(keyword.getWord(),updatedSearchResultFlickrs);
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
            keyword.setSearchResultFlickrs(SearchImagesCacheManager.with(this).getCachedSearchImages(keyword.getWord()));
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
                    if (keyword.getSearchResultFlickrs() !=null) {
                        for (int j = 0; j < Constant.clusterCount; j++) {
                            int index = j + k * Constant.clusterCount;
                            if (index < keyword.getSearchResultFlickrs().size()) {
                                clusterSearchImages.add(keyword.getSearchResultFlickrs().get(index));
                            }
                        }
                    }
                }
            }
        }else {
            for (int i = 0; i < searchKeyList.size(); i++) {
                Keyword keyword = searchKeyList.get(i);
                if (keyword.getSearchResultFlickrs() !=null) {
                    for (int j = 0; j < Constant.clusterCount; j++) {
                        if (j < keyword.getSearchResultFlickrs().size()) {
                            clusterSearchImages.add(keyword.getSearchResultFlickrs().get(j));
                        }
                    }
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

    private void getGoogleAnalyticsEvent(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constant.google_analytics_item_id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, Constant.google_analytics_item_content);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Constant.google_analytics_item_content);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolbar("Ghost Writer",false);
        if(imageSlider!=null){
            initSliderView();
            imageSlider.startAutoCycle();
        }
        if(SharedPreferencesManager.with(this).isChangedLocale){
            SharedPreferencesManager.with(this).isChangedLocale = false;
            restartActivity();
        }
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        imageSlider.stopAutoCycle();
        imageSlider.removeAllSliders();
        super.onDestroy();
    }
}
