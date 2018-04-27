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
        holder.keywordTextView.setText(keywords.get(position).getWord());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keyword keyword = keywords.get(position);
                keyword.setSelected(holder.checkBox.isChecked());
                keyword.setEnWord(Constant.allSearchkeys[position]);
                keywords.set(position,keyword);
                if (holder.checkBox.isChecked()) {
                    mKeywordListAdapterListener.onCheckedKeyword(keyword);
                }else {
                    mKeywordListAdapterListener.onUnCheckedKeyword(keyword);
                }
            }
        });

        holder.checkBox.setChecked(keywords.get(position).isSelected());

        Keyword keyword = keywords.get(position);

        Resources resources = mContext.getResources();
        int resourceId;
        if (keyword.getEnWord().equalsIgnoreCase("Alpha&Omega")){
           resourceId = resources.getIdentifier("alpha", "drawable",
                    mContext.getPackageName());

        }else if (keyword.getEnWord().equalsIgnoreCase("walled garden")){
            resourceId = resources.getIdentifier("garden", "drawable",
                    mContext.getPackageName());
        }else if (keyword.getEnWord().equalsIgnoreCase("wild man")){
            resourceId = resources.getIdentifier("wildman", "drawable",
                    mContext.getPackageName());
        }else{
            String identifier = "";
            if (keyword.getEnWord().length() <= 1) {
                identifier = keyword.getEnWord().toLowerCase();
            } else {
                identifier = keyword.getEnWord().substring(0, 1).toLowerCase() + keyword.getEnWord().substring(1);
            }

            resourceId = resources.getIdentifier(identifier, "drawable",
                    mContext.getPackageName());
            if(resourceId == 0){
                resourceId = resources.getIdentifier("logo", "drawable",
                        mContext.getPackageName());
            }
        }
        holder.imageView_keyword.setImageDrawable(resources.getDrawable(resourceId));
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
