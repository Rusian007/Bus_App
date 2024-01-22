package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class DiscountLimitResponse {
    @SerializedName("max_limit")
    private String max_limit;

    public String getMax_limit() {
        return max_limit;
    }

    public void setMax_limit(String max_limit) {
        this.max_limit = max_limit;
    }
}
