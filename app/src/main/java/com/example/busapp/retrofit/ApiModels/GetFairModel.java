package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class GetFairModel {
    @SerializedName("fair")
    public double fair;

    public double getFair() {
        return fair;
    }

    public void setFair(double fair) {
        this.fair = fair;
    }
}
