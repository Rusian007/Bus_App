package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;

public class ChooseRouteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.route_choose_view);
    }
    public void goToShortRoute_View(View view){
        Intent intent = new Intent(this, ShortRouteFromLoationActivity.class);
        startActivity(intent);
    }

    public void goToLongRoute_View(View view){
        Intent intent = new Intent(this, ChooseLongRouteActivity.class);
        startActivity(intent);
    }
}
