package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LongRouteSeatModel {
    @SerializedName("seat_structure")
    private List<List<String>> seatStructure;

    public List<List<String>> getSeatStructure() {
        return seatStructure;
    }
}
