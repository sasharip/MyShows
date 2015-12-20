package com.myshows.studentapp.rest.services;

import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface LoginService {

    String VK = "vk";
    String FB = "fb";

    @GET("/profile/login")
    Call<ResponseBody> login(@Query("login") String login,
                             @Query("password") String password);

    @GET("/profile/login/{service}")
    Call<ResponseBody> loginSocial(@Path("service") String loginService,
                                   @Query("token") String token,
                                   @Query("userId") String userId);
}
