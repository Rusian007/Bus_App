package com.example.busapp.Booking.ShortRouteBooking;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.ChooseRouteActivity;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class ShortRouteBookingActivity extends AppCompatActivity implements ToLocationAdapter.IEndLocation {

    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();
    String FromLocationSelected;
    Button printbtn;
    boolean clicked= false ;
    private TextView fromLoc, toLoc, amount;


    ArrayList<String> endLocationSelected = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.short_route_to_location);

        Intent intent = getIntent();
        FromLocationSelected = intent.getStringExtra("FromLocation");

        fromLoc = findViewById(R.id.fromLocation);
        toLoc = findViewById(R.id.toLocation);
        amount = findViewById(R.id.shortRoute_amount);
        printbtn = (Button) findViewById(R.id.ShortprintBtn);


        /// Dummy data - REMOVE in production
        // call API - start a method
        toLocations.add(new ShortRoute_LocationModel("Mawa", "500/-"));
        toLocations.add(new ShortRoute_LocationModel("Dhaka", "55/-"));
        toLocations.add(new ShortRoute_LocationModel("Barishal", "1000/-"));
        toLocations.add(new ShortRoute_LocationModel("Khulna","2000/-"));

        printbtn.setVisibility(View.INVISIBLE);
        initRecycleView();

    }


    private void initRecycleView() {


        // To List Adapter
        RecyclerView toListRecyclerView = findViewById(R.id.toLocation_recycleView);
        ToLocationAdapter toAdapter = new ToLocationAdapter(this, toLocations, this);
        GridLayoutManager toLayoutManager = new GridLayoutManager(this, 3);
        toListRecyclerView.setLayoutManager(toLayoutManager);
        toListRecyclerView.setAdapter(toAdapter);
    }


    // print button On click, print ticket here
    // make necessary changes
    public void Print_Short_Route(View view){
        if(endLocationSelected.size()>0){
            Toast.makeText(getApplicationContext(),"Your Ticket is being printed !", Toast.LENGTH_SHORT).show();
            // Print ticket here
            // Change later
            Intent intent = new Intent(this, ShortRouteFromLoationActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, 1000);


        }
        else Toast.makeText(getApplicationContext(),"You must select an end location", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean AddDestinationLocation(String locationName) {
        if (endLocationSelected.isEmpty()){
            endLocationSelected.add(locationName);
            checkLocations();
            printbtn.setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void RemoveDestinationLocation(String locationName) {
        endLocationSelected.remove(locationName);
    }

    @Override
    public boolean isSelected() {
        if (endLocationSelected.isEmpty() && !clicked){
            clicked = true;
            return false;
        } else if(endLocationSelected.size() > 0 && clicked){
            clicked = false;
            return true;
        } else{
            return true;
        }

    }

// start printing button here
    private void checkLocations() {
        if (endLocationSelected.size() > 0 ){
            fromLoc.setText(FromLocationSelected);
            toLoc.setText(endLocationSelected.get(0));
            amount.setText("500");
        }
    }
    public void finishActivity(View v){
        Intent intent = new Intent(getApplicationContext(), ShortRouteFromLoationActivity.class);
        startActivity(intent);
        this.finish();
    }


}
