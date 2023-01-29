package com.example.busapp.Booking.ShortRouteBooking;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;
import com.google.android.flexbox.FlexDirection;
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

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this.getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // To List Adapter
        RecyclerView toListRecyclerView = findViewById(R.id.toLocation_recycleView);
        ToLocationAdapter toAdapter = new ToLocationAdapter(this, toLocations);
        FlexboxLayoutManager toLayoutManager = new FlexboxLayoutManager(this.getApplicationContext());

        toLayoutManager.setFlexDirection(FlexDirection.ROW);

        toListRecyclerView.setLayoutManager(toLayoutManager);
        toListRecyclerView.setAdapter(toAdapter);
    }
}
