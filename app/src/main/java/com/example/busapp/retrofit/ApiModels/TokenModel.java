package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class TokenModel {


    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

}
