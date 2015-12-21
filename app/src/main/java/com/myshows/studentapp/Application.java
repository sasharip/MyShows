package com.myshows.studentapp;


import android.content.Context;

import com.facebook.FacebookSdk;
import com.myshows.studentapp.rest.model.Cookies;
import com.myshows.studentapp.utils.CookiesUtil;
import com.vk.sdk.VKSdk;

public class Application extends android.app.Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        VKSdk.initialize(context);
        FacebookSdk.sdkInitialize(context);
    }

    public static Cookies getCookies() {
        return CookiesUtil.get(context);
    }

    public static boolean saveCookies(Cookies cookies) {
        return CookiesUtil.save(context, cookies);
    }

    public static void deleteCookies() {
        CookiesUtil.delete(context);
    }

}
