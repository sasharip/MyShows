package com.myshows.studentapp.rest.interceptors;

import com.myshows.studentapp.rest.model.Cookies;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class CookiesReceiverInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        Cookies receivedCookies = Cookies.getCookies();
        if (receivedCookies == null) receivedCookies = new Cookies();

        List<String> cookieHeaders = originalResponse.headers("Set-Cookie");
        if (!cookieHeaders.isEmpty()) {
            for (String cookieHeader : cookieHeaders) {
                String[] cookies = cookieHeader.trim().split(";|,");
                if (cookies.length > 0) {
                    for (String cookie : cookies) {
                        String[] keyValue = cookie.split("=");
                        switch (keyValue[0]) {
                            case Cookies.SITE_USER_LOGIN:
                                receivedCookies.login = keyValue[1];
                                break;
                            case Cookies.SITE_USER_PASSWORD:
                                receivedCookies.passwordHash = keyValue[1];
                                break;
                            case Cookies.PHPSESSID:
                                receivedCookies.phpSessionId = keyValue[1];
                                break;
                        }
                    }
                }
            }
            receivedCookies.save();
        }
        return originalResponse;
    }

}