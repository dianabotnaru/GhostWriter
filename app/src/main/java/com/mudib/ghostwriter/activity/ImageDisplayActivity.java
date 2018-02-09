package com.mudib.ghostwriter.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mudib.ghostwriter.R;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.mudib.ghostwriter.manager.ApiClient;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import hotchemi.stringpicker.StringPickerDialog;

/**
 * Created by diana on 06/02/2018.
 */

public class ImageDisplayActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,StringPickerDialog.OnClickListener{

    @BindView(R.id.slider_images)
    SliderLayout imageSlider;

    @BindView(R.id.textView_keys)
    TextView keysTextView;

    @BindView(R.id.textView_synonym)
    TextView synonymTextView;

    public static String[] allSearchkeys = {"Apple", "Orange", "Coffee", "Laptop","Tree","River","Building","Sky","Mouse","Desk"};

    private ArrayList<String> searchKeyList;

    private ArrayList<SearchResultItem> allsearchResultItems;
    private ArrayList<SearchResultItem> searchResultItems;


    private ArrayList<SynonymWord> selectedSynonymList;
    private String selectedKey;

    private int currentSearchIndex;
    private int sliderCount;

    private static final String TAG = StringPickerDialog.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagedisplay);
        initDatas();
        initToolbar("Image Display");
        initSliderView();

//        getGoogleSearchImages(currentSearchIndex);
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
        allsearchResultItems = new ArrayList<SearchResultItem>();
        searchKeyList = Util.initListFromStrings(allSearchkeys);
        selectedSynonymList = new ArrayList<SynonymWord>();
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
            sliderCount++;
        }
        imageSlider.startAutoCycle();
    }

    private void getGoogleSearchImages(final int index){
        ApiClient.with(this).startGoogleSearchAsync(selectedSynonymList.get(index).getWord(), 0, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response != null) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<SearchResultItem>>() {
                                }.getType();
                                ArrayList<SearchResultItem> searchResultItems = gson.fromJson(response.getJSONArray("items").toString(), listType);
                                addSearchResults(searchResultItems,index);
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        if (index<selectedSynonymList.size()-1) {
                            getGoogleSearchImages(index + 1);
                        }
                        else{
                            dismissLoading();
                            setSliderViewImages();
                            setTextViewsText();
                            allsearchResultItems.clear();
                            selectedSynonymList.clear();
                            currentSearchIndex = 0;
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

    private void getSynonyms(){
        showLoading();
        ApiClient.with(this).startGetSynonymsAsync(selectedKey, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if (response != null) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<SynonymWord>>() {
                            }.getType();
                            selectedSynonymList = gson.fromJson(response.toString(), listType);
//                            SynonymWord synonymWord = new SynonymWord();
//                            synonymWord.setScore(0);
//                            synonymWord.setWord(selectedKey);
//                            selectedSynonymList.add(0,synonymWord);
                            if(selectedSynonymList.size()!=0)
                                getGoogleSearchImages(0);
                            else
                                dismissLoading();
                        }else{
                            dismissLoading();
                            Toast.makeText(ImageDisplayActivity.this,"can't get synonyms. please try again.", Toast.LENGTH_SHORT).show();
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

    private void showSearchTextPickerDialog(String[] searchTextArray){
        Bundle bundle = new Bundle();
        bundle.putStringArray(getString(R.string.string_picker_dialog_values), searchTextArray);
        StringPickerDialog dialog = new StringPickerDialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), TAG);
    }

    @OnClick(R.id.textView_keys)
    public void onkeysTextClick() {
        if(searchKeyList.size() == 0)
            Toast.makeText(ImageDisplayActivity.this,"All of keys was chosen.", Toast.LENGTH_SHORT).show();
        else
            showSearchTextPickerDialog(Util.initStringsFromList(searchKeyList));
    }

    @Override
    protected void onStop() {
        imageSlider.stopAutoCycle();
        imageSlider = null;
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
        if(position == sliderCount-1){
            imageSlider.stopAutoCycle();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onClick(String value) {
        searchKeyList.remove(value);
        imageSlider.stopAutoCycle();
        selectedKey = value;
        getSynonyms();
   }

    private void setTextViewsText(){
        if(keysTextView.getText().toString() == ""){
            keysTextView.setText(selectedKey);
        }else{
            keysTextView.setText(keysTextView.getText().toString()+" ,"+selectedKey);
        }
        for(int i=0;i<selectedSynonymList.size();i++){
            SynonymWord synonymWord = selectedSynonymList.get(i);
            if(synonymTextView.getText().toString() == ""){
                synonymTextView.setText(synonymWord.getWord());
            }else{
                synonymTextView.setText(synonymTextView.getText().toString()+" ,"+synonymWord.getWord());
            }
        }
    }

}
