package com.example.busapp.Model;

public class ShortRoute_LocationModel {

    String start_location;
    String price;
    boolean is_clicked = false;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ShortRoute_LocationModel(String start_location) {
        this.start_location = start_location;
    }
    public ShortRoute_LocationModel(String start_location, String price) {
        this.start_location = start_location;
        this.price = price;
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
