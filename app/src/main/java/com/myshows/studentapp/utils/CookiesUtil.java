package com.myshows.studentapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.myshows.studentapp.rest.model.Cookies;

public class CookiesUtil {

    private static final String SP_COOKIES = "com.myshows.studentapp.SP_COOKIES";
    private static final String SP_PHPSESSID = "com.myshows.studentapp.SP_PHPSESSID";
    private static final String SP_LOGIN = "com.myshows.studentapp.SP_LOGIN";
    private static final String SP_PASSWORD_HASH = "com.myshows.studentapp.SP_PASSWORD_HASH";

    public static Cookies get(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_COOKIES, Context.MODE_PRIVATE);
        Cookies cookies = new Cookies();
        cookies.phpSessionId = sp.getString(SP_PHPSESSID, null);
        cookies.login = sp.getString(SP_LOGIN, null);
        cookies.passwordHash = sp.getString(SP_PASSWORD_HASH, null);
        return cookies;
    }

    public static boolean save(Context context, Cookies cookies) {
        if (cookies != null
                && cookies.phpSessionId != null
                && cookies.login != null
                && cookies.passwordHash != null) {
            context.getSharedPreferences(SP_COOKIES, Context.MODE_PRIVATE)
                    .edit()
                    .putString(SP_PHPSESSID, cookies.phpSessionId)
                    .putString(SP_LOGIN, cookies.login)
                    .putString(SP_PASSWORD_HASH, cookies.passwordHash)
                    .apply();
            return true;
        }
        return false;
    }

    public static void delete(Context context) {
        context.getSharedPreferences(SP_COOKIES, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

}
