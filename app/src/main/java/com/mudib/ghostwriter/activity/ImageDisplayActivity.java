package com.mudib.ghostwriter.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mudib.ghostwriter.Interface.KeywordPickerDialogInterface;
import com.mudib.ghostwriter.R;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mudib.ghostwriter.adapter.KeywordListAdapter;
import com.mudib.ghostwriter.dialog.KeywordPickerDialog;
import com.mudib.ghostwriter.manager.ApiClient;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mudib.ghostwriter.models.Keyword;
import com.mudib.ghostwriter.models.SearchResultItem;
import com.mudib.ghostwriter.models.SynonymWord;
import com.mudib.ghostwriter.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by diana on 06/02/2018.
 */

public class ImageDisplayActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,KeywordPickerDialogInterface{

    public static int Clustering_Count = 3;

    @BindView(R.id.slider_images)
    SliderLayout imageSlider;

    @BindView(R.id.textView_keys)
    TextView keysTextView;

    @BindView(R.id.textView_synonym)
    TextView synonymTextView;

    private ArrayList<Keyword> searchKeyList;

    private ArrayList<SynonymWord> selectedSynonymList;

    private ArrayList<SearchResultItem> allSearchResultItems = new ArrayList<>();

    private int currentSearchIndex;
    private int startNum;
    private int sliderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        initDatas();
        initToolbar("Image Display");
        initSliderView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDatas(){
        currentSearchIndex = 0;
        sliderCount = 0;
        startNum = 1;
        searchKeyList = new ArrayList<Keyword>();
        selectedSynonymList = new ArrayList<SynonymWord>();
    }

    private void initSliderView(){
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        long displayTime = TimePreferencesManager.with(this).getImageDisplayTime();
        imageSlider.setDuration(displayTime);
        imageSlider.addOnPageChangeListener(this);
    }

    private void setSliderViewImages(ArrayList<SearchResultItem> searchResultItems){
        sliderCount+=searchResultItems.size();
        for(int i = 0;i<searchResultItems.size();i++){
            SearchResultItem searchResultItem = searchResultItems.get(i);
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(searchResultItem.getTitle())
                    .image(searchResultItem.getImage().getThumbnailLink())
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
                    .setOnSliderClickListener(this);

            imageSlider.addSlider(textSliderView);
        }
        imageSlider.startAutoCycle();
    }

    private void getGoogleSearchImages(){
        if(imageSlider != null){
            imageSlider.stopAutoCycle();
        }
        if(currentSearchIndex == 0)
            showLoading();
        ApiClient.with(this).startGoogleSearchAsync(searchKeyList.get(currentSearchIndex).getWord(), startNum,Clustering_Count, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response != null) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<SearchResultItem>>() {
                                }.getType();
                                ArrayList<SearchResultItem> searchResultItems = gson.fromJson(response.getJSONArray("items").toString(), listType);
                                for(int i = 0; i<searchResultItems.size();i++){
                                    SearchResultItem searchResultItem = searchResultItems.get(i);
                                    allSearchResultItems.add(searchResultItem);
                                }
                                if(currentSearchIndex<(searchKeyList.size()-1)){
                                    currentSearchIndex++;
                                    getGoogleSearchImages();
                                }else{
                                    setSliderViewImages(allSearchResultItems);
                                    currentSearchIndex = 0;
                                    startNum = startNum + Clustering_Count;
                                    allSearchResultItems.clear();
                                    dismissLoading();
                                }
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject jsonObject ) {
                        super.onFailure(statusCode, headers, throwable, jsonObject);
                        dismissLoading();
                        Toast.makeText(ImageDisplayActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        if(currentSearchIndex<(searchKeyList.size()-1)){
                            currentSearchIndex++;
                            getGoogleSearchImages();
                        }else{
                            setSliderViewImages(allSearchResultItems);
                            currentSearchIndex = 0;
                            startNum = startNum + Clustering_Count;
                            allSearchResultItems.clear();
                            dismissLoading();
                        }
                    }
                }
        );
    }

    private void getSynonyms(){
//        showLoading();
//        ApiClient.with(this).startGetSynonymsAsync(selectedKey, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        if (response != null) {
//                            Gson gson = new Gson();
//                            Type listType = new TypeToken<List<SynonymWord>>() {
//                            }.getType();
//                            ArrayList<SynonymWord> synonymWords = gson.fromJson(response.toString(), listType);
//                            SynonymWord synonymWord = new SynonymWord();
//                            synonymWord.setScore(0);
//                            synonymWord.setWord(selectedKey);
//                            synonymWords.add(0,synonymWord);
//                            selectedSynonymList.addAll(synonymWords);
//                            setTextViewsText();
//                            dismissLoading();
//                            if(currentSearchIndex == 0){
//                                getGoogleSearchImages(selectedSynonymList.get(currentSearchIndex).getWord());
//                            }
//                        }else{
//                            Toast.makeText(ImageDisplayActivity.this,"can't get synonyms. please try again.", Toast.LENGTH_SHORT).show();
//                            dismissLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers,Throwable throwable, JSONObject jsonObject ) {
//                        super.onFailure(statusCode, headers, throwable, jsonObject);
//                        dismissLoading();
//                        Toast.makeText(ImageDisplayActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );

    }


    @OnClick(R.id.textView_keys)
    public void onkeysTextClick() {
        KeywordPickerDialog keywordPickerDialog = KeywordPickerDialog.newInstance();
        keywordPickerDialog.show(getSupportFragmentManager(), KeywordPickerDialog.TAG);
    }

    @Override
    protected void onStop() {
        imageSlider.stopAutoCycle();
        imageSlider.removeAllSliders();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if((position == sliderCount-1)&&(isLoading==false)){
            getGoogleSearchImages();
//            if(currentSearchIndex<selectedSynonymList.size()-1){
//                    currentSearchIndex++;
//                    getGoogleSearchImages(selectedSynonymList.get(currentSearchIndex).getWord());
//            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onSelectedKeywords(List<Keyword> keywords) {

        initSliderFlags();

        keysTextView.setText("");
        for(Keyword keyword:keywords){
            searchKeyList.add(keyword);
        }
        for(int i=0;i<searchKeyList.size();i++){
            Keyword keyword = searchKeyList.get(i);
            if(keysTextView.getText().toString() == ""){
                keysTextView.setText(keyword.getWord());
            }else{
                keysTextView.setText(keysTextView.getText().toString()+" ,"+keyword.getWord());
            }
        }
        getGoogleSearchImages();
    }

    private void initSliderFlags(){
        searchKeyList.clear();
        currentSearchIndex = 0;
        startNum = 1;
        imageSlider.stopAutoCycle();
        imageSlider.removeAllSliders();
        sliderCount = 0;
        allSearchResultItems.clear();
    }
}
