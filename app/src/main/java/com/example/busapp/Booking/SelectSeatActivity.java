package com.example.busapp.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusSeatAdapter;
import com.example.busapp.Model.BusSeatListModel;
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

    private List<BusSeatListModel> getSeatList(){
        List<BusSeatListModel> BusSeatList = new ArrayList<>();



            //Dummy Data - for test
        BusSeatListModel busSeatName = new BusSeatListModel();
        busSeatName.setSeat1("A1");
        busSeatName.setSeat2("A2");
        busSeatName.setSeat3("X");
        busSeatName.setSeat4("A3");
        busSeatName.setSeat5("A4");
        BusSeatList.add(busSeatName);

        busSeatName = new BusSeatListModel();
        busSeatName.setSeat1("B1");
        busSeatName.setSeat2("B2");
        busSeatName.setSeat3("X");
        busSeatName.setSeat4("B3");
        busSeatName.setSeat5("B4");
        BusSeatList.add(busSeatName);

        /*
        BusSeatList.add(new BusSeatListModel("A1","A2","X","A3","A4"));
        BusSeatList.add(new BusSeatListModel("B1","B2","X","B3","B4"));
        BusSeatList.add(new BusSeatListModel("C1","C2","X","C3","C4"));
        BusSeatList.add(new BusSeatListModel("D1","D2","X","D3","D4"));
        BusSeatList.add(new BusSeatListModel("E1","E2","X","E3","E4"));
        BusSeatList.add(new BusSeatListModel("F1","F2","X","F3","F4"));
        BusSeatList.add(new BusSeatListModel("G1","G2","X","G3","G4"));
        BusSeatList.add(new BusSeatListModel("H1","H2","X","H3","H4"));
        BusSeatList.add(new BusSeatListModel("I1","I2","X","I3","I4"));
        BusSeatList.add(new BusSeatListModel("J1","J2","X","J3","J4"));
        BusSeatList.add(new BusSeatListModel("K1","K2","X","K3","K4"));
        BusSeatList.add(new BusSeatListModel("L1","L2","LX","L3","L4"));

        */
        return BusSeatList;
    }
}
