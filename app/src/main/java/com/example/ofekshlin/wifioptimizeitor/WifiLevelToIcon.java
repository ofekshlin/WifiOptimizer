package com.example.ofekshlin.wifioptimizeitor;

import java.util.HashMap;

public class WifiLevelToIcon {
    public static HashMap<Integer, Integer> levelToIcon = new HashMap<Integer, Integer>() {{
        put(0, R.drawable.ic_signal_wifi_0_bar_black_24dp); // Very Low
        put(1, R.drawable.ic_signal_wifi_1_bar_black_24dp); // Low
        put(2, R.drawable.ic_signal_wifi_2_bar_black_24dp); // Medium
        put(3, R.drawable.ic_signal_wifi_3_bar_black_24dp); // High
        put(4, R.drawable.ic_signal_wifi_4_bar_black_24dp); // Very High
    }};
}