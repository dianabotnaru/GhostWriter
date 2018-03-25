package com.mudib.ghostwriter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.manager.TimePreferencesManager;

import butterknife.BindView;
import butterknife.OnClick;
import hotchemi.stringpicker.StringPickerDialog;

/**
 * Created by diana on 06/02/2018.
 */

public class SettingActivity extends BaseActivity implements StringPickerDialog.OnClickListener {

    @BindView(R.id.seekbar_displaytime)
    SeekBar seekbar_displaytime;

    @BindView(R.id.textView_displaytime)
    TextView textView_displaytime;

    @BindView(R.id.textView_displaytransform)
    TextView textView_displaytransform;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar("Setting",true);
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
                long displayTime = seekbar_displaytime.getProgress()*1000;
                TimePreferencesManager.with(SettingActivity.this).saveImageDisplayTime(displayTime);
            }
        });
        textView_displaytransform.setText(TimePreferencesManager.with(this).getImageDisplayTransformer());
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

        StringPickerDialog dialog = new StringPickerDialog();
        Bundle bundle = new Bundle();
        String[] values = new String[SliderLayout.Transformer.values().length];
        for(int i = 0; i<SliderLayout.Transformer.values().length;i++){
            values[i] = SliderLayout.Transformer.values()[i].toString();
        }
        bundle.putStringArray(getString(R.string.string_picker_dialog_values), values);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "SettingActivity");
    }

    @Override
    public void onClick(String value) {
        textView_displaytransform.setText(value);
        TimePreferencesManager.with(this).saveImageDisplayTransformer(value);
    }
}
