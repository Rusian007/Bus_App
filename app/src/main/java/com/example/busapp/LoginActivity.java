package com.example.busapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.busapp.Booking.ShortRouteBooking.ShortRouteFromLoationActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.LoginApi;
import com.example.busapp.retrofit.ApiModels.TokenModel;
import com.example.busapp.retrofit.ErrorsModel.LoginError;
import com.example.busapp.retrofit.RequestModel.LoginCredentials;
import com.google.gson.Gson;

import java.io.IOException;

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


        usernameEdit = findViewById(R.id.username);
        passwordEdit = findViewById(R.id.password);
        db = new Database(LoginActivity.this);



        boolean ISTableEmpty = db.IsTableEmpty(db);
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
                callLoginApi(username, password);


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
                    CheckBox checkBox = findViewById(R.id.RememberCheck);
                    if (checkBox.isChecked()) {
                        // CheckBox is checked, do something
                        Toast.makeText(LoginActivity.this, "Token saved: "+token, Toast.LENGTH_SHORT).show();


                        boolean ISTableEmpty = db.IsTableEmpty(db);

                        if(ISTableEmpty){
                            // If table is empty then we insert new row
                            db.addNewToken(token);

                        } else {
                            // If table already exists then we just update the row
                            db.UpdateToken(db, token);
                        }
                        openNextActivity();

                    } else {
                        // CheckBox is not checked, do something else
                        openNextActivity();
                    }

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