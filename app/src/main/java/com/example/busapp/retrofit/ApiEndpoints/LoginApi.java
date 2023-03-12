package com.example.busapp.retrofit.ApiEndpoints;

import com.example.busapp.retrofit.ApiModels.TokenModel;
import com.example.busapp.retrofit.RequestModel.LoginCredentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginApi {

    @GET("get-auth-token")
    Call<TokenModel> login();


    @POST("get-auth-token/")
    Call<TokenModel> loginUser(@Body LoginCredentials req);
}
