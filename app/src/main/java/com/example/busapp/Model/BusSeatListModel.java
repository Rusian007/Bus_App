package com.example.busapp.Model;


public class BusSeatListModel {

    public class Seat {
        public String SeatName;
        public boolean isAvailable;
    }

    private Seat seatCol1 = new Seat();
    private Seat seatCol2 = new Seat();
    private Seat seatCol3 = new Seat();
    private Seat seatCol4= new Seat();
    private Seat seatCol5= new Seat();

    public String getSeatCol1() {
        return seatCol1.SeatName;
    }
    public boolean getSeatCol1Status(){
        return  seatCol1.isAvailable;
    }
    public void setSeatCol1(String SeatName, boolean isAvailable) {
        this.seatCol1.SeatName = SeatName;
        this.seatCol1.isAvailable = isAvailable;
    }



    public String getSeatCol2() {
        return seatCol2.SeatName;
    }

    public boolean getSeatCol2Status(){
        return  seatCol2.isAvailable;
    }

    public void setSeatCol2(String SeatName, boolean isAvailable) {
        this.seatCol2.SeatName = SeatName;
        this.seatCol2.isAvailable = isAvailable;
    }


    public String getSeatCol3() {
        return seatCol3.SeatName;
    }

    public boolean getSeatCol3Status(){
        return  seatCol3.isAvailable;
    }

    public void setSeatCol3(String SeatName, boolean isAvailable) {
        this.seatCol3.SeatName = SeatName;
        this.seatCol3.isAvailable = isAvailable;
    }


    public String getSeatCol4() {
        return seatCol4.SeatName;
    }

    public boolean getSeatCol4Status(){
        return  seatCol4.isAvailable;
    }

    public void setSeatCol4(String SeatName, boolean isAvailable) {
        this.seatCol4.SeatName = SeatName;
        this.seatCol4.isAvailable = isAvailable;
    }


    public String getSeatCol5() {
        return seatCol5.SeatName;
    }

    public boolean getSeatCol5Status(){
        return  seatCol5.isAvailable;
    }

    public void setSeatCol5(String SeatName, boolean isAvailable) {
        this.seatCol5.SeatName = SeatName;
        this.seatCol5.isAvailable = isAvailable;
    }

}
