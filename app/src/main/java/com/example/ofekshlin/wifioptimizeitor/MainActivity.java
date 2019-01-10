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
        Button mOptimizeButton = findViewById(R.id.optimize_button);
        showAllWifisAvailable();
        mOptimizeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                moveToBetterWifi();
            }
        });

    }

    private void showAllWifisAvailable(){
        mTvWifiAvailable.append("\n");
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            mTvWifiAvailable.append("\n" + net.SSID + "\n");
        }
    }

    private void moveToBetterWifi(){
        mDebag.setText("Debag + \n");
        List<ScanResult> wifisAvailable = wifiController.getScanResults();
        String bestWifi = "";
        int currntWifiLevel = wifiController.getConnectionInfo().getRssi();
        mDebag.append("cwl:" + currntWifiLevel + "\n");
        for(ScanResult net : wifisAvailable)
        {
            mDebag.append("nw: " + net.SSID + " nwl: " + net.level + "\n");
            if(net.level > currntWifiLevel){
                mDebag.append(" got in the if \n nw: " + net.SSID + " nwl: " + net.level + "\n");
                currntWifiLevel = net.level;
                bestWifi = net.SSID;
            }
        }
        mDebag.append("the bestWifi is: " + bestWifi);
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
