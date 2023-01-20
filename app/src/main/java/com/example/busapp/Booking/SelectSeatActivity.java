package com.example.busapp.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusSeatAdapter;
import com.example.busapp.Model.BusSeatModel;
import com.example.busapp.R;

import java.util.ArrayList;
import java.util.List;

public class SelectSeatActivity extends AppCompatActivity {
    RecyclerView busSeatRecycleView;
    BusSeatAdapter adapter;
    ImageButton back;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.bus_seat_view);

        String BusName = (String) getIntent().getStringExtra("BUSNAME");

        TextView busNameText = findViewById(R.id.busNumber);
        busNameText.setText(BusName);
        back = (ImageButton) findViewById(R.id.backbtn);
        busSeatRecycleView = findViewById(R.id.busSeatRecycleView);




        // Set recycle view
        setRecycleView();
    }

    //On Back button click
    public void gotoBack(View view) {
        Intent intent = new Intent(this, SelectBusActivity.class );
        startActivity(intent);
        this.finish();
    }

    private void setRecycleView() {
        busSeatRecycleView.setHasFixedSize(true);
        busSeatRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusSeatAdapter(this, getSeatList() );
        busSeatRecycleView.setAdapter(adapter);
    }

    private List<BusSeatModel> getSeatList(){
        List<BusSeatModel> BusSeatList = new ArrayList<>();

        //Dummy Data

        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("A1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("F1", "B1", "X", "D1", "E1"));
        BusSeatList.add(new BusSeatModel("X1", "B1", "X", "D1", "E1"));


        return BusSeatList;
    }
}
