package com.example.ofekshlin.wifioptimizeitor;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AvailableWifiNetworks extends RecyclerView.Adapter<AvailableWifiNetworks.WifiViewHolder> {

    private WifiManager mWifiController;
    private List<ScanResult> availableWifis;


    public AvailableWifiNetworks(WifiManager wifiController) {
        mWifiController = wifiController;
        availableWifis = mWifiController.getScanResults();
    }


    public class WifiViewHolder extends RecyclerView.ViewHolder{

        public TextView ssid;
        public ImageView wifiLevel;

        public WifiViewHolder(View itemView){
            super(itemView);

            ssid = (TextView) itemView.findViewById(R.id.item_name);
            wifiLevel = (ImageView) itemView.findViewById(R.id.wifi_level_tv);
        }

        public void bind(ScanResult sr){

            ssid.setText(sr.SSID);
            switch(defineSignalLevelByRSSI(sr.level)){
                case VeryHigh:
                    wifiLevel.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_24dp);
                    break;
                case High:
                    wifiLevel.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_24dp);
                    break;
                case Medium:
                    wifiLevel.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_24dp);
                    break;
                case Low:
                    wifiLevel.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_24dp);
                    break;
                case VeryLow:
                    wifiLevel.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_24dp);
            }
        }

    }


    @NonNull
    @Override
    public WifiViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatley = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediatley);
        WifiViewHolder viewHolder = new WifiViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WifiViewHolder wifiViewHolder, int i) {
        wifiViewHolder.bind(availableWifis.get(i));
    }

    @Override
    public int getItemCount() {
        return availableWifis.size();
    }


    public static SignalLevel defineSignalLevelByRSSI(int rssi){
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        SignalLevel signalLevel = null;
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
        return signalLevel;
    }

}
