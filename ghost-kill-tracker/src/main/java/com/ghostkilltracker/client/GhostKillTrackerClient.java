package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static GhostKillTrackerClient SESSION;
    
    // settings for HUD and Menu
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    public static boolean dropsEnabled = true;
    public static int hudX = 10;
    public static int hudY = 10;

    private int totalWorms = 0;
    private long startTime = System.currentTimeMillis();

    @Override
    public void onInitializeClient() {
        SESSION = this;
    }

    // This is the method the Mixin was missing
    public void addWorm() {
        this.totalWorms++;
    }

    public void addSorrow() { /* placeholder for ghost tracking */ }
    public void addPlasma() { /* placeholder for ghost tracking */ }

    public int getTotalWorms() {
        return totalWorms;
    }

    public double getWormsPerHour() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return 0.0; 
        return totalWorms / (elapsed / 3600000.0);
    }
}
