package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class PhoneNumberResponse {
    @SerializedName("phone_number")
   private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
