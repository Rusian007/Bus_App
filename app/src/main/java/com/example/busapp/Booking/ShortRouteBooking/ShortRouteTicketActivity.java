package com.example.busapp.Booking.ShortRouteBooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busapp.Database.Database;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.ShortRouteApi;
import com.example.busapp.retrofit.ApiModels.ShortRouteTicketModel;
import com.example.busapp.retrofit.ApiModels.TicketRequestBodyMultiple;

import java.nio.Buffer;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShortRouteTicketActivity extends AppCompatActivity {
    Database db;
    Handler handler;
    ArrayList<TicketRequestBodyMultiple.TicketModel> ticketList ;
    TextView loadingText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //code to remove top and bottom bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_short_route_ticket_uploading);

        db = new Database(this);
        loadingText = findViewById(R.id.loading);
        ticketList = new ArrayList<>();
        GetAllData();
    }

    private void GetAllData() {
        if(! db.doesTicketSoldTableExist()){
             handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingText.setText("All Data Already Uploaded");
            }
        },2000);


        }else {
            Cursor cv = db.getSoldTickets();
            cv.moveToFirst();

            do {
                // Retrieve the values from the cursor for each column
                String routeId = String.valueOf(cv.getInt(cv.getColumnIndex("route_id")));
                String seats = String.valueOf(cv.getInt(cv.getColumnIndex("seats")));

                // Create a new TicketModel object with the retrieved values
                TicketRequestBodyMultiple.TicketModel ticket = new TicketRequestBodyMultiple.TicketModel(routeId, seats);
                // Add the ticket to the list
                ticketList.add(ticket);
                // Move to the next row

            }while (cv.moveToNext());
            TicketRequestBodyMultiple multipleTickets = new TicketRequestBodyMultiple(ticketList);

           /* for (TicketRequestBodyMultiple.TicketModel ticket : ticketList) {
                // Retrieve the values from the TicketModel object
                String routeId = ticket.getRouteId();
                String seats = ticket.getSeats();

                // Print out the values
                System.out.println("Route ID: " + routeId + ", Seats: " + seats);
            }
           */
            if(isNetworkAvailable()){
                callTicketUploadApi(multipleTickets);
            }
            else loadingText.setText("You need to have a internet connection to upload data.");

            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), ShortRouteFromLoationActivity.class);
                    startActivity(intent);
                    finish();

                }
            },3500);

        }

    }

    private void callTicketUploadApi(TicketRequestBodyMultiple multipleTickets) {
        String token = db.GetToken(db);
        // call API
        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        ShortRouteApi shortapi = retrofit.create(ShortRouteApi.class);

        Call<String> call = shortapi.postMultipleTickets("Token " + token, multipleTickets);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("Debug", response.toString());
                        db.deleteAllRecordsFromTable();
                        loadingText.setText("Data upload complete. Please close this screen.");
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occured on sending request" , Toast.LENGTH_LONG).show();
                    Log.d("****", "Error Occured on sending request");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error " +t.getMessage() , Toast.LENGTH_LONG).show();
                Log.d("Debug", t.getMessage());

                if (call.request().body() != null) {
                    try {
                        // Convert the request body to a string and log it
                        String requestBody = call.request().body().toString();
                        Log.d("Debug", "Request Body: " + requestBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}