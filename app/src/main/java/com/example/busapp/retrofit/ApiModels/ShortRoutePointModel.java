package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShortRoutePointModel {
    @SerializedName("list_of_points")
    private List<ShortRoutePointModel.Route> routes;

    @SerializedName("error")
    private String error;

    public String getError() {
        return error;
    }

    public List<ShortRoutePointModel.Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<ShortRoutePointModel.Route> routes) {
        this.routes = routes;
    }

    public static class Route {
        @SerializedName("name")
        private String name;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

        public String getName() {
            return name;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

}
