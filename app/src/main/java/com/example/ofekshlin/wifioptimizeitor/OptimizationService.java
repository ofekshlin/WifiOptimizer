package com.example.ofekshlin.wifioptimizeitor;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class OptimizationService extends Service {

    private WifiManager wifiController;
    private long timeToWait;
    private Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        wifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        timeToWait = Integer.parseInt(this.getResources().getString(R.string.WifiCheckInterval));
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                moveToBetterWifi();
            }
        }, 0, timeToWait);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        timer.cancel();
    }

    private void moveToBetterWifi(){
        List<ScanResult> wifisAvailable = wifiController.getScanResults();
        String bestWifi = null;
        int currntWifiLevel = wifiController.getConnectionInfo().getRssi();
        for(ScanResult net : wifisAvailable)
        {
            if(net.level > currntWifiLevel && networkInConfiguredNetworks(net.SSID)){
                currntWifiLevel = net.level;
                bestWifi = net.SSID;
            }
        }

        if (bestWifi != null) {
            wifiController.enableNetwork(getWifiIdBySSID(bestWifi), true);
        }

    }

    private int getWifiIdBySSID(String ssid){
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            if(ssid.equals(net.SSID)){
                return net.networkId;
            }
        }
        return 0;
    }

    private boolean networkInConfiguredNetworks(String ssid) {
        List<WifiConfiguration> wifisAvailable = wifiController.getConfiguredNetworks();
        for(WifiConfiguration net : wifisAvailable){
            if(ssid.equals(net.SSID)){
                return true;
            }
        }
        return false;
    }
}
