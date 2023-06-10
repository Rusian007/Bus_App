package com.example.busapp.Booking.LongRouteBooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.BusLayoutAdapter;
import com.example.busapp.Database.Database;
import com.example.busapp.Model.BusModel;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.LongRouteBusModel;
import com.example.busapp.retrofit.ApiModels.LongRouteModel;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectBusActivity extends AppCompatActivity
{
    private ArrayList<BusModel> theBusNames = new ArrayList<>();
    TextView locationtxt;
    EditText searchBus;
    BusLayoutAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.select_bus_view);
        locationtxt = findViewById(R.id.locationtxt);
        Intent intent = getIntent();
        String fromlocation = intent.getStringExtra("fromLocation");
        String tolocation = intent.getStringExtra("toLocation");
        locationtxt.setText("From: "+fromlocation+" To: "+tolocation);
        InitializeNamesArray();

        Database db = new Database(SelectBusActivity.this);
        TextView UsernameText = findViewById(R.id.name);
        String username = db.GetUsername(db);
        UsernameText.setText(username);
        searchBus = findViewById(R.id.searchBus);
        searchBus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });
    }

    private void filter(String busname){
        ArrayList<BusModel> filteredBus = new ArrayList<>();
        for(BusModel bus : theBusNames){
            if(bus.getBusname().toLowerCase().contains(busname.toLowerCase())){
                filteredBus.add(bus);
            }
        }
        adapter.filterList(filteredBus);
    }
    private void InitializeNamesArray(){
        BusModel busname;
        Database db = new Database(SelectBusActivity.this);
        String token = db.GetToken(db);
        // Call API
        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);
        Call<LongRouteBusModel> call = longRoute.getLongRouteBuses("Token " + token);

        call.enqueue(new Callback<LongRouteBusModel>() {
            @Override
            public void onResponse(Call<LongRouteBusModel> call, Response<LongRouteBusModel> response) {
                if (response.isSuccessful()) {
                    LongRouteBusModel model = response.body();
                    List<LongRouteBusModel.Bus> buses = model.getListOfBuses();
                    for (LongRouteBusModel.Bus bus : buses) {

                        theBusNames.add(new BusModel(bus.getBusNumber(), bus.getId()));


                    }
                    //Start the View
                    initRecycleView();
                } else {
                    // handle error
                    Log.d("ERROR", "err: " + response.raw().body());
                    Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LongRouteBusModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Check if Internet is Available", Toast.LENGTH_SHORT).show();

            }
        });



    }
    private void initRecycleView(){

        RecyclerView recyclerView = findViewById(R.id.BusrecycleView);
        adapter = new BusLayoutAdapter(this, theBusNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void finishActivity(View v){
        this.finish();
    }
}
