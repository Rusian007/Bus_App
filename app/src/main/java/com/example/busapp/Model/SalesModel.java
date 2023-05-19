package com.example.busapp.Model;

public class SalesModel {
    private String information;
    private boolean setBold;

    public SalesModel(String information, boolean bold) {
        this.information = information;
        this.setBold = bold;
    }

    public boolean isSetBold() {
        return setBold;
    }

    public void setSetBold(boolean setBold) {
        this.setBold = setBold;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
