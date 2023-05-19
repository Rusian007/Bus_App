package com.example.busapp.retrofit.ErrorsModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginError {
    @SerializedName("non_field_errors")
    private List<String> nonFieldErrors;

    public List<String> getNonFieldErrors() {
        return nonFieldErrors;
    }

    public void setNonFieldErrors(List<String> nonFieldErrors) {
        this.nonFieldErrors = nonFieldErrors;
    }
}
