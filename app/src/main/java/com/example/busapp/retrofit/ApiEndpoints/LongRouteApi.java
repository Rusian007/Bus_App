package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.LongRouteBusModel;
import com.example.busapp.retrofit.ApiModels.LongRouteModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LongRouteApi {
    @GET("route/locations/")
    Call<LongRouteModel> getLongRoutes(@Header("Authorization") String token);

    @GET("bus/buses/")
    Call<LongRouteBusModel> getLongRouteBuses(@Header("Authorization") String token);

}
