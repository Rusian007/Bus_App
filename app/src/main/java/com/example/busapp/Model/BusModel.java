package com.example.busapp.Model;

public class BusModel {
    // variables for storing our Bus name.
    private String Busname;
    private int ID;

    public BusModel(String busname, int ID) {
        Busname = busname;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBusname() {
        return Busname;
    }

    public void setBusname(String busname) {
        Busname = busname;
    }
}
