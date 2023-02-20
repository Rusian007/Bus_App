package com.example.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;

public class LoginActivity extends AppCompatActivity {
    Button button;
    String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.login_activity_view);
        Bundle bundle = getIntent().getExtras();
       route = bundle.getString("ROUTE"); // the selected route, long or short


        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextActivity();
            }
        });


    }



    private void openNextActivity() {
        if(route.equals("LONG")){
            Intent intent = new Intent(this, ChooseLongRouteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
        }
        else if (route.equals("SHORT")){
            Intent intent = new Intent(this, ShortRouteFromLoationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
        }

    }


}