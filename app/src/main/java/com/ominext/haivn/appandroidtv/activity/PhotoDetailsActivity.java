package com.ominext.haivn.appandroidtv.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.ominext.haivn.appandroidtv.BaseActivity;
import com.ominext.haivn.appandroidtv.MainFragment;
import com.ominext.haivn.appandroidtv.R;
import com.ominext.haivn.appandroidtv.adapter.PhotoPagerAdapter;
import com.ominext.haivn.appandroidtv.api.ApiClient;
import com.ominext.haivn.appandroidtv.api.ApiService;
import com.ominext.haivn.appandroidtv.model.ListItem;
import com.ominext.haivn.appandroidtv.model.MyItem;
import com.ominext.haivn.appandroidtv.model.MyUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoDetailsActivity extends BaseActivity {
    private ViewPager viewPager;
    private List<MyItem> list;
    private PhotoPagerAdapter adapter;
    private int index;
    private Handler handler;
    private int delay = 7000;
    private int page = 0;

    Runnable runnable = new Runnable() {
        public void run() {
            if (adapter != null) {
                if (adapter.getCount() == page) {
                    page = 0;
                } else {
                    page++;
                }
                viewPager.setCurrentItem(page, true);
            }
            handler.postDelayed(this, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_photo);
        Intent intent = getIntent();
        //index = intent.getIntExtra("INDEX", 0);
        //list = (List<MyItem>) intent.getExtras().getSerializable("LIST");
        handler = new Handler();
        initView();
        //listFiles("Bearer ya29.GlzwBvJuX2fg6uDCGk0MqDMjqBj-AsC_6re_OtC4xsnPTG1-775p1k6nW-tX8vQgBVpDDX44C9yXINWQWAcTvLGqpNgcXlytsNkCNzhM3EwBofXYrGm2pCXOdqF6Yw");
    }

    @Override
    protected void onDriveClientReady() {
        new PhotoDetailsActivity.RetrieveTokenTask().execute(googleSignInAccount.getEmail());
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        private final String TAG = PhotoDetailsActivity.RetrieveTokenTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            String token = null;
            String scope = "oauth2:" + Scopes.PROFILE;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), googleSignInAccount.getAccount(), scope);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listFiles("Bearer " + s);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    /**
     * Retrieves results for the next page. For the first run,
     * it retrieves results for the first page.
     */
    private void listFiles(String token) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ListItem> call = apiService.getListFile(token);
        call.enqueue(new Callback<ListItem>() {
            @Override
            public void onResponse(Call<ListItem> call, Response<ListItem> response) {
                if (response.code() == 200) {
                    List<MyItem> items = response.body().getItems();
                    list = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (MyUtils.isPhoto(items.get(i).getTitle())) {
                            list.add(items.get(i));
                        }
                    }

                    adapter = new PhotoPagerAdapter(list);
                    viewPager.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
            }
        });
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        /*if (list.size() > 0) {
            viewPager.setCurrentItem(index);
        }*/
    }
}
