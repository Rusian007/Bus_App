package com.example.busapp.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        // Remove this in Production
        // This data will come from API
        String[] seatRow1 = {"A1", "A2","X","A3","A4"} ;
        String[] seatRow2 = {"B1","B2","X","B3","B4"} ;
        String[] seatRow3 = {"C1","C2","X","C3","C4"};
        String[] seatRow4 = {"D1","D2","X","D3","D4"};
        String[] seatRow5 = {"E1","E2","X","E3","E4"};
        String[] seatRow6 = {"F1","F2","X","F3","F4"};
        String[] seatRow7 = {"G1","G2","X","G3","G4"};
        String[] seatRow8 = {"H1","H2","X","H3","H4"};
        String[] seatRow9 = {"I1","I2","X","I3","I4"};
        String[] seatRow10 = {"J1","J2","X","J3","J4"};
        String[] seatRow11= {"K1","K2","X","K3","K4"};
        String[] seatRow12 = {"L1","L2","LX","L3","L4"};

        // Delete these in production
        ArrayList<String[]> AllSeats= new ArrayList<>();
        AllSeats.add(seatRow1);
        AllSeats.add(seatRow2);
        AllSeats.add(seatRow3);
        AllSeats.add(seatRow4);
        AllSeats.add(seatRow5);
        AllSeats.add(seatRow6);
        AllSeats.add(seatRow7);
        AllSeats.add(seatRow8);
        AllSeats.add(seatRow9);
        AllSeats.add(seatRow10);
        AllSeats.add(seatRow11);
        AllSeats.add(seatRow12);


        // This is for Viewing in RecycleView
        for (String[] seat:
                AllSeats) {
            BusSeatListModel busSeatRow = new BusSeatListModel();
            busSeatRow.setSeatCol1(seat[0], true);
            busSeatRow.setSeatCol2(seat[1], true);
            busSeatRow.setSeatCol3(seat[2], true);
            busSeatRow.setSeatCol4(seat[3], true);
            busSeatRow.setSeatCol5(seat[4], true);

            BusSeatList.add(busSeatRow);
        }


        return BusSeatList;
    }
}
