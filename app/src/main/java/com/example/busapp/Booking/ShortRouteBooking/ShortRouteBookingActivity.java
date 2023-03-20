package com.example.busapp.Booking.ShortRouteBooking;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.Database.Database;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.ShortRouteApi;
import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.ApiModels.ShortRoutePointModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShortRouteBookingActivity extends AppCompatActivity implements ToLocationAdapter.IEndLocation {

    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();
    String FromLocationSelected;
    Button printbtn;
    boolean clicked= false ;
    private TextView fromLoc, toLoc, amount;
    Database db;

    ArrayList<String> endLocationSelected = new ArrayList<>();
    ArrayList<String> endLocationSelectedPrice = new ArrayList<>();

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

        setContentView(R.layout.short_route_to_location);

        Intent intent = getIntent();
        db= new Database(ShortRouteBookingActivity.this);

        FromLocationSelected = db.getShortLocationCache();


        fromLoc = findViewById(R.id.fromLocation);
        fromLoc.setText(FromLocationSelected);
        toLoc = findViewById(R.id.toLocation);
        amount = findViewById(R.id.shortRoute_amount);
        printbtn = (Button) findViewById(R.id.ShortprintBtn);


        // call API - start a method
        callApi();


        printbtn.setVisibility(View.INVISIBLE);


    }

    private void callApi() {


        String token = db.GetToken(db);



        // call API
        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        ShortRouteApi shortapi = retrofit.create(ShortRouteApi.class);

        Call<ShortRouteModel> call = shortapi.getShortRoutes("Token "+token);

        if (isNetworkAvailable()) {
            // Internet connection is available

            call.enqueue(new Callback<com.example.busapp.retrofit.ApiModels.ShortRouteModel>() {
                @Override
                public void onResponse(Call<ShortRouteModel> call, Response<ShortRouteModel> response) {
                    if (response.isSuccessful()) {
                        ShortRouteModel model = response.body();

                        List<ShortRouteModel.Route> routes = model.getRoutes();

                        // do something with the routes
                        for (ShortRouteModel.Route route : routes) {
                            int price = route.getFair();



                            // if that route does not exist the intert that in db
                            if( db.doesRouteExist(route.getStartingPointName())){
                                // exists
                            } else {
                                // does not exist
                                double distance;
                                if(route.getDistance() == null){
                                    distance = 0;
                                } else {
                                    distance = route.getDistance();
                                }

                                db.addNewRoutes(route.getId(), route.getStartingPointName(), route.getEndingPointName(), route.getFair(), distance, route.getStartingPoint(), route.getEndingPoint());
                            }
                            //
                        }
                        Cursor cv = db.returnEndingLocation(FromLocationSelected);

                        if (cv.moveToFirst()) {
                            do {
                                // Retrieve values from the current row of the Cursor object


                                getAndSetLocation(cv);
                            } while (cv.moveToNext());
                        }


                    } else {
                        // handle error
                        Log.d("ERROR", "err: "+ response.errorBody().toString());
                        Toast.makeText(getApplicationContext(), "Sorry something went wrong", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ShortRouteModel> call, Throwable t) {

                }
            });

        } else {
            // No internet connection available

            Cursor cv = db.returnEndingLocation(FromLocationSelected);

            if (cv.moveToFirst()) {
                do {
                    // Retrieve values from the current row of the Cursor object


                    getAndSetLocation(cv);
                } while (cv.moveToNext());
            }

        }

    }

    void getAndSetLocation(Cursor cv){
        String endingLocationName = cv.getString(cv.getColumnIndex("ending_point_name"));
        double fair = cv.getDouble(cv.getColumnIndex("fair"));
        toLocations.add(new ShortRoute_LocationModel(endingLocationName, String.valueOf(fair)));
        // Do something with the retrieved values

        Log.d("DATABASE", "Point: , " + fair + ", " + endingLocationName );
        initRecycleView();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initRecycleView() {

        // To List Adapter
        RecyclerView toListRecyclerView = findViewById(R.id.toLocation_recycleView);
        ToLocationAdapter toAdapter = new ToLocationAdapter(this, toLocations, this);
        GridLayoutManager toLayoutManager = new GridLayoutManager(this, 3);
        toListRecyclerView.setLayoutManager(toLayoutManager);
        toListRecyclerView.setAdapter(toAdapter);
    }


    // print button On click, print ticket here
    // make necessary changes
    public void Print_Short_Route(View view){
        if(endLocationSelected.size()>0){
            Toast.makeText(getApplicationContext(),"Your Ticket is being printed !", Toast.LENGTH_SHORT).show();
            // Print ticket here
            // Change later
            Intent intent = new Intent(this, ShortRouteBookingActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, 1000);



        }
        else Toast.makeText(getApplicationContext(),"You must select an end location", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean AddDestinationLocation(String locationName , String price) {
        if (endLocationSelected.isEmpty()){
            endLocationSelected.add(locationName);
            endLocationSelectedPrice.add(price);
            checkLocations();
            printbtn.setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void RemoveDestinationLocation(String locationName) {
        endLocationSelected.remove(locationName);
        checkLocations();
    }

    @Override
    public boolean isSelected() {
        if (endLocationSelected.isEmpty() && !clicked){
            clicked = true;
            return false;
        } else if(endLocationSelected.size() > 0 && clicked){
            clicked = false;
            return true;
        } else{
            return true;
        }

    }

// start printing button here
    private void checkLocations() {
        if (endLocationSelected.size() > 0 ){
            fromLoc.setText(FromLocationSelected);
            toLoc.setText(endLocationSelected.get(0));
            amount.setText(endLocationSelectedPrice.get(0));
        } else if(endLocationSelected.isEmpty()){
            toLoc.setText("...");
            amount.setText("...");
        }
    }
    public void finishActivity(View v){
        Intent intent = new Intent(getApplicationContext(), ShortRouteFromLoationActivity.class);
        startActivity(intent);
        this.finish();
    }


}
