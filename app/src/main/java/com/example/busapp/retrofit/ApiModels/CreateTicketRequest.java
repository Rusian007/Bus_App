package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CreateTicketRequest {
    @SerializedName("starting_location_id")
    private int startingLocationId;

    @SerializedName("ending_location_id")
    private int endingLocationId;

    @SerializedName("bus_id")
    private int busId;

    @SerializedName("category_id")
    private int category_id;

    @SerializedName("seats")
    private String seats;

    @SerializedName("discount")
    private int discount;

    public CreateTicketRequest(int startingLocationId, int endingLocationId, int category_id, int busId, String seats, int discount) {
        this.startingLocationId = startingLocationId;
        this.endingLocationId = endingLocationId;
        this.category_id = category_id;
        this.busId = busId;
        this.seats = seats;
        this.discount = discount;
    }

    public int getStartingLocationId() {
        return startingLocationId;
    }

    public void setStartingLocationId(int startingLocationId) {
        this.startingLocationId = startingLocationId;
    }

    public int getEndingLocationId() {
        return endingLocationId;
    }

    public void setEndingLocationId(int endingLocationId) {
        this.endingLocationId = endingLocationId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}