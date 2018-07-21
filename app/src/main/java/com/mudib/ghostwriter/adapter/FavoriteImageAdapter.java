package com.mudib.ghostwriter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mudib.ghostwriter.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteImageAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private String[] favoriteUrlList;

    public FavoriteImageAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return favoriteUrlList.length;
    }

    @Override
    public String getItem(int position) {
        return favoriteUrlList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FavoriteImageAdapter.ImageHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.item_image;
            convertView = mLayoutInflater.inflate(layoutId, parent, false);
            holder = new FavoriteImageAdapter.ImageHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (FavoriteImageAdapter.ImageHolder) convertView.getTag();
        }
        String favorite = favoriteUrlList[position];
        Picasso.with(mContext).load(favorite).into(holder.imageView);
        return convertView;
    }

    public void setItems(String[] favoriteUrlList) {
        this.favoriteUrlList = favoriteUrlList;
        notifyDataSetChanged();
    }

    static class ImageHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        ImageHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
