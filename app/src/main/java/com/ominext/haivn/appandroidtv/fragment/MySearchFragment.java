package com.ominext.haivn.appandroidtv.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.util.Log;

import com.ominext.haivn.appandroidtv.CardPresenter;
import com.ominext.haivn.appandroidtv.MainFragment;
import com.ominext.haivn.appandroidtv.Movie;
import com.ominext.haivn.appandroidtv.model.MyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MySearchFragment extends SearchFragment implements SearchFragment.SearchResultProvider {
    private static final String TAG = MySearchFragment.class.getSimpleName();

    private static final int REQUEST_SPEECH = 0x00000010;
    private ArrayObjectAdapter mRowsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        setSearchResultProvider(this);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        setDefaultResults();
        return mRowsAdapter;
    }

    private void setDefaultResults() {
        if (mRowsAdapter != null) {
            mRowsAdapter.clear();
            List<Movie> mItems = MainFragment.listMovie;
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
            listRowAdapter.addAll(0, mItems);
            HeaderItem header = new HeaderItem("Search results");
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
        }
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        if (newQuery.isEmpty()) {
            setDefaultResults();
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadRows(query);
        return true;
    }

    private void loadRows(String mQuery) {
        // offload processing from the UI thread
        new AsyncTask<String, Void, ListRow>() {
            private final String query = mQuery;

            @Override
            protected void onPreExecute() {
                mRowsAdapter.clear();
            }

            @Override
            protected ListRow doInBackground(String... params) {
                final List<Movie> result = new ArrayList<>();
                for (Movie movie : MainFragment.listMovie) {
                    // Main logic of search is here.
                    // Just check that "query" is contained in Title or Description or not.
                    if (movie.getTitle().toLowerCase(Locale.ENGLISH)
                            .contains(query.toLowerCase(Locale.ENGLISH))
                            || movie.getDescription().toLowerCase(Locale.ENGLISH)
                            .contains(query.toLowerCase(Locale.ENGLISH))) {
                        result.add(movie);
                    }
                }

                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                listRowAdapter.addAll(0, result);
                HeaderItem header = new HeaderItem("Search Results");
                return new ListRow(header, listRowAdapter);
            }

            @Override
            protected void onPostExecute(ListRow listRow) {
                mRowsAdapter.add(listRow);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
