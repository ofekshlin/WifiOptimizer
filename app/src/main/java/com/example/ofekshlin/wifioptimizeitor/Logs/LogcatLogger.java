package com.example.ofekshlin.wifioptimizeitor.Logs;

import android.util.Log;

public class LogcatLogger implements ILogger {

    public LogcatLogger() {}

    @Override
    public void Error(String tag, String message) {
        Log.e(tag, message);
    }

    @Override
    public void Info(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public void Debug(String tag, String message) {
        Log.d(tag, message);
    }
}
