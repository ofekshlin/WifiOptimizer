package com.example.ofekshlin.wifioptimizeitor;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.*;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiController;
    private TextView mTvWifiAvailable;
    private TextView  mDebag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvWifiAvailable = findViewById(R.id.tv_wifi_available);
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mDebag = findViewById(R.id.debag);
        Button mChangeWifiButton = findViewById(R.id.change_wifi_button);
        showAllWifisAvailable();
        mChangeWifiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                moveToBetterWifi();
            }
        });
        Button mFuckme = findViewById(R.id.change_button);
        mChangeWifiButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                moveWifi();
            }
        });

    }

    private  void moveWifi(){
        List<ScanResult> wifisAvailable = wifiController.getScanResults();


    }

    private void showAllWifisAvailable(){
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            mTvWifiAvailable.append(net.SSID + "\n\n\n");
        }
    }

    private void moveToBetterWifi(){
        List<ScanResult> wifisAvailable = wifiController.getScanResults();
        String bestWifi = "";
        String currntWifi = wifiController.getConnectionInfo().getSSID();
        int currntWifiLevel = wifiController.getConnectionInfo().getRssi();
        mDebag.append("cwn: " + currntWifi + " cwl: " + currntWifiLevel + " \n");
        // code that doesn't requier
        /*
        for(ScanResult net : wifisAvailable){
            if(net.SSID.equals(currntWifi)){
                mDebag.append("cw=" + net.SSID + "L: " + net.level + "\n");
                currntWifiLevel = net.level;
            }
        }
        */
        for(ScanResult net : wifisAvailable)
        {
            if(net.level > currntWifiLevel){
                mDebag.append("nw: " + net.level + "\n");
                mDebag.append("nwn: " + net.SSID + "\n");
                bestWifi = net.SSID;
            }
        }
        Boolean moveAction = wifiController.enableNetwork(getWifiIdBySSID(bestWifi), true);
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

    private int getWifiIdBySSID(String s){
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            if(s.equals(net.SSID)){
                return net.networkId;
            }
        }
        return 0;
    }
}
