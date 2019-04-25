package com.example.ofekshlin.wifioptimizeitor;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.*;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiController;

    private AvailableWifiNetworks mAdapter;
    private RecyclerView mWifiList;

    public static List<WifiConfiguration> cofiguredWifis;

    private Switch optimizeSwitch;

    private Intent mOptimizationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        //set the configurdWifis var
        cofiguredWifis = wifiController.getConfiguredNetworks();

        //Handle the RecyclerView
        mWifiList = findViewById(R.id.available_networks);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mWifiList.setLayoutManager(layoutManager);

        mWifiList.setHasFixedSize(true);

        mAdapter = new AvailableWifiNetworks(wifiController);
        mWifiList.setAdapter(mAdapter);

        //start the optimization service
        mOptimizationService = new Intent(MainActivity.this, OptimizationService.class);
        startService(mOptimizationService);

        //handle the optimize now button
        Button optimizeButton = findViewById(R.id.optimize_button);
        optimizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToBetterWifi();
            }
        });

    }


    /*
    A Section dedicate to handle the Menu that contain the switch to turn on\off the optimization
    service  (it is still Work in Progress
     */

    // Toast.makeText(MainActivity.this, "start optimize", Toast.LENGTH_SHORT);
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        //handle the optimize switch
        optimizeSwitch = menu.findItem(R.id.optimize_switch_place).getActionView().findViewById(R.id.optimize_switch);
        optimizeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    OptimizationService.shouldContinue = true;
                } else {
                    OptimizationService.shouldContinue = false;
                }
            }
        });
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
        Boolean moveAction = false;
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

}
