package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesApiModel {

    @SerializedName("tickets")
    private List<Ticket> tickets;

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public static class Ticket {
        @SerializedName("id")
        private int id;

        @SerializedName("starting_point")
        private String starting_point;

        @SerializedName("ending_point")
        private String ending_point;

        @SerializedName("date")
        private String date;

        @SerializedName("time")
        private String time;

        @SerializedName("fair")
        private int fair;

        @SerializedName("discount")
        private int discount;

        @SerializedName("counterman_username")
        private String booked_by;

        @SerializedName("bus_number")
        private String bus_number;

        @SerializedName("counterman_metadata")
        private CountermanMetadata counterman_metadata;

        // Getters and Setters for Ticket class fields


        public String getBus_number() {
            return bus_number;
        }

        public void setBus_number(String bus_number) {
            this.bus_number = bus_number;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStarting_point() {
            return starting_point;
        }

        public void setStarting_point(String starting_point) {
            this.starting_point = starting_point;
        }

        public String getEnding_point() {
            return ending_point;
        }

        public void setEnding_point(String ending_point) {
            this.ending_point = ending_point;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getFair() {
            return fair;
        }

        public void setFair(int fair) {
            this.fair = fair;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getBooked_by() {
            return booked_by;
        }

        public void setBooked_by(String booked_by) {
            this.booked_by = booked_by;
        }

        public CountermanMetadata getCounterman_metadata() {
            return counterman_metadata;
        }

        public void setCounterman_metadata(CountermanMetadata counterman_metadata) {
            this.counterman_metadata = counterman_metadata;
        }

        public static class CountermanMetadata {
            private String name;

            // Getters and Setters for CountermanMetadata class fields

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
