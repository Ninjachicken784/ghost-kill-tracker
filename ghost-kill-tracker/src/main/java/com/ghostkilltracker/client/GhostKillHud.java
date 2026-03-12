package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GhostKillHud {
    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;
        
        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;

        try {
            // --- GHOST ---
            if (GhostKillTrackerClient.ghostEnabled) {
                ctx.fill(x, y, x + 110, y + 20, 0x80000000); // Simple dark box
                
                // Convert the rate to a plain integer to prevent decimal crashes
                int gRate = 0;
                if (GhostKillTrackerClient.SESSION != null) {
                    gRate = (int) GhostKillTrackerClient.SESSION.getSessionKillsPerHour();
                }
                
                // Draw text directly, no fancy symbols
                ctx.drawTextWithShadow(client.textRenderer, "Ghost/H: " + gRate, x + 5, y + 6, 0x00FFFF);
                
                y += 25; // Move down for the next box
            }

            // --- WORM ---
            if (GhostKillTrackerClient.scathaEnabled) {
                ctx.fill(x, y, x + 110, y + 20, 0x80000000); // Simple dark box
                
                int wRate = (int) GhostKillTrackerClient.wormRate;
                
                // Draw text directly
                ctx.drawTextWithShadow(client.textRenderer, "Worms/H: " + wRate, x + 5, y + 6, 0xFFFF00);
            }

        } catch (Exception e) {
            // IF IT FAILS, IT WILL SHOW THIS INSTEAD OF AN EMPTY BOX
            ctx.drawTextWithShadow(client.textRenderer, "HUD CRASHED", x, y, 0xFF0000);
        }
    }
}
