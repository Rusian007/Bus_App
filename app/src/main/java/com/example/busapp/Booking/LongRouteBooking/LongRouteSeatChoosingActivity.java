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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.R;

public class LongRouteSeatChoosingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.longroute_seat_choosing_view);
        SeatDropDown();
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
