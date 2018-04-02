package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.manager.TimePreferencesManager;
import com.mudib.ghostwriter.utils.Util;

import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import stfalcon.universalpickerdialog.UniversalPickerDialog;
//import hotchemi.stringpicker.StringPickerDialog;

/**
 * Created by diana on 06/02/2018.
 */

public class SettingActivity extends BaseActivity implements UniversalPickerDialog.OnPickListener {

    public static int DISPLAY_TRANSFORM = 1;

    public static int KEYWORD_LNGUAGE = 2;

    @BindView(R.id.seekbar_displaytime)
    SeekBar seekbar_displaytime;

    @BindView(R.id.textView_displaytime)
    TextView textView_displaytime;

    @BindView(R.id.textView_displaytransform)
    TextView textView_displaytransform;

    @BindView(R.id.textView_language)
    TextView textView_language;

    private String[] transformValues;

    private int settingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        initToolbar(getResources().getString(R.string.setting),true);
        initUi();
    }

    private void initData(){
        settingIndex = 0;
        transformValues = new String[SliderLayout.Transformer.values().length];
    }

    private void initUi(){
        seekbar_displaytime.setProgress((int)TimePreferencesManager.with(this).getImageDisplayTime()/1000);
        textView_displaytime.setText(String.valueOf(seekbar_displaytime.getProgress())+"s");
        seekbar_displaytime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                if(progress == 0){
                    seekBar.setProgress(1);
                }
                textView_displaytime.setText(String.valueOf(seekBar.getProgress())+"s");
                long displayTime = seekbar_displaytime.getProgress()*1000;
                TimePreferencesManager.with(SettingActivity.this).saveImageDisplayTime(displayTime);
            }
        });
        textView_displaytransform.setText(TimePreferencesManager.with(this).getImageDisplayTransformer());
        textView_language.setText(TimePreferencesManager.with(this).getKeywordLangauge());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.layout_displaytransform)
    public void onDisplayTransformClicked() {
        settingIndex = DISPLAY_TRANSFORM;
        for(int i = 0; i<SliderLayout.Transformer.values().length;i++){
            transformValues[i] = SliderLayout.Transformer.values()[i].toString();
        }
        new UniversalPickerDialog.Builder(this)
                .setTitle(getResources().getString(R.string.image_display_transform))
                .setPositiveButtonText(getResources().getString(R.string.button_ok))
                .setNegativeButtonText(getResources().getString(R.string.button_cancel))
                .setListener(this)
                .setInputs(
                        new UniversalPickerDialog.Input(0, transformValues)
                )
                .show();
    }

    @OnClick(R.id.layout_language)
    public void onLanguageLayoutClicked() {
        settingIndex = KEYWORD_LNGUAGE;
        new UniversalPickerDialog.Builder(this)
                .setTitle(getResources().getString(R.string.keyword_language))
                .setPositiveButtonText(getResources().getString(R.string.button_ok))
                .setNegativeButtonText(getResources().getString(R.string.button_cancel))
                .setListener(this)
                .setInputs(
                        new UniversalPickerDialog.Input(0, Constant.languages)
                )
                .show();
    }

    @Override
    public void onPick(int[] selectedValues, int key) {
        if(settingIndex == DISPLAY_TRANSFORM) {
            textView_displaytransform.setText(transformValues[selectedValues[0]]);
            TimePreferencesManager.with(this).saveImageDisplayTransformer(transformValues[selectedValues[0]]);
        }else{
            String value = Constant.languages[selectedValues[0]];
            if(!value.equalsIgnoreCase(TimePreferencesManager.with(this).getKeywordLangauge())) {
                textView_language.setText(value);
                TimePreferencesManager.with(this).saveKeywordLangauge(value);
                Util.setLocale(getApplicationContext(), value);
                TimePreferencesManager.with(this).isChangedLocale = true;
                restartActivity();
            }
        }
    }

}
