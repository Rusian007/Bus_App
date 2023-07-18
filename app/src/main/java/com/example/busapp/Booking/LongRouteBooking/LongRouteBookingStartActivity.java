package com.example.busapp.Booking.LongRouteBooking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.Database.Database;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.LongRouteModel;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LongRouteBookingStartActivity extends AppCompatActivity {
    String FromLocation, ToLocation;
    ArrayMap<String, Integer> routeMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.long_route_booking_start_view);

        getLocationsFromApi();
        Database db = new Database(LongRouteBookingStartActivity.this);

        TextView UsernameText = findViewById(R.id.name);
        String username = db.GetUsername(db);
        UsernameText.setText(username);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void getLocationsFromApi() {
        Database db = new Database(LongRouteBookingStartActivity.this);
        String token = db.GetToken(db);
        ArrayList<String> items = new ArrayList<>();
        routeMap = new ArrayMap<>();

        if (isNetworkAvailable()) {
            ApiClientLongRoute client = new ApiClientLongRoute();
            Retrofit retrofit = client.getRetrofitInstance();
            LongRouteApi longRoute = retrofit.create(LongRouteApi.class);
            Call<LongRouteModel> call = longRoute.getLongRoutes("Token " + token);

            call.enqueue(new Callback<LongRouteModel>() {
                @Override
                public void onResponse(Call<LongRouteModel> call, Response<LongRouteModel> response) {
                    if (response.isSuccessful()) {
                        LongRouteModel model = response.body();

                        List<LongRouteModel.Locations> routes = model.getRoutes();

                        // do something with the routes
                        for (LongRouteModel.Locations route : routes) {
                            items.add(route.getName());
                            String name = route.getName();
                            int id = route.getId();
                           routeMap.put(name, id);

                        }
                        FromDropDown(items);
                        ToDropDown(items);

                    } else {
                        // handle error
                        Log.d("ERROR", "err: " + response.raw().body());
                        Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<LongRouteModel> call, Throwable t) {

                }
            });
        }else {
            // No internet connection available
            Toast.makeText(getApplicationContext(), "Internet is not available", Toast.LENGTH_SHORT).show();
        }
    }


    public void FromDropDown( ArrayList<String> items){
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.long_from_dst);
        //create a list of items for the spinner. this will come from API/ TODO: Get data from api


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_list);

        dropdown.setAdapter(adapter);

        // see what Item has been selected
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String location =dropdown.getSelectedItem().toString();
                FromLocation = location;
            //    Log.e("Selected item : ",location);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void ToDropDown(ArrayList<String> items){
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.long_to_dst);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_list);

        dropdown.setAdapter(adapter);



        // see what Item has been selected
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                String location =dropdown.getSelectedItem().toString();
                ToLocation = location;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void goToBusChoosing(View view){
        Intent intent = new Intent(this, SelectBusActivity.class);
        intent.putExtra("fromLocation", FromLocation);
        intent.putExtra("toLocation", ToLocation);
        Integer fromid = routeMap.get(FromLocation);
        Integer toid = routeMap.get(ToLocation);

            intent.putExtra("fromLocationID", fromid);

            intent.putExtra("toLocationID", toid);

        Database db = new Database(LongRouteBookingStartActivity.this);
        //db.addNewLongLocation(FromLocation, ToLocation);

     //   Cursor cr = db.getLocations(db);

       // cr.moveToFirst();
       // String number = cr.getString(0);
      boolean ISTableEmpty = db.IsTableEmpty(db);

       if(ISTableEmpty){
           // If table is empty then we insert new row
           db.addNewLongLocationID(fromid,FromLocation, toid, ToLocation);
       } else {
           // If table already exists then we just update the row
            db.UpdateLocations(db, fromid,FromLocation, toid, ToLocation);
       }

     startActivity(intent);

    }
    public void goBack(View view){
        this.finish();
    }
}
