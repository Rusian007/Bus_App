package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class TicketRequestBody {

    @SerializedName("route_id")
    private String routeId;

    @SerializedName("seats")
    private String seats;

    public TicketRequestBody(String routeId, String seats) {
        this.routeId = routeId;
        this.seats = seats;
    }


}
