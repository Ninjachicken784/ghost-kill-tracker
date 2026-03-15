package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GhostKillTrackerClient implements ClientModInitializer {
    // These static variables fix the "cannot find symbol" errors in your other files
    public static GhostKillTrackerClient SESSION;
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    public static boolean dropsEnabled = true;
    public static int hudX = 10;
    public static int hudY = 10;

    // Worm Tracker Variables
    private int totalWorms = 0;
    private long startTime = System.currentTimeMillis();

    @Override
    public void onInitializeClient() {
        SESSION = this;
    }

    // This is called by your Mixin when a chat message is received
    public void handleChat(String message) {
        if (message.contains("You hear the sound of something approaching...")) {
            totalWorms++;
        }
    }

    // Logic for other files to call
    public void addSorrow() { /* Logic for ghosts */ }
    public void addPlasma() { /* Logic for ghosts */ }

    public int getTotalWorms() { return totalWorms; }
    
    public double getWormsPerHour() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 1000) return 0;
        return (totalWorms / (elapsed / 3600000.0));
    }
}
