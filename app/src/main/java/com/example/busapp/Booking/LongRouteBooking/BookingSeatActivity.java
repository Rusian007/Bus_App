package com.example.busapp.Booking.LongRouteBooking;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class BookingSeatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_booking_seat);
    }

    // go back to home page if booking is cancelled
    public void Cancel_booking_onClick(View view){
        Intent intent = new Intent(this, ChooseLongRouteActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void Print_ticket_OnClick(View view){
        Toast.makeText(getApplicationContext(),"Printing Ticket ....", Toast.LENGTH_SHORT).show();
    }

}