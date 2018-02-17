package com.mudib.ghostwriter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mudib.ghostwriter.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jordi on 17/02/2018.
 */

public class KeywordListAdapter extends BaseAdapter {

    public static String[] allSearchkeys = {"Alpha", "Omega", "Anchor", "Angel","Ant","Apple","Baby","Beehive","Bird","Bread","Bull", "Camel", "Candle", "Cauldron","Chameleon","Compass","Cornucopia","Crocodile","Dolphin","Elephant"
    ,"Globe", "Griffin", "Helmet", "Horse","Hourglass","Lute","Madonna","Marionette","Moon", "Owl", "Serpent", "Sun","Sword","Thunderbolt","Tree","Walled Garden","Wild Man"};

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public KeywordListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allSearchkeys.length;
    }

    @Override
    public String getItem(int position) {
        return allSearchkeys[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.item_keyword;
            convertView = mLayoutInflater.inflate(layoutId, parent, false);
            holder = new DeviceHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (DeviceHolder) convertView.getTag();
        }
        holder.keywordTextView.setText(allSearchkeys[position]);
        return convertView;
    }

//    public void setItems(List<BAC> bacs) {
//        mBacs.clear();
//        for (BAC bac : bacs) {
//            mBacs.add(bac);
//        }
//        notifyDataSetChanged();
//    }

//    public void clear() {
//        mBacs.clear();
//        notifyDataSetChanged();
//    }

    static class DeviceHolder {
        @BindView(R.id.textView_keyword)
        TextView keywordTextView;
//
//        @BindView(R.id.text_date)
//        TextView mDate;

        DeviceHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
