package com.example.busapp.Model;

public class ShortRoute_FromLocation_Model {

    String start_location;
    boolean is_clicked = false;

    public ShortRoute_FromLocation_Model(String start_location) {
        this.start_location = start_location;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public boolean isIs_clicked() {
        return is_clicked;
    }

    public void setIs_clicked(boolean is_clicked) {
        this.is_clicked = is_clicked;
    }
}
