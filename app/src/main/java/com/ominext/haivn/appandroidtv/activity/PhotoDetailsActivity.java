package com.ominext.haivn.appandroidtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ominext.haivn.appandroidtv.R;
import com.ominext.haivn.appandroidtv.adapter.PhotoPagerAdapter;
import com.ominext.haivn.appandroidtv.model.MyItem;

import java.util.List;

public class PhotoDetailsActivity extends Activity {
    private ViewPager viewPager;
    private List<MyItem> list;
    private PhotoPagerAdapter adapter;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_photo);
        Intent intent = getIntent();
        index = intent.getIntExtra("INDEX", 0);
        list = (List<MyItem>) intent.getExtras().getSerializable("LIST");
        initView();
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        adapter = new PhotoPagerAdapter(list);
        viewPager.setAdapter(adapter);
        if (list.size() > 0) {
            viewPager.setCurrentItem(index);
        }
    }
}
