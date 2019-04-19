/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ominext.haivn.appandroidtv;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.ominext.haivn.appandroidtv.activity.PhotoDetailsActivity;
import com.ominext.haivn.appandroidtv.activity.SearchActivity;
import com.ominext.haivn.appandroidtv.activity.SettingsActivity;
import com.ominext.haivn.appandroidtv.api.ApiClient;
import com.ominext.haivn.appandroidtv.api.ApiService;
import com.ominext.haivn.appandroidtv.model.ListItem;
import com.ominext.haivn.appandroidtv.model.MyItem;
import com.ominext.haivn.appandroidtv.model.MyUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    public static List<MyItem> listPhoto, listVideo, listData;
    public static List<Movie> listMovie;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        prepareBackgroundManager();
        setupUIElements();
        //listFiles("Bearer ya29.GlzuBrE_nWifRCjWJpB4JZSrcrbZiMo2t5zk6cbx_gctvsF95v3JdTuBv4JJONjFgDQW2UN2DQ9qwwOhTIHrAIoe7JQIi8vbgFC22Fn7vw6AVJ0pSD1AtPeZxNgXpw");
        setupEventListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mBackgroundTimer) {
            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
            mBackgroundTimer.cancel();
        }
    }

    private void loadRows() {
        List<Movie> listPhoto = MovieList.setupMovies(this.listPhoto);
        List<Movie> listVideo = MovieList.setupMovies(this.listVideo);
        listMovie = MovieList.setupMovies(this.listData);

        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        CardPresenter cardPresenter = new CardPresenter();

        ArrayObjectAdapter listRowVideoAdapter = new ArrayObjectAdapter(cardPresenter);
        for (int j = 0; j < this.listVideo.size(); j++) {
            listRowVideoAdapter.add(listVideo.get(j));
        }
        HeaderItem headerVideo = new HeaderItem(0, MovieList.MOVIE_CATEGORY[0]);
        rowsAdapter.add(new ListRow(headerVideo, listRowVideoAdapter));

        ArrayObjectAdapter listRowPhotoAdapter = new ArrayObjectAdapter(cardPresenter);
        for (int j = 0; j < this.listPhoto.size(); j++) {
            listRowPhotoAdapter.add(listPhoto.get(j));
        }
        HeaderItem headerPhoto = new HeaderItem(1, MovieList.MOVIE_CATEGORY[1]);
        rowsAdapter.add(new ListRow(headerPhoto, listRowPhotoAdapter));

        HeaderItem gridHeader = new HeaderItem(2, "SETTINGS");

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(rowsAdapter);
    }

    private void prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());

        mDefaultBackground = ContextCompat.getDrawable(getActivity(), R.drawable.default_background);
        mMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    private void setupUIElements() {
        // setBadgeDrawable(getActivity().getResources().getDrawable(
        // R.drawable.videos_by_google_banner));
        setTitle(getString(R.string.browse_title)); // Badge, when set, takes precedent
        // over title
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                        .show();

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void updateBackground(String uri) {
        int width = mMetrics.widthPixels;
        int height = mMetrics.heightPixels;
        Glide.with(getActivity())
                .load(uri)
                .centerCrop()
                .error(mDefaultBackground)
                .into(new SimpleTarget<GlideDrawable>(width, height) {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable>
                                                        glideAnimation) {
                        mBackgroundManager.setDrawable(resource);
                    }
                });
        mBackgroundTimer.cancel();
    }

    private void startBackgroundTimer() {
        if (null != mBackgroundTimer) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new UpdateBackgroundTask(), BACKGROUND_UPDATE_DELAY);
    }

    @Override
    protected void onDriveClientReady() {
        new MainFragment.RetrieveTokenTask().execute(googleSignInAccount.getEmail());
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        private final String TAG = MainFragment.RetrieveTokenTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            String token = null;
            String scope = "oauth2:" + Scopes.PROFILE;
            try {
                token = GoogleAuthUtil.getToken(getActivity().getApplicationContext(), googleSignInAccount.getAccount(), scope);
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
                    listPhoto = new ArrayList<>();
                    listVideo = new ArrayList<>();
                    listData = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        if (MyUtils.isPhoto(items.get(i).getTitle())) {
                            listPhoto.add(items.get(i));
                            listData.add(items.get(i));
                        } else if (MyUtils.isVideo(items.get(i).getTitle())) {
                            listVideo.add(items.get(i));
                            listData.add(items.get(i));
                        }
                    }

                    Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
                    intent.putExtra("LIST", (Serializable) listData);
                    getActivity().startActivity(intent);
                    //loadRows();
                }
            }

            @Override
            public void onFailure(Call<ListItem> call, Throwable t) {
            }
        });
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
                Log.d(TAG, "Item: " + item.toString());
                if (MyUtils.isPhoto(movie.getTitle())) {
                    Intent intent = new Intent(getActivity(), PhotoDetailsActivity.class);
                    for (int i = 0; i < listPhoto.size(); i++) {
                        if (movie.getVideoUrl().equals(listPhoto.get(i).getWebContentLink())) {
                            intent.putExtra("INDEX", i);
                            break;
                        }
                    }
                    intent.putExtra("LIST", (Serializable) listPhoto);
                    getActivity().startActivity(intent);
                } else if (MyUtils.isVideo(movie.getTitle())) {
                    Intent intent = new Intent(getActivity(), PlaybackActivity.class);
                    intent.putExtra(DetailsActivity.MOVIE, movie);
                    startActivity(intent);

                    /*Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                    intent.putExtra("VIDEO", movie.getVideoUrl());
                    startActivity(intent);*/

                }
            } else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
            if (item instanceof Movie) {
                mBackgroundUri = ((Movie) item).getBackgroundImageUrl();
                startBackgroundTimer();
            }
        }
    }

    private class UpdateBackgroundTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateBackground(mBackgroundUri);
                }
            });
        }
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(
                    ContextCompat.getColor(getActivity(), R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {
        }
    }

}
