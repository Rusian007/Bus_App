package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.Booking.SelectBusActivity;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.choose_main);
    }

    public void goToSalesView(View view){
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
    }
    public void goToBookingView(View view){
        Intent intent = new Intent(this, SelectBusActivity.class);
        startActivity(intent);
    }
    public void goToShortRoute_View(View view){
        Intent intent = new Intent(this, ShortRouteBookingActivity.class);
        startActivity(intent);
    }

}
