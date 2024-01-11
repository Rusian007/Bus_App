package com.example.busapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.FromLocationAdapter;
import com.example.busapp.Adaptar.SalesViewAdapter;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.Model.SalesModel;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.SalesApi;
import com.example.busapp.retrofit.ApiEndpoints.ShortRouteApi;
import com.example.busapp.retrofit.ApiModels.SalesApiModel;
import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SalesActivity extends AppCompatActivity {
    TextView dateText;
    SalesViewAdapter adapter;
    Database db;
    String date = null;
    ImageButton Datebutton;
    ArrayList<SalesModel> SalesInfoList = new ArrayList<>() ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.sales_view);

        dateText = findViewById(R.id.DateText);
        Datebutton = (ImageButton) findViewById(R.id.imageButton);

        Datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate(v);
            }
        });
        db = new Database(this);

        SalesInfoList.add(new SalesModel("Select Date",true));

        initialize();


    }

    public void setDate(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthofyear, int dayofmonth) {
                monthofyear = monthofyear+1;
                date = year + "-" + monthofyear + "-" + dayofmonth;
                dateText.setText(date);

                callAPI();
            }
        }, year, month, day);
        dpd.show();
    }
    public void initialize(){
        // From List Adapter
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sales_recycleView);
         adapter = new SalesViewAdapter(this, SalesInfoList);
        // setting grid layout manager to implement grid view.
        GridLayoutManager layoutManager=new GridLayoutManager(this,5);
        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }

    public void callAPI(){
        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        SalesApi salesApi = retrofit.create(SalesApi.class);
        String token = db.GetToken(db);

        Call<SalesApiModel> call = salesApi.getSales("Token "+token, date);
        Log.d("TOKEN", token);
        // Execute the API call
        call.enqueue(new Callback<SalesApiModel>() {
            @Override
            public void onResponse(Call<SalesApiModel> call, Response<SalesApiModel> response) {

                if (response.isSuccessful()) {
                    adapter.clearData();
                    SalesApiModel model = response.body();
                    List<SalesApiModel.Ticket> sales = model.getTickets();
                    Toast.makeText(getApplicationContext(), "Loading data", Toast.LENGTH_SHORT).show();
                    if(sales.size() > 0){
                        SalesInfoList.add(new SalesModel("time", true));
                        SalesInfoList.add(new SalesModel("bus", true));
                      //  SalesInfoList.add(new SalesModel("seat",true));
                        SalesInfoList.add(new SalesModel("amount",true));
                        SalesInfoList.add(new SalesModel("discount",true));
                        SalesInfoList.add(new SalesModel("booked by",true));

                        for (SalesApiModel.Ticket sale : sales) {

                            String fair = String.valueOf(sale.getFair());
                            String discount = String.valueOf(sale.getDiscount());
                            String time = sale.getTime();
                            String hourAndMinute = time.substring(0, 5); // "16:42"

                            SalesInfoList.add(new SalesModel(hourAndMinute,false));
                            SalesInfoList.add(new SalesModel(sale.getBus_number(),false));
                          //  SalesInfoList.add(new SalesModel("1",false));
                            SalesInfoList.add(new SalesModel(fair,false));
                            SalesInfoList.add(new SalesModel(discount,false));
                            SalesInfoList.add(new SalesModel(sale.getBooked_by(),false));
                        }
                    }

                else {
                        SalesInfoList.add(new SalesModel("No Data", false));

                    }

                    initialize();
                } else {
                    Log.d("EROR",response.toString());
                    Toast.makeText(getApplicationContext(), "Sorry, Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SalesApiModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void finishActivity(View v){
       this.finish();
    }
}
