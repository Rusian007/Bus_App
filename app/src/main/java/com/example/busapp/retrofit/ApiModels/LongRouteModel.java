package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LongRouteModel {
    @SerializedName("list_of_locations")
    private List<LongRouteModel.Locations> locations;

    public List<LongRouteModel.Locations> getRoutes() {
        return locations;
    }

    public void setRoutes(List<LongRouteModel.Locations> locations) {
        this.locations = locations;
    }

    public static class Locations{
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("address")
        private String address;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

}
