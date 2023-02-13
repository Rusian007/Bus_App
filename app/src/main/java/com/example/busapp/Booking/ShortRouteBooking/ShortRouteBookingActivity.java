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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class ShortRouteBookingActivity extends AppCompatActivity implements ToLocationAdapter.IEndLocation {

    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();
    String FromLocationSelected;
    Button printbtn;
    boolean clicked = false;
    View popView;
    boolean isUp ;
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

        popView = findViewById(R.id.pop_constraintLayout);
        fromLoc = findViewById(R.id.fromLocation);
        toLoc = findViewById(R.id.toLocation);
        amount = findViewById(R.id.shortRoute_amount);
        printbtn = (Button) findViewById(R.id.ShortprintBtn);

        /// Dummy data - REMOVE in production
        // call API - start a method
        toLocations.add(new ShortRoute_LocationModel("Mawa"));
        toLocations.add(new ShortRoute_LocationModel("Dhaka"));
        toLocations.add(new ShortRoute_LocationModel("Barishal"));
        toLocations.add(new ShortRoute_LocationModel("Khulna"));

        popView.getBackground().setAlpha(0);
        printbtn.setVisibility(View.INVISIBLE);

        initRecycleView();

    }


    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                printbtn.setVisibility(View.INVISIBLE);
            }
        }, 500);

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick() {
        if (isUp) {
            slideDown(popView);

        } else {
            slideUp(popView);
            popView.getBackground().setAlpha(255);
            printbtn.setVisibility(View.VISIBLE);
        }
        isUp = !isUp;
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
        Log.d("TAG", "Print_Short_Route: " +"****************");
    }

    @Override
    public boolean AddDestinationLocation(String locationName) {
        endLocationSelected.add(locationName);
        onSlideViewButtonClick();
        checkLocations();
        return true;
    }

    @Override
    public void RemoveDestinationLocation(String locationName) {
        endLocationSelected.remove(locationName);
        onSlideViewButtonClick();
    }

    @Override
    public boolean isSelected() {
        if (endLocationSelected.size()>0 && !clicked){
            clicked = true;
            return true;
        }
        else{
            clicked = false;
            return false;
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
}
