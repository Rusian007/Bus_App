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
import android.widget.FrameLayout;
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
    boolean loading = true, deviceConnected = false;
    View parentLayout ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.route_choose_view);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(getApplicationContext(), "Device does not support bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }

        parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, "Connecting to Bluetooth, please wait", Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbarView.setLayoutParams(params);
        snackbar.show();

        db = new Database(ChooseRouteActivity.this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
        if(ContextCompat.checkSelfPermission(ChooseRouteActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
        }
        if(ContextCompat.checkSelfPermission(ChooseRouteActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 6);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,  new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 3);
        }

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Bluetooth was enabled, do something
                Toast.makeText(getApplicationContext(), "Scanning started", Toast.LENGTH_SHORT).show();
                loading = false;
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

    public void finishActivity(View v){
        this.finish();
    }
}
