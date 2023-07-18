package com.example.busapp.retrofit.ApiModels;
import com.google.gson.annotations.SerializedName;

public class RouteRequestModel {
    @SerializedName("route")
    private Route route;

    public Route getRoute() {
        return route;
    }

    public static class Route {
        @SerializedName("id")
        private int id;

        @SerializedName("route_name")
        private String routeName;

        @SerializedName("distance")
        private String distance;

        @SerializedName("fair")
        private int fair;

        @SerializedName("offset")
        private int offset;

        @SerializedName("starting_location")
        private int startingLocation;

        @SerializedName("ending_location")
        private int endingLocation;

        @SerializedName("bus_category")
        private int busCategory;

        public int getId() {
            return id;
        }

        public String getRouteName() {
            return routeName;
        }

        public String getDistance() {
            return distance;
        }

        public int getFair() {
            return fair;
        }

        public int getOffset() {
            return offset;
        }

        public int getStartingLocation() {
            return startingLocation;
        }

        public int getEndingLocation() {
            return endingLocation;
        }

        public int getBusCategory() {
            return busCategory;
        }
    }
}
