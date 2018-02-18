package com.mudib.ghostwriter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diana on 17/02/2018.
 */

public class KeywordListAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Keyword> keywords = new ArrayList<>();

    public KeywordListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return keywords.size();
    }

    @Override
    public Keyword getItem(int position) {
        return keywords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DeviceHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.item_keyword;
            convertView = mLayoutInflater.inflate(layoutId, parent, false);
            holder = new DeviceHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceHolder) convertView.getTag();
        }
        holder.keywordTextView.setText(keywords.get(position).getWord());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                                    Keyword keyword = keywords.get(position);
                                                    keyword.setSelected(isChecked);
                                                    keywords.set(position,keyword);
                                               }
                                           }
        );
        holder.checkBox.setChecked(keywords.get(position).isSelected());
        return convertView;
    }

    public void setItems(List<Keyword> keywords) {
        this.keywords.clear();
        for (Keyword keyword : keywords) {
            this.keywords.add(keyword);
        }
        notifyDataSetChanged();
    }

    public List<Keyword> getSelectedItems(){
        List<Keyword> selectedKeywords = new ArrayList<>();
        for(Keyword keyword:keywords){
            if(keyword.isSelected()){
                selectedKeywords.add(keyword);
            }
        }
        return selectedKeywords;
    }

    public void clear() {
        keywords.clear();
        notifyDataSetChanged();
    }

    static class DeviceHolder {
        @BindView(R.id.textView_keyword)
        TextView keywordTextView;

        @BindView(R.id.checkBox)
        CheckBox checkBox;

        DeviceHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
