package com.myshows.studentapp.fragments;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myshows.studentapp.R;
import com.myshows.studentapp.activities.MainActivity;
import com.myshows.studentapp.utils.Connectivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoInternetFragment extends Fragment {

    View rootView;

    @Bind(R.id.description)
    TextView descriptionField;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_no_internet_connection, container, false);
            ButterKnife.bind(this, rootView);
        }

        return rootView;
    }

    @OnClick(R.id.retry_btn)
    @SuppressWarnings("unused")
    public void onRetryBtnClick(View view) {
        MainActivity activity = (MainActivity) getActivity();
        if (Connectivity.isConnected(getContext())) {
            if (activity != null) {
                INFragment iNFragment = activity.getInternetNeededFragment();
                if (iNFragment != null) {
                    activity.getSupportFragmentManager().popBackStack();
                    activity.switchScreen(iNFragment.getFragment(), iNFragment.getTag(), true);
                    activity.setInternetNeededFragment(null);
                }
            }
        } else {
            descriptionField.setText(R.string.are_you_sure_internet_is_on);
        }
    }

    @Nullable
    public static INFragment goToINFragment(AppCompatActivity a, Fragment f, String t) {
        final String NAME = NoInternetFragment.class.getName();
        FragmentManager fragmentManager = a.getSupportFragmentManager();

        boolean fragmentPopped = fragmentManager.popBackStackImmediate(NAME, 0);
        if (!fragmentPopped && fragmentManager.findFragmentByTag(NAME) == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new NoInternetFragment(), NAME)
                    .addToBackStack(NAME)
                    .commit();
            return new INFragment(f.getClass().getName(), f.getArguments(), t);
        }
        return null;
    }

    /**
     * INFFragment (Internet Needed Fragment) - needed to keep in memory invoked fragment
     * to call it after internet connection appears
     */
    public static final class INFragment implements Parcelable {

        public static final String INTERNET_NEEDED_FRAGMENT = "INTERNET_NEEDED_FRAGMENT";

        private String className;
        private Bundle fragmentState;
        private String tag;

        public INFragment(String className, Bundle fragmentState, String tag) {
            this.className = className;
            this.fragmentState = fragmentState;
            this.tag = tag;
        }

        private INFragment(Parcel in) {
            className = in.readString();
            fragmentState = in.readParcelable(Bundle.class.getClassLoader());
            tag = in.readString();
        }

        public String getTag() {
            return tag;
        }

        @Nullable
        public Fragment getFragment() {
            Fragment object = null;
            try {
                Class<?> targetClass = Class.forName(className);
                object = (Fragment) targetClass.getConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (object != null) {
                object.setArguments(fragmentState);
            }
            return object;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(className);
            dest.writeParcelable(fragmentState, flags);
            dest.writeString(tag);
        }

        public static final Creator<INFragment>
                CREATOR = new Creator<INFragment>() {
            public INFragment createFromParcel(Parcel in) {
                return new INFragment(in);
            }

            public INFragment[] newArray(int size) {
                return new INFragment[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbar().setTitle("");
    }

}
