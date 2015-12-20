package com.myshows.studentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myshows.studentapp.rest.model.Cookies;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cookies cookies = Cookies.getCookies();
        if (cookies != null
                && cookies.phpSessionId != null
                && cookies.login != null
                && cookies.passwordHash != null)
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
