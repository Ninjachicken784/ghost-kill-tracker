package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class GhostKillTrackerClient implements ClientModInitializer {
    // These MUST match the names the HUD is looking for
    public static int wormKills = 0;
    public static int ghostKills = 0;
    public static double wormPerHour = 0.0;
    public static long startTime = System.currentTimeMillis();
    
    // HUD Position
    public static int hudX = 10;
    public static int hudY = 10;

    @Override
    public void onInitializeClient() {
        // Init Keybinds
        GhostKillKeybinds.register();
        // Init Commands
        GhostTrackerCommand.register();

        // The Chat Tracker
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String msg = message.getString().toLowerCase();
            if (msg.contains("something approaching")) {
                wormKills++;
                updateRates();
            }
        });

        // The HUD Hook
        HudRenderCallback.EVENT.register((ctx, delta) -> {
            GhostKillHud.render(ctx, MinecraftClient.getInstance());
        });
    }

    public static void updateRates() {
        long elapsed = System.currentTimeMillis() - startTime;
        double hours = elapsed / 3600000.0;
        wormPerHour = (hours > 0) ? (wormKills / hours) : 0;
    }

    public static String getSessionTime() {
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }
    
    public static void resetStats() {
        wormKills = 0;
        ghostKills = 0;
        wormPerHour = 0;
        startTime = System.currentTimeMillis();
    }
}
