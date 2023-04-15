package com.example.busapp.Model;

public class ShortRoute_LocationModel {

    String start_location;
    int Id;
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
    public ShortRoute_LocationModel(String start_location, String price, int id) {
        this.start_location = start_location;
        this.price = price;
        this.Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
