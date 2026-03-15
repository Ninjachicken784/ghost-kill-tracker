package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;

public class GhostKillTrackerClient implements ClientModInitializer {
    // Static instance so other files can find it
    public static GhostKillTrackerClient SESSION;

    // Settings (Fixed symbols for your Menu Screen)
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true; // This toggles the Worm tracker too
    public static boolean dropsEnabled = true;
    public static int hudX = 10;
    public static int hudY = 10;

    // Worm Stats
    private int totalWorms = 0;
    private long startTime = System.currentTimeMillis();

    @Override
    public void onInitializeClient() {
        SESSION = this;
    }

    public void addWorm() {
        this.totalWorms++;
    }

    // Placeholders to fix the errors in your Mixin
    public void addSorrow() { /* You can add ghost logic here later */ }
    public void addPlasma() { /* You can add ghost logic here later */ }

    public int getTotalWorms() {
        return totalWorms;
    }

    public double getWormsPerHour() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return 0.0; // Avoid division by zero
        return totalWorms / (elapsed / 3600000.0);
    }
}
