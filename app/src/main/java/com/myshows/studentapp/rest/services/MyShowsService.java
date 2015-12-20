package com.myshows.studentapp.rest.services;

import com.myshows.studentapp.model.Show;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;

public interface MyShowsService {

    @GET("/profile/shows/")
    Call<ArrayList<Show>> loadShows();

}
