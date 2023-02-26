package com.example.busapp.Booking.LongRouteBooking;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusLayoutAdapter;
import com.example.busapp.Model.BusModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class SelectBusActivity extends AppCompatActivity
{
    private ArrayList<BusModel> theBusNames = new ArrayList<>();
    String LocationText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.select_bus_view);

        // Location Intent
        String LocationText = getIntent().getStringExtra("LOCATION");

        //
        InitializeNamesArray();
    }
    private void InitializeNamesArray(){
        BusModel busname;
        // Call API
        try {

            theBusNames.add(new BusModel("DHA-BAN-1234"));
            theBusNames.add(new BusModel("DHA-BARI-222"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Start the View
        initRecycleView();
    }
    private void initRecycleView(){

        RecyclerView recyclerView = findViewById(R.id.BusrecycleView);
        BusLayoutAdapter adapter = new BusLayoutAdapter(this, theBusNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void finishActivity(View v){
        this.finish();
    }
}
