package com.myshows.studentapp.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myshows.studentapp.R;
import com.myshows.studentapp.model.Show;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MyShowsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Show> shows;

    public MyShowsAdapter(Context context, ArrayList<Show> shows) {
        this.context = context;
        this.shows = shows;
    }

    @Override
    public int getCount() {
        if (shows == null) return 0;
        return shows.size();
    }

    @Override
    public Object getItem(int position) {
        return shows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_my_show_preview, parent, false);
            Show show = shows.get(position);

            ImageView poster = ButterKnife.findById(view, R.id.poster);
            TextView title = ButterKnife.findById(view, R.id.title);
            TextView totalEpisodes = ButterKnife.findById(view, R.id.total_episodes);
            TextView watchedEpisodes = ButterKnife.findById(view, R.id.watched_episodes);

            String totalEpisodesText =
                    context.getString(R.string.total_episodes) + " : " + show.totalEpisodes;
            String watchedEpisodesText =
                    context.getString(R.string.watched_episodes) + " : " + show.watchedEpisodes;

            Picasso.with(context).load(show.image).into(poster);
            title.setText(show.title);
            totalEpisodes.setText(totalEpisodesText);
            watchedEpisodes.setText(watchedEpisodesText);
        }
        return view;
    }
}
