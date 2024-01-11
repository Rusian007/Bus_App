package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.SalesApiModel;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SalesApi {

    @GET("ticket/sales/")
    Call<SalesApiModel> getSales(@Header("Authorization") String token, @Query("date") String date);
}
