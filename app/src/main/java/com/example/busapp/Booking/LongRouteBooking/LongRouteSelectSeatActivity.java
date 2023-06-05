package com.example.busapp.Booking.LongRouteBooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusSeatAdapter;
import com.example.busapp.Database.Database;
import com.example.busapp.Model.BusSeatListModel;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.GetBookedSeatsModel;
import com.example.busapp.retrofit.ApiModels.LongRouteSeatModel;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LongRouteSelectSeatActivity extends AppCompatActivity implements  BusSeatAdapter.IBusSeat{
    RecyclerView busSeatRecycleView;
    BusSeatAdapter adapter;
    ImageButton back;
    TextView locationText;
    String fromLoc, toLoc, BusName;
    int busid;

    ArrayList<String> SelectedBusSeatsList;
    Button seatConfirmedButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.bus_seat_view);

        // DB stuff
        Database db = new Database(LongRouteSelectSeatActivity.this);
        Cursor cursor = db.getLocations(db);

        // Iterate through the results
        while (cursor.moveToNext()) {
            // Get the values from the cursor
            fromLoc = cursor.getString(0);
            toLoc = cursor.getString(1);

        }

        // Close the cursor
        cursor.close();

        BusName = (String) getIntent().getStringExtra("BUSNAME");
        busid =Integer.parseInt( getIntent().getStringExtra("BUSID"));

        TextView busNameText = findViewById(R.id.busName);
        locationText = findViewById(R.id.locationText);
        locationText.setText(fromLoc + " to " + toLoc);

        SelectedBusSeatsList = new ArrayList<>();
        seatConfirmedButton = (Button) findViewById(R.id.SeatConfirmButton); // Confirm button after selecting seats

        busNameText.setText(BusName);
        back = (ImageButton) findViewById(R.id.backbtn);


        busSeatRecycleView = findViewById(R.id.busSeatRecycleView);


        // Set recycle view
        setRecycleView();
    }

    public void ConfirmSeatButton_OnClickListener(View view){
        Intent intent = new Intent(this, BookingSeatActivity.class);
        if (SelectedBusSeatsList.isEmpty()){
            Toast.makeText(this.getApplicationContext(), "Select a seat please.", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putStringArrayListExtra("SEATLIST", SelectedBusSeatsList);
        intent.putExtra("BUSNAME", BusName);
        startActivity(intent);
        this.finish();
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
        adapter = new BusSeatAdapter(this, getSeatList() , this);
        busSeatRecycleView.setAdapter(adapter);
    }

    private List<BusSeatListModel> getSeatList(){
        List<BusSeatListModel> BusSeatList = new ArrayList<>();
        List<String> bookedSeatsList = new ArrayList<>();


        //Dummy Data - for test
        // Remove this in Production
        // This data will come from API

        Database db = new Database(LongRouteSelectSeatActivity.this);
        String token = db.GetToken(db);
        // Call API
       // LongRouteSeatStructureRequestModel busIdRequest = new LongRouteSeatStructureRequestModel(1);


        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);
        Call<LongRouteSeatModel> call = longRoute.getLongRouteSeatStructure("Token " + token, busid);

        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");

        // Get the current date
        Date currentDate = new Date();

        // Format the current date using the SimpleDateFormat
        String formattedDate = dateFormat.format(currentDate);

        //Get booked seats
        LongRouteApi longRoute2 = retrofit.create(LongRouteApi.class);
        Call<GetBookedSeatsModel> call_seats = longRoute2.getBookedSeats("Token "+token, busid, formattedDate);
        call_seats.enqueue(new Callback<GetBookedSeatsModel>() {
            @Override
            public void onResponse(Call<GetBookedSeatsModel> call, Response<GetBookedSeatsModel> response) {
                if(response.isSuccessful()){
                    GetBookedSeatsModel bookedSeats = response.body();
                    for(GetBookedSeatsModel.BookedSeat seat : bookedSeats.getBookedSeats()){
                     //   Log.d("BOOK", seat.getSeatNo());
                        bookedSeatsList.add(seat.getSeatNo());

                    }

                }else {
                    Log.d("ERROR", "err: " + response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetBookedSeatsModel> call, Throwable t) {

            }
        });
        //get bus seats
        call.enqueue(new Callback<LongRouteSeatModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<LongRouteSeatModel> call, Response<LongRouteSeatModel> response) {
                if (response.isSuccessful()) {

                    LongRouteSeatModel seats = response.body();

                    // get the booked seats

                  //  Log.d("TAG", bookedSeatsList.toString());
                    // make the seats that are booked = false
                    for(List<String> seat : seats.getSeatStructure()){
                        boolean s1=true,s2=true,s3=true,s4=true,s5=true;
                        if(bookedSeatsList.contains(seat.get(0))) s1=false;
                        if(bookedSeatsList.contains(seat.get(1))) s2=false;
                        if(bookedSeatsList.contains(seat.get(2))) s3=false;
                        if(bookedSeatsList.contains(seat.get(3))) s4=false;
                        if(bookedSeatsList.contains(seat.get(4))) s5=false;
                        // Log.d("Seat", seat.toString());
                        BusSeatListModel busSeatRow = new BusSeatListModel(
                                new BusSeatListModel.Seat(seat.get(0), s1),
                                new BusSeatListModel.Seat(seat.get(1), s2),
                                new BusSeatListModel.Seat(seat.get(2), s3),
                                new BusSeatListModel.Seat(seat.get(3), s4),
                                new BusSeatListModel.Seat(seat.get(4), s5));

                        BusSeatList.add(busSeatRow);

                    }

                    // Log.d("Seats", showAllSeats.toString());

                    adapter.notifyDataSetChanged();

                } else {
                    // handle error
                    Log.d("ERROR", "err: " + response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LongRouteSeatModel> call, Throwable t) {

            }
        });
/*


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

*/

        // This is for Viewing in RecycleView
//        for (String[] seat:
//                AllSeats) {
//            BusSeatListModel busSeatRow = new BusSeatListModel();
//            busSeatRow.setSeatCol1(seat[0], true);
//            busSeatRow.setSeatCol2(seat[1], true);
//            busSeatRow.setSeatCol3(seat[2], true);
//            busSeatRow.setSeatCol4(seat[3], true);
//            busSeatRow.setSeatCol5(seat[4], true);
//
//            BusSeatList.add(busSeatRow);
//        }
//        BusSeatListModel busSeatRow = new BusSeatListModel(
//                new BusSeatListModel.Seat("A1", true),
//                new BusSeatListModel.Seat("A2", true),
//                new BusSeatListModel.Seat("X", true),
//                new BusSeatListModel.Seat("A4", true),
//                new BusSeatListModel.Seat("A5", true));
//
//        BusSeatList.add(busSeatRow);

        return BusSeatList;
    }


    @Override
    public boolean AddSeat(String seatname) {
       // Log.d("SEATNAME", "AddSeat: "+ seatname);


        boolean contains = false;
        if(SelectedBusSeatsList.size() > 0){
            contains = SelectedBusSeatsList.contains(seatname);
        }
        boolean RestrictSeats = false;

       if(contains){
            SelectedBusSeatsList.remove(seatname);
         //  Log.d("seats", "Seat Removed: "+ "Removed******");
            //print Out selected Seats

        }else {
            SelectedBusSeatsList.add(seatname);
            //print Out selected Seats

        }

        return RestrictSeats;

    }

    public void finishActivity(View v){
        this.finish();
    }

}
