package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LongRouteBusModel{
    @SerializedName("list_of_buses")
    private List<Bus> listOfBuses;

    public List<Bus> getListOfBuses() {
            return listOfBuses;
        }


    public class Bus {
        @SerializedName("id")
        private int id;

        @SerializedName("bus_number")
        private String busNumber;

        @SerializedName("active")
        private boolean active;

        @SerializedName("bus_category")
        private int busCategory;

        @SerializedName("seat_structure")
        private int seatStructure;

        @SerializedName("bus_category_name")
        private String busCategoryName;

        @SerializedName("bus_model")
        private String busModel;

        @SerializedName("seat_structure_json")
        private List<List<String>> seatStructureJson;

        @SerializedName("total_seats")
        private int totalSeats;

        public int getId() {
            return id;
        }

        public String getBusNumber() {
            return busNumber;
        }

        public boolean isActive() {
            return active;
        }

        public int getBusCategory() {
            return busCategory;
        }

        public int getSeatStructure() {
            return seatStructure;
        }

        public String getBusCategoryName() {
            return busCategoryName;
        }

        public String getBusModel() {
            return busModel;
        }

        public List<List<String>> getSeatStructureJson() {
            return seatStructureJson;
        }

        public int getTotalSeats() {
            return totalSeats;
        }
    }
}
