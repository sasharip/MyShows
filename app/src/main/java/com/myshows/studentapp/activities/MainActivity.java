package com.myshows.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.myshows.studentapp.Application;
import com.myshows.studentapp.R;
import com.myshows.studentapp.fragments.MainFragment;
import com.myshows.studentapp.fragments.NoInternetFragment;
import com.myshows.studentapp.fragments.NoInternetFragment.INFragment;
import com.myshows.studentapp.tests.SimpleTests;
import com.myshows.studentapp.utils.Connectivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private INFragment iNFragment;

    public MainActivity(Class<SimpleTests> simpleTestsClass) {

    }

    public INFragment getInternetNeededFragment() {
        return iNFragment;
    }

    public void setInternetNeededFragment(INFragment iNFragment) {
        this.iNFragment = iNFragment;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        updateValuesFromBundle(savedInstanceState);
    }

    public void goToMain() {
        final String NAME = MainFragment.class.getName();
        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(NAME, 0);
        if (!fragmentPopped && getSupportFragmentManager().findFragmentByTag(NAME) == null) {
            switchScreen(new MainFragment(), NAME, true);
        }
    }

    public void switchScreen(final Fragment screen, String tag, boolean checkInternetState) {
        if (!checkInternetState || Connectivity.isConnected(this)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, screen, tag)
                    .addToBackStack(tag)
                    .commit();
        } else {
            INFragment iNFragment = NoInternetFragment.goToINFragment(this, screen, tag);
            if (iNFragment != null) {
                // if iNFragment == null, then NoInternetFragment was
                // in the backStack and has been popped
                this.iNFragment = iNFragment;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            Application.deleteCookies();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            goToMain();
        } else {
            if (savedInstanceState.containsKey(INFragment.INTERNET_NEEDED_FRAGMENT)) {
                iNFragment = savedInstanceState.getParcelable(INFragment.INTERNET_NEEDED_FRAGMENT);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (iNFragment != null) {
            outState.putParcelable(INFragment.INTERNET_NEEDED_FRAGMENT, iNFragment);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) finish();
    }

}
