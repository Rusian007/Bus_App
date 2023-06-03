package com.example.busapp.Model;


public class BusSeatListModel {

    public static class Seat {

        public String SeatName;
        public boolean isAvailable;
        public boolean isClicked = false;

        public Seat(String seatName, boolean isAvailable) {
            SeatName = seatName;
            this.isAvailable = isAvailable;
        }
        public Seat(){

        }
    }

    public BusSeatListModel(Seat col1,Seat col2, Seat col3, Seat col4, Seat col5 ){
        this.seatCol1 = col1;
        this.seatCol2 = col2;
        this.seatCol3 = col3;
        this.seatCol4 = col4;
        this.seatCol5 = col5;
    }
    public BusSeatListModel(){

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

    public boolean isSeatCol1Clicked(){
        return seatCol1.isClicked;
    }
    public boolean isSeatCol2Clicked(){
        return seatCol2.isClicked;
    }
    public boolean isSeatCol3Clicked(){
        return seatCol3.isClicked;
    }
    public boolean isSeatCol4Clicked(){
        return seatCol4.isClicked;
    }
    public boolean isSeatCol5Clicked(){
        return seatCol5.isClicked;
    }

    public void setIsClickedSeatCol1(boolean state){
        this.seatCol1.isClicked = state;
    }
    public void setIsClickedSeatCol2(boolean state){
        this.seatCol2.isClicked = state;
    }
    public void setIsClickedSeatCol3(boolean state){
        this.seatCol3.isClicked = state;
    }
    public void setIsClickedSeatCol4(boolean state){
        this.seatCol4.isClicked = state;
    }
    public void setIsClickedSeatCol5(boolean state){
        this.seatCol5.isClicked = state;
    }

}
