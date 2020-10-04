package com.example.ofekshlin.wifioptimizeitor;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.wifi.WifiConfiguration;

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
            int level = WifiManager.calculateSignalLevel(sr.level, 5);
            if (WifiLevelToIcon.levelToIcon.containsKey(level)) {
                wifiLevel.setImageResource(WifiLevelToIcon.levelToIcon.get(level));
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
}
