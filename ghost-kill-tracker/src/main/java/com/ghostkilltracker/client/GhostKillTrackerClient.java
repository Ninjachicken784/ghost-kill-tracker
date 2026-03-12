package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class GhostKillTrackerClient implements ClientModInitializer {
    // --- GHOST DATA ---
    public static int ghostKills = 0;
    public static double ghostPerHour = 0.0;

    // --- WORM DATA ---
    public static int wormKills = 0;
    public static double wormPerHour = 0.0;
    
    // --- SESSION DATA ---
    public static long startTime = System.currentTimeMillis();
    public static int hudX = 10;
    public static int hudY = 10;

    private boolean rWasDown = false;

    @Override
    public void onInitializeClient() {
        
        // 1. CHAT LISTENER (The Logic)
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String msg = message.getString().toLowerCase();

            // WORM DETECTION
            if (msg.contains("something approaching")) {
                wormKills++;
                updateRates();
            }

            // GHOST DETECTION (Matching standard kill messages)
            if (msg.contains("the ghost of") && msg.contains("defeated")) {
                ghostKills++;
                updateRates();
            }
        });

        // 2. RESET LOGIC (The R Key)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82); // 82 = R
            if (rDown && !rWasDown) {
                resetStats();
                client.player.sendMessage(Text.literal("§c[!] All Stats Reset"), true);
            }
            rWasDown = rDown;
        });

        // 3. HUD REGISTRATION
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });
        
        // Register other files
        GhostTrackerCommand.register();
    }

    public static void updateRates() {
        long elapsed = System.currentTimeMillis() - startTime;
        double hours = elapsed / 3600000.0;
        if (hours > 0) {
            wormPerHour = wormKills / hours;
            ghostPerHour = ghostKills / hours;
        }
    }

    public static String getSessionTime() {
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    }

    public static void resetStats() {
        wormKills = 0;
        ghostKills = 0;
        wormPerHour = 0;
        ghostPerHour = 0;
        startTime = System.currentTimeMillis();
    }
}
