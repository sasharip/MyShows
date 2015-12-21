package com.myshows.studentapp.rest.interceptors;

import com.myshows.studentapp.Application;
import com.myshows.studentapp.rest.model.Cookies;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class SessionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Cookies cookies = Application.getCookies();
        if (cookies != null && !cookies.toString().equals("")) {
            builder.addHeader("Cookie", cookies.toString());
        }
        return chain.proceed(builder.build());
    }

}
