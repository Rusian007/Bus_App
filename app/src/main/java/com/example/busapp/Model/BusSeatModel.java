package com.example.busapp.Model;

public class BusSeatModel {
    String SeatNumber;

    public BusSeatModel(String seatNumber) {
        SeatNumber = seatNumber;
    }

    public String getSeatNumber() {
        return SeatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        SeatNumber = seatNumber;
    }
}
