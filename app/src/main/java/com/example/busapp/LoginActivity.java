package com.example.busapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.LoginApi;
import com.example.busapp.retrofit.ApiModels.TokenModel;
import com.example.busapp.retrofit.ErrorsModel.LoginError;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;
import com.example.busapp.retrofit.RequestModel.LoginCredentials;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    Button button;
    Database db;
    String route;
    LoginApi login;
    String token;
    boolean isTokenSaved = false;


    EditText usernameEdit, passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.login_activity_view);
        Bundle bundle = getIntent().getExtras();
       route = bundle.getString("ROUTE"); // the selected route, long or short
        Log.d("ROUTE", ": "+route);

        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);
        db = new Database(LoginActivity.this);

        boolean ISTableEmpty = db.IsTokenTableEmpty(db);
        if (!ISTableEmpty){
            openNextActivity();
        }


        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdit.getText().toString();
                String password = passwordEdit.getText().toString();

                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (route.equals("LONG")){
                    callLoginApiLongRoute(username, password);
                } else {
                    callLoginApi(username, password);
                }

            }
        });

    }
    public void callLoginApiLongRoute(String username, String password){
        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LoginApi login = retrofit.create(LoginApi.class);
        LoginCredentials credentials = new LoginCredentials(username, password);
        Call<TokenModel> call = login.loginUser(credentials);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {

                    TokenModel loginResponse = response.body();
                    token = loginResponse.getToken();
                    // save the token or proceed to the next screen
                    //  CheckBox checkBox = findViewById(R.id.RememberCheck);
                    boolean ISTableEmpty = db.IsTokenTableEmpty(db);
//                    if (checkBox.isChecked()) {
//                        // CheckBox is checked, do something
//                        Toast.makeText(LoginActivity.this, "Token saved: "+token, Toast.LENGTH_SHORT).show();
//
//
//
//                        if(ISTableEmpty){
//                            // If table is empty then we insert new row
//                            db.addNewToken(token, usernameEdit.getText().toString());
//
//                        } else {
//                            // If table already exists then we just update the row
//                            db.UpdateToken(db, token, usernameEdit.getText().toString());
//                        }
//                        openNextActivity();




                    // for now we still save the token in database - this maybe change later
                    if(ISTableEmpty){
                        db.addNewToken(token, usernameEdit.getText().toString(), "LONG");
                    } else {
                        db.UpdateToken(db, token, usernameEdit.getText().toString(), "LONG");
                    }
                    openNextActivity();


                } else {
                    // handle the error
                    try {
                        Gson gson = new Gson();
                        LoginError errorResponse = gson.fromJson(response.errorBody().string(), LoginError.class);
                        String errorMessage = errorResponse.getNonFieldErrors().get(0); // assuming the first error message is the relevant one
                     Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("ERROR", ": "+t);
                Toast.makeText(LoginActivity.this, "Request Send Failed, Please Check Your Internet Connection or Maybe The Server Is Down.", Toast.LENGTH_SHORT).show();
            }
        });

    }


  public void callLoginApi(String username, String password){

        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        LoginApi login = retrofit.create(LoginApi.class);
        LoginCredentials credentials = new LoginCredentials(username, password);
        Call<TokenModel> call = login.loginUser(credentials);
        call.enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                if (response.isSuccessful()) {

                    TokenModel loginResponse = response.body();
                    token = loginResponse.getToken();
                    // save the token or proceed to the next screen
                  //  CheckBox checkBox = findViewById(R.id.RememberCheck);
                    boolean ISTableEmpty = db.IsTokenTableEmpty(db);
//                    if (checkBox.isChecked()) {
//                        // CheckBox is checked, do something
//                        Toast.makeText(LoginActivity.this, "Token saved: "+token, Toast.LENGTH_SHORT).show();
//
//
//
//                        if(ISTableEmpty){
//                            // If table is empty then we insert new row
//                            db.addNewToken(token, usernameEdit.getText().toString());
//
//                        } else {
//                            // If table already exists then we just update the row
//                            db.UpdateToken(db, token, usernameEdit.getText().toString());
//                        }
//                        openNextActivity();

                        // for now we still save the token in database - this maybe change later
                        if(ISTableEmpty){
                            db.addNewToken(token, usernameEdit.getText().toString(), "SHORT");
                        } else {
                            db.UpdateToken(db, token, usernameEdit.getText().toString(), "SHORT");
                        }
                        openNextActivity();

                } else {
                    // handle the error
                    try {
                        Gson gson = new Gson();
                        LoginError errorResponse = gson.fromJson(response.errorBody().string(), LoginError.class);
                        String errorMessage = errorResponse.getNonFieldErrors().get(0); // assuming the first error message is the relevant one
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.d("ERROR", ": "+t);
                Toast.makeText(LoginActivity.this, "Request Send Failed, Please Check Your Internet Connection or Maybe The Server Is Down.", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void openNextActivity() {
        if(route.equals("LONG")){
            Intent intent = new Intent(this, ChooseLongRouteActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
        }
        else if (route.equals("SHORT")){
            Intent intent = new Intent(this, ShortRouteFromLoationActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            this.finish();
        }
    }

}