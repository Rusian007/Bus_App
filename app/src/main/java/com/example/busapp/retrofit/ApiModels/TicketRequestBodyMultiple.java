package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketRequestBodyMultiple {
    @SerializedName("tickets")
    private List<TicketModel> tickets;

    public TicketRequestBodyMultiple(List<TicketModel> tickets) {
        this.tickets = tickets;
    }

    public List<TicketModel> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }



    public class TicketModel {
        @SerializedName("route_id")
        private String routeId;

        @SerializedName("seats")
        private String seats;

        public TicketModel(String routeId, String seats) {
            this.routeId = routeId;
            this.seats = seats;
        }

        public String getRouteId() {
            return routeId;
        }

        public void setRouteId(String routeId) {
            this.routeId = routeId;
        }

        public String getSeats() {
            return seats;
        }

        public void setSeats(String seats) {
            this.seats = seats;
        }
    }

}
