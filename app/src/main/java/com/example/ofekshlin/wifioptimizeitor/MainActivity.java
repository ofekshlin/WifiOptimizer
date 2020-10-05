package com.example.ofekshlin.wifioptimizeitor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiController;

    private AvailableWifiNetworks mAdapter;
    private RecyclerView mWifiList;

    public static List<WifiConfiguration> configuredWifis;

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
        optimizeSwitch = (Switch) findViewById(R.id.optimize_switch);
        return true;
    }


    /*
    A section for the methods of the bottun optimize now (it is an old vertion of the action of the
    optimizition service
     */

    private void moveToBetterWifi(){
        List<ScanResult> wifisAvailable = wifiController.getScanResults();
        String bestWifi = "";
        int currntWifiLevel = wifiController.getConnectionInfo().getRssi();
        for(ScanResult net : wifisAvailable)
        {
            if(net.level > currntWifiLevel){
                currntWifiLevel = net.level;
                bestWifi = net.SSID;
            }
        }

        boolean moveAction = false;
        if (bestWifi != null) {
            moveAction = wifiController.enableNetwork(getWifiIdBySSID(bestWifi), true);
        }
        if (moveAction){
            Toast succeeded = Toast.makeText(getApplicationContext()
                    ,"Succeeded!", Toast.LENGTH_SHORT);
            succeeded.show();
        }
        else{
            Toast faild = Toast.makeText(getApplicationContext()
                    ,"Try again!", Toast.LENGTH_SHORT);
            faild.show();
        }
    }

    public int getWifiIdBySSID(String s){
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            if(s.equals(net.SSID)){
                return net.networkId;
            }
        }
        return 0;
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
}
