package com.example.ofekshlin.wifioptimizeitor;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.List;


public class OptimizationService extends IntentService {

    private WifiManager wifiController;
    private long timeToWait;


    public OptimizationService(){
        super("OptimizeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        timeToWait = 6000;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Toast.makeText(getApplicationContext(), "Run Time!!", Toast.LENGTH_SHORT).show();
        moveToBetterWifi();
        SystemClock.sleep(timeToWait);
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
            Toast.makeText(getApplicationContext()
                    ,"Succeeded!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext()
                    ,"Try again!", Toast.LENGTH_SHORT).show();
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
