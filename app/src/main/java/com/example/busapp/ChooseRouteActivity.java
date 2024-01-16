package com.example.busapp;

import android.Manifest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;


import com.example.busapp.Booking.ShortRouteBooking.ShortRouteBookingActivity;
import com.example.busapp.Database.Database;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChooseRouteActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notification_channel";

    Database db;
    BluetoothAdapter bluetoothAdapter;
    BroadcastReceiver broadcastReceiver;
    boolean registered = false, deviceConnected = false;
    TextView logoutTextView;
    View parentLayout ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.route_choose_view);
         logoutTextView = findViewById(R.id.logout);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);

        }
        if (!bluetoothAdapter.isEnabled()) {
            Log.d("NOT", "Not Enabled");

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);

        } else {
            parentLayout = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(parentLayout, "Please connect to a new device, or the application will not work >_<.", Snackbar.LENGTH_LONG);
            snackbar.show();
            deviceConnected = true;
      }

        // Logout btn on click action
        Logout();
        db = new Database(ChooseRouteActivity.this);

        Button longRouteButton = findViewById(R.id.long_route);
        Button shortRouteButton = findViewById(R.id.short_route);

        String routeInfo = db.GetRouteLoginInfo(db);
        Log.d("ROUTEINFO", "route - " + routeInfo);

        if (routeInfo != null) {
            if (routeInfo.equals("SHORT")) {
                longRouteButton.setVisibility(View.GONE); // This will hide the button
            } else if (routeInfo.equals("LONG")) {
                shortRouteButton.setVisibility(View.GONE); // This will show the button
            }
        }

    }

    private void startSnackBar(){
        parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, "Searching for available devices, Please Wait.", Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.show();
    }



    protected void startBluetoothScan() {

        startSnackBar();
        bluetoothAdapter.startDiscovery();
        ArrayList<String> arrayList = new ArrayList<>();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(action.equals(BluetoothDevice.ACTION_FOUND)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("BLUETOOTH", ">> " + device.getName());

                    if(device.getName() != null){
                        arrayList.add(device.getName() +" -> "+device.getAddress());
                        Toast.makeText(getApplicationContext(), "Bluetooth Device found "+ device.getName(), Toast.LENGTH_SHORT).show();
                    }

                }
                else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                    // Create the AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChooseRouteActivity.this);
                    builder.setTitle("Select a device");

                    // Convert the ArrayList to a CharSequence array
                    CharSequence[] devices = arrayList.toArray(new CharSequence[arrayList.size()]);

                    // Add the devices to the dialog
                    builder.setItems(devices, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position of the selected device
                            String selectedDeviceName = arrayList.get(which);
                            String[] device = selectedDeviceName.split("->");
                            String MacAddress = device[1];

                            Snackbar snackbar = Snackbar.make(parentLayout, "Connected", Snackbar.LENGTH_SHORT);

                            boolean doesMacExist = db.doesMacExist();
                            if(doesMacExist){
                                db.setMacAddress(MacAddress);
                            } else{
                                db.newMacAddress(MacAddress);
                            }

                            View snackbarView = snackbar.getView();
                            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                            params.gravity = Gravity.TOP;
                            snackbarView.setLayoutParams(params);
                            snackbar.show();
                            deviceConnected = true;
                        }
                    });

                    // Show the dialog
                    builder.show();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
        registered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    if(registered){
         unregisterReceiver(broadcastReceiver);
    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Connect to a new device if not already.", Toast.LENGTH_SHORT).show();
                deviceConnected = true;
            } else {
                // Bluetooth was not enabled, do something else or show an error message
                Toast.makeText(getApplicationContext(), "You must Enable Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void goToShortRoute_View(View view){
        if(deviceConnected){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ROUTE", "SHORT");
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(this, "Need to connect to Bluetooth First", Toast.LENGTH_SHORT).show();
        }

    }

    public void goToLongRoute_View(View view){
        if(deviceConnected){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("ROUTE", "LONG");
            startActivity(intent);
            this.finish();
        }
        else {
            Toast.makeText(this, "Need to connect to Bluetooth First", Toast.LENGTH_SHORT).show();
        }
    }

    public void Logout(){
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform action when TextView is clicked
                if(db.IsTokenTableEmpty(db)){
                    Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_SHORT).show();
                } else {

                    if (db.DeleteToken(db)){
                        Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ChooseRouteActivity.this, ChooseRouteActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else{
                        Toast.makeText(getApplicationContext(), "Internal error, not logged out", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }




    public void startBouetoothButton(View view){
        startBluetoothScan();
    }
    public void finishActivity(View v){
        this.finish();
    }
}
