package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static GhostKillTrackerClient SESSION;
    public static int totalWorms = 0;
    public static long startTime = System.currentTimeMillis();
    public static int hudX = 10;
    public static int hudY = 10;
    public static boolean scathaEnabled = true;

    @Override
    public void onInitializeClient() {
        SESSION = this;
    }

    public void addWorm() {
        totalWorms++;
    }

    public static String getWormsPerHour() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return "0.00";
        double hours = elapsed / 3600000.0;
        return String.format("%.2f", totalWorms / hours);
    }
}
