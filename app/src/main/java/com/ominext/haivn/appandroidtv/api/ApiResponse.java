package com.ominext.haivn.appandroidtv.api;

import com.ominext.haivn.appandroidtv.model.ListItem;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiResponse {

    @GET("/drive/v2/files")
    Call<ListItem> getListFile(@Header("Authorization") String token);
}
