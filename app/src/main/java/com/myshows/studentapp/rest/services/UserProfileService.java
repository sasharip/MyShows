package com.myshows.studentapp.rest.services;

import com.myshows.studentapp.model.User;

import retrofit.Call;
import retrofit.http.GET;

public interface UserProfileService {

    @GET("/profile/")
    Call<User> getUserData();

}
