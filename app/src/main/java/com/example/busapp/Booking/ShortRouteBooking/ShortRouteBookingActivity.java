package com.example.busapp.Booking.ShortRouteBooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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


        /// Dummy data - REMOVE in production
        // call API - start a method
        toLocations.add(new ShortRoute_LocationModel("Mawa"));
        toLocations.add(new ShortRoute_LocationModel("Dhaka"));
        toLocations.add(new ShortRoute_LocationModel("Barishal"));
        toLocations.add(new ShortRoute_LocationModel("Khulna"));

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



    public void popWindow(){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.btn_confirm_pop_view, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it

       // popupView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom));

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.setAnimationStyle(com.google.android.material.R.style.Animation_Design_BottomSheetDialog);

        ((TextView)popupWindow.getContentView().findViewById(R.id.fromLocation)).setText(FromLocationSelected);
        ((TextView) popupWindow.getContentView().findViewById(R.id.toLocation)).setText(endLocationSelected.get(0));



        // Set amount based on Bus fair
        // Change this on Production
        // Delete this part and calculate for different bus fair
        TextView amount = popupWindow.getContentView().findViewById(R.id.shortRoute_amount);
        amount.setText("500");


        // show the popup window
        popupWindow.showAtLocation(getCurrentFocus(), Gravity.BOTTOM, 0, 0);



        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
    }


    // print button On click, print ticket here
    // make necessary changes
    public void Print_Short_Route(View view){
        Log.d("TAG", "Print_Short_Route: " +"****************");
    }

    @Override
    public boolean AddDestinationLocation(String locationName) {
        endLocationSelected.add(locationName);
        checkLocations();
        return true;
    }

    @Override
    public void RemoveDestinationLocation(String locationName) {
        endLocationSelected.remove(locationName);
    }



    private void checkLocations() {
        if (endLocationSelected.size() > 0 ){
            popWindow();
        }
    }
}
