package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by diana on 06/02/2018.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.seekbar_displaytime)
    SeekBar seekbar_displaytime;

    @BindView(R.id.textView_displaytime)
    TextView textView_displaytime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar("Setting");
        initUi();
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
           }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.apply_btn)
    public void onApplyClick() {
        long displayTime = seekbar_displaytime.getProgress()*1000;
        TimePreferencesManager.with(this).saveImageDisplayTime(displayTime);
        finish();
    }


}
