package com.example.ofekshlin.wifioptimizeitor.Logs;

public interface ILogger {
    void Error(String tag, String message);
    void Info(String tag, String message);
    void Debug(String tag, String message);
}
