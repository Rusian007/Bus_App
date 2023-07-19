package com.example.busapp.Booking.LongRouteBooking;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;
import com.example.busapp.Database.Database;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiEndpoints.LongRouteApi;
import com.example.busapp.retrofit.ApiModels.GetBookedSeatsModel;
import com.example.busapp.retrofit.ApiModels.GetTicketBody;
import com.example.busapp.retrofit.ApiModels.TicketResponse;
import com.example.busapp.retrofit.RequestModel.ApiClientLongRoute;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PrintTicketActivity extends AppCompatActivity {
    int tid;
    Database db;
    BluetoothSocket socket = null;
    BluetoothAdapter bluetoothAdapter;
    boolean donePrint = false;
    ArrayList<String> selectedSeats;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.longroute_printticket);
        ImageView imageView = findViewById(R.id.imggif);

         //Integer.parseInt( getIntent().getStringExtra("TID"));
        String ticketIdString = getIntent().getStringExtra("TID");
       // Log.d("ID", "onCreate: "+ticketIdString);
        tid = 0; // Default value in case the extra is null
        tid = Integer.parseInt(ticketIdString);


        selectedSeats = getIntent().getStringArrayListExtra("SEATLIST");

        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(imageView);

        GetTicketInfo();
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

    }

    private void GetTicketInfo() {
        db = new Database(PrintTicketActivity.this);
        String token = db.GetToken(db);

        ApiClientLongRoute client = new ApiClientLongRoute();
        Retrofit retrofit = client.getRetrofitInstance();
        LongRouteApi longRoute = retrofit.create(LongRouteApi.class);

        Call<GetTicketBody> call = longRoute.getTicket(tid, "Token " + token);
        call.enqueue(new Callback<GetTicketBody>() {
            @Override
            public void onResponse(Call<GetTicketBody> call, Response<GetTicketBody> response) {
                if(response.isSuccessful()){
                    GetTicketBody ticket = response.body();
                  // Print the ticket in POS

                    GetTicketBody.Ticket TheTicket = ticket.getTicket();
                    String originalDate = TheTicket.getDate();
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formattedDate  =TheTicket.getDate();
                    try {
                        Date date = inputFormat.parse(originalDate);
                         formattedDate = outputFormat.format(date);

                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Please restart the app because of error ", Toast.LENGTH_SHORT).show();

                    }
                    Bluetoothprint(TheTicket.getStartingLocationName(), TheTicket.getEndingLocationName(), formattedDate, TheTicket.getTime(), TheTicket.getBusNumber(), TheTicket.getCategory(),TheTicket.getFair(), TheTicket.getDiscount(), TheTicket.getCountermanUsername(), selectedSeats);
                }else{
                    Log.d("ERROR", "err: " + response.errorBody().toString());
                    Toast.makeText(getApplicationContext(), "Please restart the app because of the following error:  " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<GetTicketBody> call, Throwable t) {

            }
        });
    }
    void Bluetoothprint( String startingLocationName, String endingLocationName,String date, String time, String busNumber,String category,int fair,int discount,String countermanUsername,  ArrayList<String> selectedSeats) {

        boolean macExists = db.doesMacExist();
        if (!macExists){
            Toast.makeText(getApplicationContext(), "No Bluetooth device connected, Please restart the app", Toast.LENGTH_LONG).show();
            return;
        }


        try {
            String MacAddress = db.getMacAddress();
            Log.d("#########", "Bluetooth enabled " + MacAddress);

            BluetoothDevice printer = bluetoothAdapter.getRemoteDevice(MacAddress.replaceAll("\\s", "")); // replace with your printer's MAC address
            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 9);
            }

            socket = printer.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            Log.d("******", "Connecting ################");
            OutputStream outputStream = socket.getOutputStream();


            boolean isConnected = socket.isConnected();
            if (isConnected) {
                // The device is connected
                Log.d("******", "Socket is connected: ");
            } else {
                // The device is not connected
                Log.d("******", "Socket is NOT connected: ");
            }
            // Define the text to be printed
            String bigText = "MAWA PARIBAHAN PVT LTD (ELISH)";
            // String bigText = "\u09AE\u09BE\u0993\u09DF\u09BE \u09AA\u09B0\u09BF\u09AC\u09B9\u09A8 (\u09AA\u09CD\u09B0\u09BE\u09B9)\u09B2\u09BF\u0993 (\u0987\u09B2\u09BF\u09B6 )"; // Bangla text in Unicode
            String dot_com = "www.elishparibahan.com";
            String passenger_cpy = "Passenger copy";
            String serial_no = "serial no: "+tid;
            String from_location = "From: "+startingLocationName;
            String to_location = "To: "+endingLocationName;
            String coach = "COACH: "+busNumber;
            String seatsString = TextUtils.join(", ", selectedSeats);
            String seats = "SEATS: "+seatsString;
            String formattedTime ="";
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss.SSSSSS");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");

                Date dat = inputFormat.parse(time);
                formattedTime = outputFormat.format(dat);

               // System.out.println(formattedTime);  // Output: 16:36
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Set the text alignment to center
            byte[] alignCenter = {0x1B, 0x61, 0x01};
            byte[] alignStart = {0x1B, 0x61, 0x00};

            // Set the text size to big
            //byte[] textSizeBig = {0x1D, 0x21, 0x11};
            byte[] textSizeBig = new byte[]{0x1D, 0x21, 0x12}; // 0x30 is the command for double height and double width text


            // Set the text size to small
            byte[] textSizeSmall = {0x1B, 0x21, 0x00};

            // Set the text to bold
            byte[] boldOn = {0x1B, 0x45, 0x01};

            // Set the text to regular
            byte[] boldOff = {0x1B, 0x45, 0x00};

            byte[] initializePrinter = {27, 64}; // initialize the printer
            byte[] printText = {27, 74, 2, 27, 100, 5}; // print the text and feed 4 lines
            byte[] eject = {27, 101}; // eject the paper

            Log.d("*******", "printing");
            outputStream.write(initializePrinter);

            // Print big text in the center
            outputStream.write(alignCenter);
            outputStream.write(textSizeBig);
            outputStream.write(bigText.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // passenger copy - text
            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            outputStream.write(passenger_cpy.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // Print small text in the bottom
            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            outputStream.write(dot_com.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // Print normal text 1
            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(serial_no.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // Print normal text 2 in bold
            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOn);
            outputStream.write(from_location.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("       ".getBytes("UTF-8"));


            // Print normal text 3 in bold and right-aligned
            byte[] alignRight = {0x1B, 0x61, 0x02};


            outputStream.write(textSizeSmall);
            outputStream.write(boldOn);
            outputStream.write(to_location.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("       ".getBytes("UTF-8"));

            // Category
            //outputStream.write(alignStart);
            outputStream.write(boldOff);
            outputStream.write(category.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // coach and seats
//            outputStream.write(alignStart);
//            outputStream.write(textSizeSmall);
//            outputStream.write(boldOff);
//            outputStream.write(coach.getBytes("UTF-8"));
//            outputStream.write("               ".getBytes("UTF-8"));

            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write(seats.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));




            // Amount text
            outputStream.write(alignStart);
            outputStream.write(boldOn);
            String line2 = "Amount: "+fair;
            outputStream.write(line2.getBytes());
            outputStream.write(boldOff);
            outputStream.write("\n".getBytes("UTF-8"));





            String line1 = "------------------------------------------------\n";
            outputStream.write(line1.getBytes());


            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write("issue date: ".getBytes("UTF-8"));
            outputStream.write(date.getBytes("UTF-8"));
            outputStream.write("                ".getBytes("UTF-8"));


           // outputStream.write("".getBytes("UTF-8"));
            outputStream.write(formattedTime.getBytes());
            outputStream.write("\n".getBytes("UTF-8"));

            String line3 = "------------------------------------------------\n";
            outputStream.write(line3.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOff);
            String line4 = "Complain: 01409978008 \n";
            outputStream.write(line4.getBytes());


            String line5 = "------------------------------------------------\n";
            outputStream.write(line5.getBytes());
            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            String line6 = "Developer: softwarepatron.com \n";
            outputStream.write(line6.getBytes());

            outputStream.write("\n\n".getBytes("UTF-8"));


            // guide copy
            // guide copy - text
            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            outputStream.write("Guide Copy".getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));



            // Serial No
            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(serial_no.getBytes("UTF-8"));
            outputStream.write("                            ".getBytes("UTF-8"));

            // Category

            outputStream.write(category.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // from to location
            outputStream.write(textSizeSmall);
            outputStream.write(from_location.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("    ".getBytes("UTF-8"));


            outputStream.write(textSizeSmall);
            outputStream.write(to_location.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("    ".getBytes("UTF-8"));

            // coach and category
            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write(coach.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));


            outputStream.write(boldOff);
            // Amount text
            outputStream.write(alignStart);
            outputStream.write(boldOn);
            String amt = "Amount: "+fair;
            outputStream.write(amt.getBytes());
            outputStream.write(boldOff);
            outputStream.write("     ".getBytes("UTF-8"));

            // seats
            outputStream.write(textSizeSmall);

            outputStream.write(boldOn);
            outputStream.write(seats.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("     ".getBytes("UTF-8"));

            //Due
            outputStream.write(boldOn);
            String due = "Due: "+discount;
            outputStream.write(boldOff);
            outputStream.write(due.getBytes());
            outputStream.write("\n".getBytes("UTF-8"));

            // date and time

            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write("issue date: ".getBytes("UTF-8"));
            outputStream.write(date.getBytes("UTF-8"));
            outputStream.write("                  ".getBytes("UTF-8"));


            outputStream.write("".getBytes("UTF-8"));
            outputStream.write(formattedTime.getBytes());


            outputStream.write(printText);
            outputStream.write(eject);
            Log.d("*******", "ejecting");
            outputStream.flush();
            donePrint = true;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Service Error, Please restart the app", Toast.LENGTH_LONG).show();

            Log.i("SOCKET ERROR", " : " + e.toString());
        }

        if (donePrint){

            // printing is Done
            Intent intent = new Intent(this, LongRouteBookingStartActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, 2500);
        }
    }

}
