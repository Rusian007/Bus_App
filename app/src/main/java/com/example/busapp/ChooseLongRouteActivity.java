package com.example.busapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.Booking.LongRouteBooking.LongRouteBookingStartActivity;
import com.example.busapp.Booking.LongRouteBooking.SelectBusActivity;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;
import com.example.busapp.Database.Database;

public class ChooseLongRouteActivity extends AppCompatActivity {
    Database db;
    TextView logoutTextView;
    TextView UsernameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.choose_main);
        logoutTextView=findViewById(R.id.bookingText);
        Logout();
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

    public void Logout(){
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action when TextView is clicked
                if(db.IsTokenTableEmpty(db)){
                    Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_SHORT).show();
                } else {

                    if (db.DeleteToken(db)){
                        Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ChooseRouteActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(getApplicationContext(), "Internal error, not logged out", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }



}
