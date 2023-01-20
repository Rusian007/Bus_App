package com.example.busapp.Model;

public class BusModal {
    // variables for storing our Bus name.
    private String Busname;

    public BusModal(String name) {
        this.Busname = name;
    }

    public String getBusname() {
        return Busname;
    }

    public void setBusname(String busname) {
        Busname = busname;
    }
}
