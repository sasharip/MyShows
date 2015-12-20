package com.myshows.studentapp;


import com.activeandroid.ActiveAndroid;
import com.facebook.FacebookSdk;
import com.vk.sdk.VKSdk;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ActiveAndroid.initialize(this);
        VKSdk.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
