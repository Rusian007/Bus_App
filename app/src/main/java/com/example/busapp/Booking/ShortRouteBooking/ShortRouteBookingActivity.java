package com.example.busapp.Booking.ShortRouteBooking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusLayoutAdapter;
import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class ShortRouteBookingActivity extends AppCompatActivity {
    ArrayList<ShortRoute_LocationModel> fromLocations = new ArrayList<>();
    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.short_route_view);

        // Test



        // - end


        /// Dummy data - REMOVE in production
        // call API
        fromLocations.add(new ShortRoute_LocationModel("Kakrail"));
        fromLocations.add(new ShortRoute_LocationModel("Dhaka"));
        fromLocations.add(new ShortRoute_LocationModel("Mawa"));
        fromLocations.add(new ShortRoute_LocationModel("Barishal"));
        fromLocations.add(new ShortRoute_LocationModel("Mothijhil"));

        /// Dummy data - REMOVE in production
        // call API - start a method
        toLocations.add(new ShortRoute_LocationModel("Mawa"));
        toLocations.add(new ShortRoute_LocationModel("Dhaka"));
        toLocations.add(new ShortRoute_LocationModel("Barishal"));
        toLocations.add(new ShortRoute_LocationModel("Khulna"));

        initRecycleView();

    }

    private void initRecycleView() {
        // From List Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fromLocation_recyclerView);
        FromLocationAdapter adapter = new FromLocationAdapter(this, fromLocations);
        // setting grid layout manager to implement grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // To List Adapter
        RecyclerView toListRecyclerView = findViewById(R.id.toLocation_recycleView);
        ToLocationAdapter toAdapter = new ToLocationAdapter(this, toLocations);
        GridLayoutManager toLayoutManager = new GridLayoutManager(this, 3);
        toListRecyclerView.setLayoutManager(toLayoutManager);
        toListRecyclerView.setAdapter(toAdapter);
    }



    public void popWindow(View view){
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.btn_confirm_pop_view, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TAG", "onTouch: ************");
                return true;
            }
        });
    }

}
