package com.example.ofekshlin.wifioptimizeitor.Logs;

public class LoggerFactory {
    public static ILogger getLogger() {
        return new LogcatLogger();
    }
}
