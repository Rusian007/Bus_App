package com.example.busapp.Booking.LongRouteBooking;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BookingSeatActivity extends AppCompatActivity {
    EditText discountText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_booking_seat);
        discountText = findViewById(R.id.discountText);


        discountText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) view).setCursorVisible(true);
                } else {
                    ((EditText) view).setCursorVisible(false);
                }
            }
        });

        discountText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                discountText.setCursorVisible(true);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // User clicked the "Enter" key on the keyboard
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(discountText.getWindowToken(), 0);
                    discountText.setCursorVisible(false);
                    return true;
                }
                return false;
            }
        });


    }

    // go back to home page if booking is cancelled
    public void Cancel_booking_onClick(View view){

        this.finish();
    }
    public void Print_ticket_OnClick(View view){
        Toast.makeText(getApplicationContext(),"Printing Ticket ....", Toast.LENGTH_SHORT).show();
    }

}