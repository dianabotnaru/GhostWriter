package com.mudib.ghostwriter.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.mudib.ghostwriter.constant.Constant;
import com.mudib.ghostwriter.models.Keyword;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diana on 17/02/2018.
 */

public class KeywordListAdapter extends BaseAdapter {

    public interface KeywordListAdapterListener {
        public abstract void onCheckedKeyword(Keyword keyword);
        public abstract void onUnCheckedKeyword(Keyword keyword);
    }

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Keyword> keywords = new ArrayList<>();
    private KeywordListAdapterListener mKeywordListAdapterListener = null;

    public KeywordListAdapter(Context context,KeywordListAdapterListener keywordListAdapterListener) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mKeywordListAdapterListener = keywordListAdapterListener;
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
        final DeviceHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.item_keyword;
            convertView = mLayoutInflater.inflate(layoutId, parent, false);
            holder = new DeviceHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceHolder) convertView.getTag();
        }
        Keyword keyword = keywords.get(position);
        holder.keywordTextView.setText(keyword.getWord());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyword keyword = keywords.get(position);
                keyword.setSelected(holder.checkBox.isChecked());
                keywords.set(position,keyword);
                if (holder.checkBox.isChecked()) {
                    mKeywordListAdapterListener.onCheckedKeyword(keyword);
                }else {
                    mKeywordListAdapterListener.onUnCheckedKeyword(keyword);
                }
            }
        });
        holder.checkBox.setChecked(keyword.isSelected());
        Resources resources = mContext.getResources();
        if(keyword.getResourceId()!=0) {
            holder.imageView_keyword.setImageDrawable(resources.getDrawable(keyword.getResourceId()));
        }

        if(Constant.isRTL){
            holder.linearLayout_keyword.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
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

    public List<Keyword> getUnSelectedItems(){
        List<Keyword> selectedKeywords = new ArrayList<>();
        for(Keyword keyword:keywords){
            if(!keyword.isSelected()){
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

        @BindView(R.id.imageView_keyword)
        ImageView imageView_keyword;

        @BindView(R.id.linearLayout_keyword)
        LinearLayout linearLayout_keyword;

        DeviceHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
