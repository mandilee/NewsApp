package uk.co.mandilee.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class NewsAdapter extends ArrayAdapter<News> implements ListAdapter {

    NewsAdapter(Context context, ArrayList<News> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);
        String newsTitle = currentNews.getTitle();
        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title);
        newsTitleView.setText(newsTitle);

        String newsSection = currentNews.getSection();
        TextView newsSectionView = (TextView) listItemView.findViewById(R.id.news_section);
        newsSectionView.setText(newsSection);
        TextView newsPublishedView = (TextView) listItemView.findViewById(R.id.news_published);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateFormat dfDisplay = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        Date startDate;

        try {
            startDate = df.parse(currentNews.getPublished());
            String newDateString = dfDisplay.format(startDate);
            newsPublishedView.setText(newDateString);
        } catch (ParseException e) {
            Log.e("DATE ISSUE", e.toString());
            newsPublishedView.setVisibility(View.GONE);
        }
        return listItemView;
    }
}