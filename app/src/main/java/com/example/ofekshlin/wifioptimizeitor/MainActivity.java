package com.example.ofekshlin.wifioptimizeitor;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiController;

    private AvailableWifiNetworks mAdapter;
    private RecyclerView mWifiList;

    private Switch optimizeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        askForGPSPermission();
        turnOnGPS();

        //set the configurdWifis var
        if (!wifiController.isWifiEnabled()) {
            wifiController.setWifiEnabled(true);
        }

        //Handle the RecyclerView
        mWifiList = findViewById(R.id.available_networks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mWifiList.setLayoutManager(layoutManager);

        mWifiList.setHasFixedSize(true);

        mAdapter = new AvailableWifiNetworks(wifiController);
        mWifiList.setAdapter(mAdapter);
    }


    /*
    A Section dedicate to handle the Menu that contain the switch to turn on\off the optimization
    service  (it is still Work in Progress
     */

    // Toast.makeText(MainActivity.this, "start optimize", Toast.LENGTH_SHORT);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        optimizeSwitch = menu.findItem(R.id.optimize_switch_place).getActionView().findViewById(R.id.optimize_switch);
        optimizeSwitch.setChecked(isMyServiceRunning(OptimizationService.class));
        optimizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    // Start service
                    startService(new Intent(MainActivity.this, OptimizationService.class));
                }

                else {
                    // Stop service
                    stopService(new Intent(MainActivity.this, OptimizationService.class));
                }
            }
        });
        return true;
    }

    private void turnOnGPS() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    private void askForGPSPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
