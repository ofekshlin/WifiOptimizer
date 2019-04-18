package com.example.ofekshlin.wifioptimizeitor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AvailableWifiNetworks extends RecyclerView.Adapter<AvailableWifiNetworks.WifiViewHolder> {

    private String[] availableWifis;


    public AvailableWifiNetworks(){ availableWifis = null; }
    public AvailableWifiNetworks(String[] myDataset) {
        availableWifis = myDataset;
    }


    public class WifiViewHolder extends RecyclerView.ViewHolder{

        public TextView ssid;
        public ImageView wifiLevel;

        public WifiViewHolder(View itemView){
            super(itemView);

            ssid = (TextView) itemView.findViewById(R.id.item_name);
            wifiLevel = (ImageView) itemView.findViewById(R.id.wifi_level_tv);
        }

        public void bind(String s){
            ssid.setText(s);
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
        wifiViewHolder.bind(String.valueOf(i));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
