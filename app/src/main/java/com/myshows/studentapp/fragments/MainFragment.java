package com.myshows.studentapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.myshows.studentapp.R;
import com.myshows.studentapp.activities.LoginActivity;
import com.myshows.studentapp.activities.MainActivity;
import com.myshows.studentapp.model.Show;
import com.myshows.studentapp.model.User;
import com.myshows.studentapp.model.eventbus.MyShowsMsg;
import com.myshows.studentapp.model.eventbus.UserProfileMsg;
import com.myshows.studentapp.rest.RestClient;
import com.myshows.studentapp.utils.Connectivity;
import com.myshows.studentapp.utils.Utils;
import com.myshows.studentapp.view.adapters.MyShowsAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final boolean[] LOADING = {false, false};

    private static final String BUNDLE_USER = "BUNDLE_USER";
    private static final String BUNDLE_SHOWS = "BUNDLE_SHOWS";

    private SwipeRefreshLayout rootView;

    @Bind(R.id.my_shows_list)
    ListView myShowsList;

    @Bind(R.id.empty_view)
    View emptyView;

    TextView episodesCount;
    TextView daysCount;
    TextView hoursCount;

    private User user;
    private ArrayList<Show> shows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_main, container, false);
        }
        ButterKnife.bind(this, rootView);

        rootView.setOnRefreshListener(this);
        rootView.setColorSchemeResources(R.color.colorAccent);

        setUpListView();

        return rootView;
    }

    private void setUpListView() {
        myShowsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                rootView.setEnabled(view.getChildCount() == 0 || view.getChildAt(0).getTop() == 0);
            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        myShowsList.setEmptyView(emptyView);
        setUpListViewHeader();
    }

    private void setUpListViewHeader() {
        View header = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_user_stats, myShowsList, false);
        myShowsList.addHeaderView(header);
        daysCount = ButterKnife.findById(header, R.id.day_count_count);
        hoursCount = ButterKnife.findById(header, R.id.hours_count);
        episodesCount = ButterKnife.findById(header, R.id.episodes_count);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(UserProfileMsg msg) {
        LOADING[0] = false;
        stopLoading();
        switch (msg.getMessage()) {
            case OK:
                user = msg.getUser();
                populateUserProfile();
                break;
            case AUTH_ERROR:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                Utils.makeSnackBar(getActivity(), R.string.error, Snackbar.LENGTH_SHORT);
        }
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(MyShowsMsg msg) {
        LOADING[1] = false;
        stopLoading();
        switch (msg.getMessage()) {
            case OK:
                shows = msg.getShows();
                populateMyShowsList();
                break;
            case AUTH_ERROR:
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            default:
                Utils.makeSnackBar(getActivity(), R.string.error, Snackbar.LENGTH_SHORT);
        }
    }

    private void updateData() {
        rootView.setRefreshing(true);

        LOADING[0] = true;
        LOADING[1] = true;

        RestClient.getInstance().loadUserData();
        RestClient.getInstance().loadMyShows();
    }

    private void stopLoading() {
        for (boolean isLoading : LOADING) if (isLoading) return;
        rootView.setRefreshing(false);
    }

    private void populateUserProfile() {
        ((MainActivity) getActivity()).getToolbar().setTitle(user.login);
        daysCount.setText(user.stats.watchedDays);
        episodesCount.setText(user.stats.watchedEpisodes);
        hoursCount.setText(user.stats.watchedHours);
    }

    private void populateMyShowsList() {
        myShowsList.setAdapter(new MyShowsAdapter(getContext(), shows));
    }

    @Override
    public void onRefresh() {
        updateData();
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            for (boolean isLoading : LOADING) if (isLoading) rootView.setRefreshing(true);

            if (savedInstanceState.containsKey(BUNDLE_USER)) {
                user = savedInstanceState.getParcelable(BUNDLE_USER);
            }

            if (savedInstanceState.containsKey(BUNDLE_SHOWS)) {
                shows = savedInstanceState.getParcelableArrayList(BUNDLE_SHOWS);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (user != null) {
            outState.putParcelable(BUNDLE_USER, user);
        }

        if (shows != null) {
            outState.putParcelableArrayList(BUNDLE_SHOWS, shows);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (Connectivity.isConnected(getContext())) {
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    updateData();
                }
            });
        }
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
