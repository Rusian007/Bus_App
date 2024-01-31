package com.example.busapp.Booking.LongRouteBooking;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busapp.ChooseLongRouteActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.CreateTicketRequest;
import com.example.busapp.retrofit.ApiModels.DiscountLimitResponse;
import com.example.busapp.retrofit.ApiModels.GetBookedSeatsModel;
import com.example.busapp.retrofit.ApiModels.GetFairModel;
import com.example.busapp.retrofit.ApiModels.LongRouteSeatModel;
import com.example.busapp.retrofit.ApiModels.PhoneNumberResponse;
import com.example.busapp.retrofit.ApiModels.RouteRequestModel;
import com.example.busapp.retrofit.ApiModels.TicketResponse;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import org.json.JSONArray;
import org.json.JSONException;
public class BookingSeatActivity extends AppCompatActivity {
    EditText discountText;
    String PhoneNumber = null;
    String DiscountLimit = "200";
    TextView BusNameText, seatNamesText, totalSeatText, printAmount, lessAmount, netAmount;
    double amount= 0;
    int busCategory;
    Intent startPrint;
    ArrayList<String> selectedSeats;
    int fromLocationID, toLocationID, busID,ticketID;
    Button printTicketButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_booking_seat);
        discountText = findViewById(R.id.discountText);

        BusNameText = findViewById(R.id.BusName);
        seatNamesText = findViewById(R.id.Printing_seats);
        totalSeatText = findViewById(R.id.Total_Printing_seats);
        printAmount = findViewById(R.id.Printing_amount);
        printTicketButton = findViewById(R.id.Print_ticket_btn);
        lessAmount = findViewById(R.id.less_Printing);
        netAmount = findViewById(R.id.Net_Printing_amount);

        String BusName = (String) getIntent().getStringExtra("BUSNAME");
        BusNameText.setText(BusName);

        selectedSeats = getIntent().getStringArrayListExtra("SEATLIST");
        seatNamesText.setText("Seat: ");

        DiscountLimit = getIntent().getStringExtra("DISCOUNTLIMIT");

        busID = getIntent().getIntExtra("BUSID", 0);
        for (String seat: selectedSeats){
            seatNamesText.append(seat+", ");
        }
        Database db = new Database(BookingSeatActivity.this);

       TextView UsernameText = findViewById(R.id.username);
        String username = db.GetUsername(db);
        UsernameText.setText(username);

        totalSeatText.setText("Total Seats: "+ String.valueOf( selectedSeats.size()));

      //  GetDiscountLimitApi();
        callFairApi();

        discountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Get the entered value as a string
                String input = s.toString();

                if (!input.isEmpty()) {
                    // Parse the entered value as an integer
                    int value = Integer.parseInt(input);

                    // Check if the entered value is greater than 100
                    if (value > Integer.parseInt(DiscountLimit)) {
                        // Set the value to 100
                        discountText.setText(DiscountLimit);

                       // discountText.setSelection(discountText.getText().length()); // Move cursor to the end
                       value = Integer.parseInt(DiscountLimit);
                    }
                    lessAmount.setText("Less: "+ String.valueOf(value));
                    double net = (double) amount-value;
                    netAmount.setText("Net Amount: "+ String.valueOf(net));
                } else{
                  //  netAmount.setText("Net Amount: "+ String.valueOf(amount));
                  //  lessAmount.setText("0");
                    discountText.setText("0");

                }
            }
        });

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


        // send the make ticket request
        printTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPhoneApi();

            }
        });

    }



    public void GetPhoneApi(){
        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);

        Call<PhoneNumberResponse> call = longRoute.getPhone();

        call.enqueue(new Callback<PhoneNumberResponse>() {
            @Override
            public void onResponse(Call<PhoneNumberResponse> call, Response<PhoneNumberResponse> response) {
                if (response.isSuccessful()) {
                    PhoneNumberResponse phone_number_res = response.body();
                    PhoneNumber = phone_number_res.getPhone_number();
                    SendMakeTicket();
                }else{
                    Log.d("ERROR", "err: " + response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Sorry something went wrong getting phone number", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PhoneNumberResponse> call, Throwable t) {

            }
        });
    }

    private void SendMakeTicket() {
        Database db = new Database(BookingSeatActivity.this);
        String token = db.GetToken(db);
        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);
        Cursor cursor = db.getLocationID();
        while (cursor.moveToNext()) {
            // Get the values from the cursor
            fromLocationID = cursor.getInt(0);
            toLocationID = cursor.getInt(1);

        }
        cursor.close();
        String discount = discountText.getText().toString();
        JSONArray jsonArray = new JSONArray(selectedSeats);
        String jsonFormattedString = jsonArray.toString();
if(discount.isEmpty()){
    discount = "0";
}
        CreateTicketRequest request = new CreateTicketRequest(fromLocationID, toLocationID,busCategory, busID, jsonFormattedString,Integer.parseInt( discount));



        Call<TicketResponse> call = longRoute.makeTicket("Token " + token, request );
        call.enqueue(new Callback<TicketResponse>() {
            @Override
            public void onResponse(Call<TicketResponse> call, Response<TicketResponse> response) {
                if (response.isSuccessful()){
                    try {
                        TicketResponse ticket = response.body();
                        Toast.makeText(getApplicationContext(), "Printing Ticket with ticket ID: "+ticket.getId(), Toast.LENGTH_SHORT).show();
                        startPrint = new Intent(getApplicationContext(), PrintTicketActivity.class);
                        ticketID = ticket.getId();

                        startPrint.putStringArrayListExtra("SEATLIST", selectedSeats);
                    } catch (Exception e){
                        Log.d("ERROR", "err: " + response.errorBody().toString());
                    }

                }else {
                    Log.d("ERROR", "err: " + response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TicketResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please restart the app because of network problem", Toast.LENGTH_SHORT).show();
            }
        });
        printTicketButton.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "Ticket ID");
                Log.d("TAG", String.valueOf(ticketID));
                startPrint.putExtra("TID", String.valueOf(ticketID));
                startPrint.putExtra("phoneNumber", PhoneNumber);
                startActivity(startPrint);
                finish();
            }
        }, 1555); // Delay in milliseconds

    }

    private void callFairApi() {
        Database db = new Database(BookingSeatActivity.this);
        String token = db.GetToken(db);
        Cursor cursor = db.getLocationID();

        // Iterate through the results
        while (cursor.moveToNext()) {
            // Get the values from the cursor
            fromLocationID = cursor.getInt(0);
            toLocationID = cursor.getInt(1);
        }

        // Close the cursor
        cursor.close();

        // Call API

        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);

        String seatsString = TextUtils.join(", ", selectedSeats);

        Call<RouteRequestModel> call = longRoute.GetRouteID("Token " + token, fromLocationID, toLocationID );

        call.enqueue(new Callback<RouteRequestModel>() {
            @Override
            public void onResponse(Call<RouteRequestModel> call, Response<RouteRequestModel> response) {
                if (response.isSuccessful()) {
                    RouteRequestModel routeRequestModel = response.body();
                    // Handle the response data
                    try {
                        if (routeRequestModel != null) {
                            RouteRequestModel.Route route = routeRequestModel.getRoute();
                            int id = route.getId();
                            String routeName = route.getRouteName();
                            String distance = route.getDistance();
                            int fair = route.getFair();
                            int offset = route.getOffset();
                            int startingLocation = route.getStartingLocation();
                            int endingLocation = route.getEndingLocation();
                            busCategory = route.getBusCategory();

                            printAmount.setText("Amount: "+ String.valueOf( fair * selectedSeats.size()));
                            amount = fair * selectedSeats.size();
                            // Do something with the values
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "This is a invalid route. Redirecting you back, please wait ...", Toast.LENGTH_SHORT).show();
                        // Delay the redirection and finish the current activity after 2 seconds
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Start the LongRouteBookingStartActivity
                                Intent intent = new Intent(BookingSeatActivity.this, LongRouteBookingStartActivity.class);
                                startActivity(intent);

                                // Finish the current activity to destroy it
                                finish();
                            }
                        }, 2000);
                    }

                } else {
                    // Handle API error
                    Toast.makeText(getApplicationContext(), "This is a invalid route. Redirecting you back, please wait ...", Toast.LENGTH_SHORT).show();
                    // Delay the redirection and finish the current activity after 2 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Start the LongRouteBookingStartActivity
                            Intent intent = new Intent(BookingSeatActivity.this, LongRouteBookingStartActivity.class);
                            startActivity(intent);

                            // Finish the current activity to destroy it
                            finish();
                        }
                    }, 2000); // 2000 milliseconds = 2 seconds
                }
            }

            @Override
            public void onFailure(Call<RouteRequestModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed due to a network error. Please restart and connect to a wifi.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    // go back to home page if booking is cancelled
    public void Cancel_booking_onClick(View view){
        Intent intent = new Intent(this, SelectBusActivity.class);
       // String discount = discountText.getText().toString();

        startActivity(intent);
        this.finish();
    }
    public void Print_ticket_OnClick(View view){
        Toast.makeText(getApplicationContext(),"Printing Ticket ....", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SelectBusActivity.class);
        startActivity(intent);
        this.finish();
    }

}