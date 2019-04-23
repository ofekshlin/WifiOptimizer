package com.example.ofekshlin.wifioptimizeitor;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

public class WifiNetwork {

    private String SSID;
    private int RSSI;
    private SignalLevel signalLevel;
    private double frequency;
    private int linkSpeed;
    private ScanResult relatedScanResult;
    private boolean isConfigured;
    private WifiConfiguration relatedWifiConfiguration;

    public WifiNetwork(ScanResult scanResult){
        SSID = scanResult.SSID;
        RSSI = scanResult.level;
        defineSignalLevelByRSSI();
        relatedScanResult = scanResult;
        relatedWifiConfiguration = getWifiBySSID(SSID, MainActivity.cofiguredWifis);
        if(relatedWifiConfiguration != null){
            isConfigured = true;
        }
        else{
            isConfigured = false;
        }
    }

    // gets and sets
    public String getSSID(){return SSID;}
    public int getRSSI(){return RSSI;}
    public void setRSSI(int r){
        if(r <= 0 && r >= -100){
            RSSI = r;
            defineSignalLevelByRSSI();
        }
    }
    public double getFrequency(){return frequency;}
    public void setFrequency(double f){frequency = f;}
    public int getLinkSpeed(){return linkSpeed;}
    public void setLinkSpeed(int ls){
        if(ls > 0){
            linkSpeed = ls;
        }
    }
    public ScanResult getRelatedScanResult(){return relatedScanResult;}
    public void setRelatedScanResult(ScanResult sr){
        relatedScanResult = sr;
        RSSI = sr.level;
        defineSignalLevelByRSSI();
    }
    public boolean getIsConfigured(){return isConfigured;}

    //needed function

    public WifiConfiguration getWifiBySSID(String ssid, List<WifiConfiguration> wifisAvailable){
        for(WifiConfiguration net : wifisAvailable){
            if(ssid.equals(net.SSID)){
                return net;
            }
        }
        return null;
    }

    public void defineSignalLevelByRSSI(){
        int level = WifiManager.calculateSignalLevel(this.RSSI, 5);
        switch(level){
            case 0:
                signalLevel = SignalLevel.VeryLow;
                break;
            case 1:
                signalLevel = SignalLevel.Low;
                break;
            case 2:
                signalLevel = SignalLevel.Medium;
                break;
            case 3:
                signalLevel = SignalLevel.High;
                break;
            case 4:
                signalLevel = SignalLevel.VeryHigh;
                break;
        }
    }

}