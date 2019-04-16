package com.ominext.haivn.appandroidtv.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ominext.haivn.appandroidtv.model.MyItem;

import java.util.List;

public class PhotoPagerAdapter extends PagerAdapter {
    private List<MyItem> list;

    public PhotoPagerAdapter(List<MyItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final Context context = container.getContext();
        final AppCompatImageView imageView = new AppCompatImageView(context);
        container.addView(imageView);
        Glide.with(context).load(list.get(position).getThumbnailLink().replace("220", "1280")).into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
