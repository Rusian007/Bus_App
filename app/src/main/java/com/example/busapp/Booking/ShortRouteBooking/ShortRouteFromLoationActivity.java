package com.example.busapp.Booking.ShortRouteBooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;

import java.util.ArrayList;

public class ShortRouteFromLoationActivity extends AppCompatActivity implements FromLocationAdapter.IStartLocation {
    ArrayList<ShortRoute_LocationModel> fromLocations = new ArrayList<>();
    ArrayList<String> startLocationSelected = new ArrayList<>();
    boolean clicked = false;
    Button nextBTN;

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

        setContentView(R.layout.short_route_from_location);

        /// Dummy data - REMOVE in production
        // call API
        fromLocations.add(new ShortRoute_LocationModel("Kakrail"));
        fromLocations.add(new ShortRoute_LocationModel("Dhaka"));
        fromLocations.add(new ShortRoute_LocationModel("Mawa"));
        fromLocations.add(new ShortRoute_LocationModel("Barishal"));
        fromLocations.add(new ShortRoute_LocationModel("Mothijhil"));

        initialize();
    }

    public void initialize(){
        // From List Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fromLocation_recyclerView);
        FromLocationAdapter adapter = new FromLocationAdapter(this, fromLocations, this);
        // setting grid layout manager to implement grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean AddStartLocation(String locationName) {
        startLocationSelected.add(locationName);
        return true;
    }

    @Override
    public void RemoveStartLocation(String locationName) {
        startLocationSelected.remove(locationName);
    }

    public void GoToNext_View(View view){
        if (startLocationSelected.size()>0){
            Intent intent = new Intent(getApplicationContext(), ShortRouteBookingActivity.class);

            intent.putExtra("FromLocation", startLocationSelected.get(0));
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
        } else{
            Toast.makeText(getApplicationContext(),"You must select a location",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean isSelected() {
        if (startLocationSelected.isEmpty() && !clicked){
            clicked = true;
            return false;
        } else if(startLocationSelected.size() > 0 && clicked){
            clicked = false;
            return true;
        } else{
            return true;
        }

    }
    public void finishActivity(View v){
        this.finish();
    }

}
