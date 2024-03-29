package com.example.busapp.Booking.LongRouteBooking;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.PhoneNumberResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LongRouteSeatChoosingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.longroute_seat_choosing_view);
        SeatDropDown();
        Database db = new Database(LongRouteSeatChoosingActivity.this);

        TextView UsernameText = findViewById(R.id.name);
        String username = db.GetUsername(db);
        UsernameText.setText(username);

    }

    public void goBack(View view){
        this.finish();
    }

    public void SeatDropDown(){
        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.long_seat_numbers);
        // this will come from API/ TODO: Get data from api
        String[] items = new String[]{"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.color_spinner_layout, items);
        adapter.setDropDownViewResource(R.layout.spinner_list);

        dropdown.setAdapter(adapter);

        // see what Item has been selected
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                String number =dropdown.getSelectedItem().toString();

                Log.e("Selected item : ",number);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
    public void GoToCheckOut(View view){
        Intent intent = new Intent(this, BookingSeatActivity.class);
        startActivity(intent);
        this.finish();
    }
}
