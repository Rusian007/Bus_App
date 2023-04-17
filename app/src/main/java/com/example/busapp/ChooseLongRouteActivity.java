package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.Booking.LongRouteBooking.LongRouteBookingStartActivity;
import com.example.busapp.Booking.LongRouteBooking.SelectBusActivity;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;
import com.example.busapp.Database.Database;

public class ChooseLongRouteActivity extends AppCompatActivity {
    Database db;

    TextView UsernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.choose_main);

        db = new Database(ChooseLongRouteActivity.this);

        UsernameText = findViewById(R.id.name);
        String username = db.GetUsername(db);
        UsernameText.setText(username);
        boolean tokenEmpty = db.IsTokenTableEmpty(db);
    }

    public void goToSalesView(View view){
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
    }
    public void goToBookingView(View view){
        Intent intent = new Intent(this, LongRouteBookingStartActivity.class);
        startActivity(intent);
    }
    public void finishActivity(View v){
        this.finish();
    }

}
