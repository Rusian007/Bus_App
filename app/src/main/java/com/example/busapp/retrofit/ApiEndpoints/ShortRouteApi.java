package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.ApiModels.ShortRoutePointModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface ShortRouteApi {
    @GET("routes/routes/")
    Call<ShortRouteModel> getShortRoutes(@Header("Authorization") String token);

    @GET("routes/points/")
    Call<ShortRoutePointModel> getShortRoutePoints(@Header("Authorization") String token);
}
