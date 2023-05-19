package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class ShortRouteTicketModel {
    @SerializedName("ticket")
    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public static class Ticket {
        @SerializedName("id")
        private int id;
        @SerializedName("starting_point")
        private String startingPoint;
        @SerializedName("ending_point")
        private String endingPoint;
        @SerializedName("date")
        private String date;
        @SerializedName("time")
        private String time;
        @SerializedName("fair")
        private int fair;
        @SerializedName("discount")
        private int discount;
        @SerializedName("booked_by")
        private String bookedBy;
        @SerializedName("counterman_metadata")
        private CountermanMetadata countermanMetadata;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartingPoint() {
            return startingPoint;
        }

        public void setStartingPoint(String startingPoint) {
            this.startingPoint = startingPoint;
        }

        public String getEndingPoint() {
            return endingPoint;
        }

        public void setEndingPoint(String endingPoint) {
            this.endingPoint = endingPoint;
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

        public String getBookedBy() {
            return bookedBy;
        }

        public void setBookedBy(String bookedBy) {
            this.bookedBy = bookedBy;
        }

        public CountermanMetadata getCountermanMetadata() {
            return countermanMetadata;
        }

        public void setCountermanMetadata(CountermanMetadata countermanMetadata) {
            this.countermanMetadata = countermanMetadata;
        }
    }

    public static class CountermanMetadata {
        @SerializedName("phone_no")
        private String phoneNo;

        public String getPhoneNo() {
            return phoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }
    }
}
