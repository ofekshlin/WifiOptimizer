package com.example.ofekshlin.wifioptimizeitor;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class OptimizationService extends IntentService {

    private WifiManager wifiController;
    private long timeToWait;
    public static volatile boolean shouldContinue = false;

    private static final String TAG = "MyActivity";
    public OptimizationService(){
        super("OptimizeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        timeToWait = 3000;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while(true) {
            startOptimize();
        }
    }

    private void startOptimize(){
        if(shouldContinue){
            moveToBetterWifi();
            SystemClock.sleep(timeToWait);
        }
    }

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
