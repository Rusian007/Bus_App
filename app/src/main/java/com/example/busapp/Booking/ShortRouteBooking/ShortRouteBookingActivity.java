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
import android.content.IntentFilter;
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

import androidx.annotation.NonNull;
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
import com.example.busapp.retrofit.ApiModels.ShortRouteModel;
import com.example.busapp.retrofit.ApiModels.ShortRoutePointModel;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShortRouteBookingActivity extends AppCompatActivity implements ToLocationAdapter.IEndLocation {

    ArrayList<ShortRoute_LocationModel> toLocations = new ArrayList<>();
    String FromLocationSelected;
    boolean donePrint = false;
    Button printbtn;
    boolean clicked = false;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver broadcastReceiver;

    int REQUEST_ENABLE_BT = 0;

    private TextView fromLoc, toLoc, amount;
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

        setContentView(R.layout.short_route_to_location);

        Intent intent = getIntent();
        db = new Database(ShortRouteBookingActivity.this);

        FromLocationSelected = db.getShortLocationCache();


        fromLoc = findViewById(R.id.fromLocation);
        fromLoc.setText(FromLocationSelected);
        toLoc = findViewById(R.id.toLocation);
        amount = findViewById(R.id.shortRoute_amount);
        printbtn = (Button) findViewById(R.id.ShortprintBtn);


        // call API - start a method
        callApi();
        BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
        bluetoothAdapter = bluetoothManager.getAdapter();

        printbtn.setVisibility(View.INVISIBLE);


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
                            if (db.doesRouteExist(route.getStartingPointName())) {
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

    void getAndSetLocation(Cursor cv) {
        String endingLocationName = cv.getString(cv.getColumnIndex("ending_point_name"));
        double fair = cv.getDouble(cv.getColumnIndex("fair"));
        toLocations.add(new ShortRoute_LocationModel(endingLocationName, String.valueOf(fair)));
        // Do something with the retrieved values

        Log.d("DATABASE", "Point: , " + fair + ", " + endingLocationName);
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
            } else {
                requestBluetoothPermissions();
            }

           // discoverDevices();
           Bluetoothprint();
/*

            Intent intent = new Intent(this, ShortRouteBookingActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, 1000);
*/

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
        String MacAddress = db.getMacAddress();
        Log.d("#########", "Bluetooth enabled "+MacAddress);
        BluetoothSocket socket = null;
        BluetoothDevice printer = bluetoothAdapter.getRemoteDevice(MacAddress.replaceAll("\\s", "")); // replace with your printer's MAC address
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


        try {

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
            String normalText1 = "serial no:00001 ";
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
            byte[] textSizeBig = {0x1D, 0x21, 0x11};

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
            outputStream.write("\n\n".getBytes("UTF-8"));

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


            outputStream.write(alignCenter);
            outputStream.write(textSizeSmall);
            outputStream.write(boldOff);
            outputStream.write(date.getBytes("UTF-8"));
            outputStream.write("     ".getBytes("UTF-8"));

            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String timeString = time.format(formatter);


            outputStream.write(timeString.getBytes());
            outputStream.write("\n".getBytes("UTF-8"));

            String line1 = "------------------------------------------------\n";
            outputStream.write(line1.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOn);
            String line2 = "Amount: "+endLocationSelectedPrice.get(0)+" /- \n";
            outputStream.write(line2.getBytes());

            outputStream.write(boldOff);
            String line3 = "------------------------------------------------\n";
            outputStream.write(line3.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOff);
            String line4 = "Complain: 01409978008 \n";
            outputStream.write(line4.getBytes());

            outputStream.write(alignStart);
            outputStream.write(boldOff);
            String line7 = "non-ac \n";
            outputStream.write(line7.getBytes());


            outputStream.write(boldOff);
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
            throw new RuntimeException(e);
        }

        if (donePrint){
            // printing is Done, Restart the class
            Intent intent = new Intent(this, ShortRouteBookingActivity.class);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            }, 1000);
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
    public boolean AddDestinationLocation(String locationName, String price) {
        if (endLocationSelected.isEmpty()) {
            endLocationSelected.add(locationName);
            endLocationSelectedPrice.add(price);
            checkLocations();
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


}
