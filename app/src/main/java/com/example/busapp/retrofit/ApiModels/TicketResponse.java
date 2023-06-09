package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class TicketResponse {
    @SerializedName("ticket_id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
