package com.example.busapp.Booking.ShortRouteBooking;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busapp.Adaptar.ToLocationAdapter;
import com.example.busapp.Database.Database;
import com.example.busapp.Model.ShortRoute_LocationModel;
import com.example.busapp.R;
import com.example.busapp.retrofit.ApiClient;
import com.example.busapp.retrofit.ApiEndpoints.ShortRouteApi;
import com.example.busapp.retrofit.ApiModels.PhoneNumberResponse;
import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.ApiModels.ShortRouteTicketModel;
import com.example.busapp.retrofit.ApiModels.TicketRequestBody;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.IOException;
import java.io.OutputStream;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShortRouteBookingActivity extends AppCompatActivity implements ToLocationAdapter.IEndLocation {
    String PhoneNumber = null;
    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();
    View parentLayout ;
    BluetoothSocket socket = null;
    String FromLocationSelected;
    boolean donePrint = false;
    Button printbtn;
    boolean clicked = false;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver broadcastReceiver;

    int REQUEST_ENABLE_BT = 0;
    int endLocationId;
    private TextView fromLoc, toLoc, amount, todayDate, TicketSeatCount, TicketAmountCount, UsernameText;
    Database db;

    ArrayList<String> endLocationSelected = new ArrayList<>();
    ArrayList<String> endLocationSelectedPrice = new ArrayList<>();

    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter<String> devicesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.short_route_to_location_view);

        Intent intent = getIntent();
        db = new Database(ShortRouteBookingActivity.this);
        AndroidThreeTen.init(this);

        toLocations.clear();
        toLocations.trimToSize();
        endLocationSelected.clear();
        endLocationSelected.trimToSize();
        endLocationSelectedPrice.clear();
        endLocationSelectedPrice.trimToSize();
        endLocationId = -1;
        FromLocationSelected = db.getShortLocationCache();


        fromLoc = findViewById(R.id.fromLocation);
        fromLoc.setText(FromLocationSelected);
        toLoc = findViewById(R.id.toLocation);
        amount = findViewById(R.id.shortRoute_amount);
        printbtn = (Button) findViewById(R.id.ShortprintBtn);
        todayDate = findViewById(R.id.date);
        TicketSeatCount = findViewById(R.id.ticketSeats);
        TicketAmountCount = findViewById(R.id.ticketAmount);
        UsernameText = findViewById(R.id.start);

        String username = db.GetUsername(db);
        UsernameText.setText(username);

        if(!db.doesPriceTableExist()){
            TicketSeatCount.setText("0");
            TicketAmountCount.setText("0");
            db.newPriceTable(0,0);
        } else {
            ArrayList<Integer> integerList = db.getPricesAndSeats();
            SetSeatandAmount(String.valueOf(integerList.get(0)),String.valueOf(integerList.get(1)) );
        }

        // call API - start a method
        callApi();
        GetPhoneApi();
      //  getTime();
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        printbtn.setVisibility(View.INVISIBLE);


    }

    public void GetPhoneApi(){
        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        ShortRouteApi shortApi = retrofit.create(ShortRouteApi.class);

        Call<PhoneNumberResponse> call = shortApi.getPhone();

        call.enqueue(new Callback<PhoneNumberResponse>() {
            @Override
            public void onResponse(Call<PhoneNumberResponse> call, Response<PhoneNumberResponse> response) {
                if (response.isSuccessful()) {
                    PhoneNumberResponse phone_number_res = response.body();
                    PhoneNumber = phone_number_res.getPhone_number();
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
    public void SetSeatandAmount(String seats, String amount){
        TicketSeatCount.setText(seats);
        TicketAmountCount.setText(amount);
    }

    private void callApi() {
        String token = db.GetToken(db);
        // call API
        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        ShortRouteApi shortapi = retrofit.create(ShortRouteApi.class);

        Call<ShortRouteModel> call = shortapi.getShortRoutes("Token " + token);

        if (isNetworkAvailable()) {
            // Internet connection is available
            call.enqueue(new Callback<com.example.busapp.retrofit.ApiModels.ShortRouteModel>() {
                @Override
                public void onResponse(Call<ShortRouteModel> call, Response<ShortRouteModel> response) {
                    if (response.isSuccessful()) {
                        ShortRouteModel model = response.body();
                        List<ShortRouteModel.Route> routes = model.getRoutes();
                        // do something with the routes
                        for (ShortRouteModel.Route route : routes) {
                            int price = route.getFair();
                            // if that route does not exist the insert that in db
                            if (db.doesRouteExist(route.getId())) {
                                // exists
                            } else {
                                // does not exist
                                double distance;
                                if (route.getDistance() == null) {
                                    distance = 0;
                                } else {
                                    distance = route.getDistance();
                                }

                                db.addNewRoutes(route.getId(), route.getStartingPointName(), route.getEndingPointName(), route.getFair(), distance, route.getStartingPoint(), route.getEndingPoint());
                            }
                        }
                        Cursor cv = db.returnEndingLocation(FromLocationSelected);
                        if (cv.moveToFirst()) {
                            do {
                                // Retrieve values from the current row of the Cursor object
                                getAndSetLocation(cv);
                            } while (cv.moveToNext());
                        }

                    } else {
                        // handle error
                        Log.d("ERROR", "err: " + response.errorBody().toString());
                        Toast.makeText(getApplicationContext(), "Sorry something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ShortRouteModel> call, Throwable t) {
                }
            });
        } else {
            // No internet connection available
            Cursor cv = db.returnEndingLocation(FromLocationSelected);
            if (cv.moveToFirst()) {
                do {
                    // Retrieve values from the current row of the Cursor object
                    getAndSetLocation(cv);
                } while (cv.moveToNext());
            }
        }
    }

    void CallApiSendData(){
        String token = db.GetToken(db);
        ArrayList<Integer> integerList = db.getPricesAndSeats();

        String stringValue = endLocationSelectedPrice.get(0);
        int intValue = Integer.parseInt(stringValue.split("\\.")[0]);
        int totalAmount = (int) integerList.get(1) + intValue;
        int totalSeat = integerList.get(0)+1;


        db.setPriceTable(totalSeat, totalAmount);

        SetSeatandAmount(String.valueOf(totalSeat),String.valueOf(totalAmount));
        // call API
        ApiClient client = new ApiClient();
        Retrofit retrofit = client.getRetrofitInstance();
        ShortRouteApi shortapi = retrofit.create(ShortRouteApi.class);

        Call<ShortRouteTicketModel> call = shortapi.setSoldTicket("Token " + token, new TicketRequestBody(String.valueOf(endLocationId), "1"));

        if(isNetworkAvailable()){
            call.enqueue(new Callback<ShortRouteTicketModel>() {
                @Override
                public void onResponse(Call<ShortRouteTicketModel> call, Response<ShortRouteTicketModel> response) {
                    if (response.isSuccessful()) {
                        ShortRouteTicketModel model = response.body();
                        Log.d("****", ": "+ model.getTicket().getBookedBy());
                    }else {
                        Toast.makeText(getApplicationContext(), "Error Occured on sending request" , Toast.LENGTH_SHORT).show();
                        Log.d("****", "Error Occured on sending request");
                    }
                }

                @Override
                public void onFailure(Call<ShortRouteTicketModel> call, Throwable t) {
                    Log.d("****", "Error Occured on sending request");
                    Toast.makeText(getApplicationContext(), "Error Occured on sending request" + t, Toast.LENGTH_SHORT).show();
                }
            });
        } else{

            db.newTicketSold(endLocationId,1);
            Toast.makeText(getApplicationContext(), "Network not available, Saving ticket on phone", Toast.LENGTH_SHORT).show();

        }
    }

    void getAndSetLocation(Cursor cv) {
        String endingLocationName = cv.getString(cv.getColumnIndex("ending_point_name"));
        double fair = cv.getDouble(cv.getColumnIndex("fair"));
        int id = cv.getInt(cv.getColumnIndex("ID"));
        toLocations.add(new ShortRoute_LocationModel(endingLocationName, String.valueOf(fair), id));

        initRecycleView();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void initRecycleView() {
        // To List Adapter
        RecyclerView toListRecyclerView = findViewById(R.id.toLocation_recycleView);
        ToLocationAdapter toAdapter = new ToLocationAdapter(this, toLocations, this);
        GridLayoutManager toLayoutManager = new GridLayoutManager(this, 3);
        toListRecyclerView.setLayoutManager(toLayoutManager);
        toListRecyclerView.setAdapter(toAdapter);
    }


    // print button On click
    public void Print_Short_Route(View view) {
        if (endLocationSelected.size() > 0) {

            // Print ticket here
            BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
                Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            // discoverDevices();
           //CallApiSendData();
            Bluetoothprint();


        } else
            Toast.makeText(getApplicationContext(), "You must select an end location", Toast.LENGTH_SHORT).show();

    }

    // Call this method from your activity to start the device discovery
    private void discoverDevices() {
        // Get the Bluetooth adapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Request permission for Bluetooth if not already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }


        // Get a list of paired devices
        pairedDevices = bluetoothAdapter.getBondedDevices();

        // Start discovery of nearby devices
        bluetoothAdapter.startDiscovery();


        // Create a list of device names to display to the user
        devicesList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (BluetoothDevice device : pairedDevices) {
            devicesList.add(device.getName() + "\n" + device.getAddress());
        }

        // Display a list dialog for the user to choose from
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select a device to connect to:");

        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                devicesList,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the selected device
                        String deviceInfo = devicesList.getItem(which);
                        String[] parts = deviceInfo.split("\n");
                        String deviceAddress = parts[1];

                        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                        Log.d("****", deviceAddress);
                        // Connect to the device
                        // ...
                    }
                });
        builderSingle.show();
    }


    // print the ticket
    void Bluetoothprint() {

        boolean macExists = db.doesMacExist();
        if (!macExists){
            parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "NO bluetooth device is saved, Please restart this app and connect to a new bluetooth device.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        ArrayList<Integer> integerList = db.getPricesAndSeats();

        try {
            String MacAddress = db.getMacAddress();
            Log.d("#########", "Bluetooth enabled " + MacAddress);

            BluetoothDevice printer = bluetoothAdapter.getRemoteDevice(MacAddress.replaceAll("\\s", "")); // replace with your printer's MAC address
            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


            if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 9);
            }

            socket = printer.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
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
            String smallText = "www.elishparibahan.com";
            String normalText1 = "serial no: "+integerList.get(0);
            String normalText2 = "From: "+FromLocationSelected;
            String normalText3 = "To: "+endLocationSelected.get(0);


            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedDate = currentDate.format(dateFormatter);
            String date = "issue date and time: "+formattedDate;

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

            // Print small text in the center
            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            outputStream.write(smallText.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // Print normal text 1
            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(normalText1.getBytes("UTF-8"));
            outputStream.write("\n".getBytes("UTF-8"));

            // Print normal text 2 in bold
            outputStream.write(textSizeSmall);
            outputStream.write(boldOn);
            outputStream.write(normalText2.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("\n".getBytes("UTF-8"));


            // Print normal text 3 in bold and right-aligned
            byte[] alignRight = {0x1B, 0x61, 0x02};


            outputStream.write(textSizeSmall);
            outputStream.write(boldOn);
            outputStream.write(normalText3.getBytes("UTF-8"));
            outputStream.write(boldOff);
            outputStream.write("\n".getBytes("UTF-8"));


            outputStream.write(alignStart);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write(date.getBytes("UTF-8"));
            outputStream.write("      ".getBytes("UTF-8"));

            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String timeString = time.format(formatter);


            outputStream.write(timeString.getBytes());
            outputStream.write("\n".getBytes("UTF-8"));



            String line1 = "------------------------------------------------\n";
            outputStream.write(line1.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOn);
            String line2 = "Amount: "+endLocationSelectedPrice.get(0);
            outputStream.write(line2.getBytes());

            outputStream.write(boldOff);

            outputStream.write("                       ".getBytes("UTF-8"));
            outputStream.write(boldOff);
            String line7 = "non-ac \n";
            outputStream.write(line7.getBytes());


            outputStream.write(boldOff);
            String line3 = "------------------------------------------------\n";
            outputStream.write(line3.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOff);
            String line4 = "Complain: "+ PhoneNumber +" \n";
            outputStream.write(line4.getBytes());


            String line5 = "------------------------------------------------\n";
            outputStream.write(line5.getBytes());
            outputStream.write(alignCenter);

            String line6 = "Developer: softwarepatron.com \n";
            outputStream.write(line6.getBytes());

            outputStream.write(printText);
            outputStream.write(eject);
            Log.d("*******", "ejecting");
            outputStream.flush();
            donePrint = true;

        } catch (Exception e) {
            parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "Connection failed, make sure your bluetooth device is turned on and restart this app.", Snackbar.LENGTH_LONG);
            snackbar.show();
            Log.i("SOCKET ERROR", " : " + e.toString());
        }

        if (donePrint){
            CallApiSendData();
            // printing is Done, Restart the class
            Intent intent = new Intent(this, ShortRouteBookingActivity.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled, do something
                requestBluetoothPermissions();
            } else {
                // Bluetooth was not enabled, do something else or show an error message
                Toast.makeText(getApplicationContext(), "You must Enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getTime(){
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define the date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Format the current date to "DD/MM/YYYY" format
        String formattedDate = currentDate.format(dateFormatter);
        todayDate.setText(formattedDate);
    }


    // Call this method to request Bluetooth permissions
    private void requestBluetoothPermissions() {


        if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 2);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 3);
        }
    }



    @Override
    public boolean AddDestinationLocation(String locationName, String price, int id) {
        if (endLocationSelected.isEmpty()) {
            endLocationSelected.add(locationName);
            endLocationSelectedPrice.clear();
            endLocationSelectedPrice.trimToSize();
            endLocationSelectedPrice.add(price);
            endLocationId = id;
            checkLocations();
          //  Log.d("PRICE", price);
            printbtn.setVisibility(View.VISIBLE);
        }

        return true;
    }

    @Override
    public void RemoveDestinationLocation(String locationName) {
        endLocationSelected.remove(locationName);
        checkLocations();
    }

    @Override
    public boolean isSelected() {
        if (endLocationSelected.isEmpty() && !clicked) {
            clicked = true;
            return false;
        } else if (endLocationSelected.size() > 0 && clicked) {
            clicked = false;
            return true;
        } else {
            return true;
        }

    }

    // start printing button here
    private void checkLocations() {
        if (endLocationSelected.size() > 0) {
            fromLoc.setText(FromLocationSelected);
            toLoc.setText(endLocationSelected.get(0));
            amount.setText(endLocationSelectedPrice.get(0));
        } else if (endLocationSelected.isEmpty()) {
            toLoc.setText("...");
            amount.setText("...");
        }
    }

    public void finishActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), ShortRouteFromLoationActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void EndSaleClick(View v){
        Intent intent = new Intent(getApplicationContext(), ShortRouteTicketActivity.class);
        startActivity(intent);
        this.finish();
    }



}
