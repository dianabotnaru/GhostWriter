package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mudib.ghostwriter.R;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mudib.ghostwriter.manager.GoogleImageSearchManager;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import cz.msebera.android.httpclient.Header;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mudib.ghostwriter.models.SearchResultItem;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * Created by diana on 06/02/2018.
 */

public class ImageDisplayActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    @BindView(R.id.slider_images)
    SliderLayout imageSlider;

    private String[] searchTextArray = {"Apple", "Orange", "Coffee", "Laptop","Tree","River","Building","Sky","Mouse","Desk"};
    private ArrayList<SearchResultItem> allsearchResultItems;
    private int currentSearchIndex;
    private int oldSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        initDatas();
        initToolbar("Image Display");
        initSliderView();
        getGoogleSearchImages(currentSearchIndex);
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
        allsearchResultItems = new ArrayList<SearchResultItem>();
    }

    private void initSliderView(){
        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        long displayTime = TimePreferencesManager.with(this).getImageDisplayTime();
        imageSlider.setDuration(displayTime);
        imageSlider.addOnPageChangeListener(this);
        imageSlider.stopAutoCycle();
    }

    private void addSearchResults(ArrayList<SearchResultItem> searchResultItems,int index) {
        int searchitemCount = 10;
        if(searchResultItems.size()<10){
            searchitemCount = searchResultItems.size();
        }
        for(int i = 0; i<searchitemCount;i++){
            SearchResultItem searchResultItem = searchResultItems.get(i);
            allsearchResultItems.add(allsearchResultItems.size(),searchResultItem);
        }
    }

    private void setSliderViewImages(){
        for(int i = 0;i<allsearchResultItems.size();i++){
            SearchResultItem searchResultItem = allsearchResultItems.get(i);
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(searchResultItem.getTitle())
                    .image(searchResultItem.getImage().getThumbnailLink())
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            imageSlider.addSlider(textSliderView);
        }
//        imageSlider.setCurrentPosition(0);
        imageSlider.startAutoCycle();
    }

    private void getGoogleSearchImages(final int index){
        if (index == 0)
            showLoading();
        GoogleImageSearchManager.with(this).startSearchAsync(searchTextArray[index], 0, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response != null) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<SearchResultItem>>() {
                                }.getType();
                                ArrayList<SearchResultItem> searchResultItems = gson.fromJson(response.getJSONArray("items").toString(), listType);
                                addSearchResults(searchResultItems,index);
//                                setSliderViewImages(searchResultItems);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        if (index<searchTextArray.length-1) {
                            getGoogleSearchImages(index + 1);
                        }
                        else{
                            dismissLoading();
                            setSliderViewImages();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        dismissLoading();
                        Toast.makeText(ImageDisplayActivity.this,responseString, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    @Override
    protected void onStop() {
        imageSlider.stopAutoCycle();
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
//        if (position == (allsearchResultItems.size() - 1)) {
//            if(oldSelectIndex == 0){
////                imageSlider.setCurrentPosition(0);
//            }else {
//                if (currentSearchIndex < searchTextArray.length - 1) {
//                    currentSearchIndex++;
//                    imageSlider.stopAutoCycle();
//                    getGoogleSearchImages(currentSearchIndex);
//                }
//            }
//        }
//        oldSelectIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
