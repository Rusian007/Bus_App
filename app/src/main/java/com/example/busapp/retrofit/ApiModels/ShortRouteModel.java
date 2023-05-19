package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShortRouteModel {
    @SerializedName("list_of_routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public static class Route {
        @SerializedName("id")
        private int id;

        @SerializedName("starting_point_name")
        private String startingPointName;

        @SerializedName("ending_point_name")
        private String endingPointName;

        @SerializedName("fair")
        private int fair;

        @SerializedName("distance")
        private Double distance;

        @SerializedName("starting_point")
        private int startingPoint;

        @SerializedName("ending_point")
        private int endingPoint;

        // getters and setters
        // ...

        public int getId() {
            return id;
        }

        public String getStartingPointName() {
            return startingPointName;
        }

        public String getEndingPointName() {
            return endingPointName;
        }

        public int getFair() {
            return fair;
        }

        public Double getDistance() {
            return distance;
        }

        public int getStartingPoint() {
            return startingPoint;
        }

        public int getEndingPoint() {
            return endingPoint;
        }
    }
}
