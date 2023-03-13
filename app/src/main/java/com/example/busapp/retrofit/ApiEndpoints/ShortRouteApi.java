package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.ShortRouteModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ShortRouteApi {
    @GET("get-auth-token")
    Call<ShortRouteModel> getShortRoutes(@Header("Authorization") String token);
}
