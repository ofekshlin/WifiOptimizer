package com.example.ofekshlin.wifioptimizeitor;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import java.util.List;

public class WifiDataService extends Service {

    public static List<WifiNetwork> AvalibaleWifis;
    private WifiManager mWifiController;

    public WifiDataService(){ super();}

    @Override
    public void onCreate() {
        super.onCreate();
        mWifiController = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
