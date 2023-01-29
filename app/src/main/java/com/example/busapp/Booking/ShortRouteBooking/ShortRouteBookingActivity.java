package com.example.busapp.Booking.ShortRouteBooking;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusLayoutAdapter;
import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Model.ShortRoute_FromLocation_Model;
import com.example.busapp.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

public class ShortRouteBookingActivity extends AppCompatActivity {
    ArrayList<ShortRoute_FromLocation_Model> fromLocations = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_route_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();


        /// Dummy data - REMOVE in production
        // call API
        fromLocations.add(new ShortRoute_FromLocation_Model("Kakrail"));
        fromLocations.add(new ShortRoute_FromLocation_Model("Dhaka"));
        fromLocations.add(new ShortRoute_FromLocation_Model("Mawa"));
        fromLocations.add(new ShortRoute_FromLocation_Model("Barishal"));

        initRecycleView();

    }

    private void initRecycleView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fromLocation_recyclerView);
        FromLocationAdapter adapter = new FromLocationAdapter(this, fromLocations);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this.getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
