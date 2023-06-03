package com.example.busapp.retrofit.ApiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBookedSeatsModel {
    @SerializedName("booked_seats")
    private List<BookedSeat> bookedSeats;

    public List<BookedSeat> getBookedSeats() {
        return bookedSeats;
    }

    public class BookedSeat {
        @SerializedName("seat_no")
        private String seatNo;

        public String getSeatNo() {
            return seatNo;
        }
    }
}