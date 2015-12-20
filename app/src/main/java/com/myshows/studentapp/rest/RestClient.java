package com.myshows.studentapp.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.myshows.studentapp.model.Show;
import com.myshows.studentapp.model.User;
import com.myshows.studentapp.model.eventbus.LoginMsg;
import com.myshows.studentapp.model.eventbus.Message;
import com.myshows.studentapp.model.eventbus.MyShowsMsg;
import com.myshows.studentapp.model.eventbus.UserProfileMsg;
import com.myshows.studentapp.rest.deserealizers.ShowDeserializer;
import com.myshows.studentapp.rest.interceptors.CookiesInterceptor;
import com.myshows.studentapp.rest.interceptors.CookiesReceiverInterceptor;
import com.myshows.studentapp.rest.model.Cookies;
import com.myshows.studentapp.rest.services.LoginService;
import com.myshows.studentapp.rest.services.MyShowsService;
import com.myshows.studentapp.rest.services.UserProfileService;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;

import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public final class RestClient {

    public static final String URL = "http://api.myshows.ru";

    private static volatile RestClient instance;

    public static RestClient getInstance() {
        RestClient localInstance = instance;
        if (localInstance == null) {
            synchronized (RestClient.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new RestClient();
                }
            }
        }
        return localInstance;
    }

    private RestClient() {
    }

    private Gson getGsonWithTypeAdapter(final Class type, Object typeAdapter) {
        return getGsonBuilder().registerTypeAdapter(type, typeAdapter).create();
    }

    private Gson getGson() {
        return getGsonBuilder().create();
    }

    private GsonBuilder getGsonBuilder() {
        return new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .excludeFieldsWithoutExposeAnnotation();
    }

    private OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
        client.setCookieHandler(cookieManager);

        client.interceptors().add(new CookiesInterceptor());
        client.interceptors().add(new CookiesReceiverInterceptor());
        return client;
    }

    private <T> T getRetrofitService(Gson gson, final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();
        return retrofit.create(service);
    }


    public void login(String login, String passwordMd5) {
        LoginService service = getRetrofitService(getGson(), LoginService.class);
        service.login(login, passwordMd5).enqueue(getAuthCallBack(false));
    }

    public void loginSocial(String loginService, String accessToken, String userId) {
        LoginService service = getRetrofitService(getGson(), LoginService.class);
        service.loginSocial(loginService, accessToken, userId).enqueue(getAuthCallBack(true));
    }

    private Callback<ResponseBody> getAuthCallBack(final boolean isSocial) {
        return new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                LoginMsg msg = new LoginMsg(Message.AUTH_ERROR, isSocial);
                if (response.code() == 200) {
                    Cookies cookies = Cookies.getCookies();
                    if (cookies != null
                            && cookies.phpSessionId != null
                            && cookies.login != null
                            && cookies.passwordHash != null) {
                        msg.setMessage(Message.OK);
                    }
                }
                EventBus.getDefault().post(msg);
            }

            @Override
            public void onFailure(Throwable t) {
                EventBus.getDefault().post(new LoginMsg(Message.ERROR, isSocial));
            }
        };
    }

    public void loadUserData() {
        UserProfileService service = getRetrofitService(getGson(), UserProfileService.class);
        service.getUserData().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response, Retrofit retrofit) {
                UserProfileMsg msg = new UserProfileMsg();
                switch (response.code()) {
                    case 200:
                        msg.setMessage(Message.OK);
                        msg.setUser(response.body());
                        break;
                    case 401:
                        msg.setMessage(Message.AUTH_REQUIRED);
                        break;
                    default:
                        msg.setMessage(Message.ERROR);
                }
                EventBus.getDefault().post(msg);
            }

            @Override
            public void onFailure(Throwable t) {
                EventBus.getDefault().post(new UserProfileMsg(Message.ERROR));
            }
        });
    }

    public void loadMyShows() {
        MyShowsService service = getRetrofitService(
                getGsonWithTypeAdapter(ArrayList.class, new ShowDeserializer()),
                MyShowsService.class);
        service.loadShows().enqueue(new Callback<ArrayList<Show>>() {
            @Override
            public void onResponse(Response<ArrayList<Show>> response, Retrofit retrofit) {
                MyShowsMsg msg = new MyShowsMsg();
                switch (response.code()) {
                    case 200:
                        msg.setMessage(Message.OK);
                        msg.setShows(response.body());
                        break;
                    case 401:
                        msg.setMessage(Message.AUTH_REQUIRED);
                        break;
                    default:
                        msg.setMessage(Message.ERROR);
                }
                EventBus.getDefault().post(msg);
            }

            @Override
            public void onFailure(Throwable t) {
                EventBus.getDefault().post(new MyShowsMsg(Message.ERROR));
            }
        });
    }

}
