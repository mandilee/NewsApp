package uk.co.mandilee.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String mUrl;

    NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        return Utilities.fetchNewsData(mUrl);
    }
}