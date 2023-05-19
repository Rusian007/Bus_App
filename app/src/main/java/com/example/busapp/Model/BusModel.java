package com.example.busapp.Model;

public class BusModel {
    // variables for storing our Bus name.
    private String Busname;

    public BusModel(String name) {
        this.Busname = name;
    }

    public String getBusname() {
        return Busname;
    }

    public void setBusname(String busname) {
        Busname = busname;
    }
}
