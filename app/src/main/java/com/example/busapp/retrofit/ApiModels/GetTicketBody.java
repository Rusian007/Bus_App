package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

public class GetTicketBody {
    @SerializedName("ticket")
    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    public static class Ticket {
        @SerializedName("id")
        private int id;

        @SerializedName("starting_location_id")
        private int startingLocationId;

        @SerializedName("ending_location_id")
        private int endingLocationId;

        @SerializedName("starting_location_name")
        private String startingLocationName;

        @SerializedName("ending_location_name")
        private String endingLocationName;

        @SerializedName("starting_location_address")
        private String startingLocationAddress;

        @SerializedName("ending_location_address")
        private String endingLocationAddress;

        @SerializedName("date")
        private String date;

        @SerializedName("time")
        private String time;

        @SerializedName("bus_id")
        private int busId;

        @SerializedName("bus_number")
        private String busNumber;

        @SerializedName("category")
        private String category;

        @SerializedName("fair")
        private int fair;

        @SerializedName("discount")
        private int discount;

        @SerializedName("counterman_id")
        private int countermanId;

        @SerializedName("counterman_username")
        private String countermanUsername;

        @SerializedName("phone_no")
        private String phoneNumber;

        @SerializedName("counterman_assigned_counter")
        private String countermanAssignedCounter;

        @SerializedName("counterman_address")
        private String countermanAddress;

        @SerializedName("counterman_division")
        private String countermanDivision;

        public int getId() {
            return id;
        }

        public int getStartingLocationId() {
            return startingLocationId;
        }

        public int getEndingLocationId() {
            return endingLocationId;
        }

        public String getStartingLocationName() {
            return startingLocationName;
        }

        public String getEndingLocationName() {
            return endingLocationName;
        }

        public String getStartingLocationAddress() {
            return startingLocationAddress;
        }

        public String getEndingLocationAddress() {
            return endingLocationAddress;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public int getBusId() {
            return busId;
        }

        public String getBusNumber() {
            return busNumber;
        }

        public String getCategory() {
            return category;
        }

        public int getFair() {
            return fair;
        }

        public int getDiscount() {
            return discount;
        }

        public int getCountermanId() {
            return countermanId;
        }

        public String getCountermanUsername() {
            return countermanUsername;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getCountermanAssignedCounter() {
            return countermanAssignedCounter;
        }

        public String getCountermanAddress() {
            return countermanAddress;
        }

        public String getCountermanDivision() {
            return countermanDivision;
        }
    }
}
