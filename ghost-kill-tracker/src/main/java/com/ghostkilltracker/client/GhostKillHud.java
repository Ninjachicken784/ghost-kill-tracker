package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.util.Locale;

public class GhostKillHud {
    // We calculate everything here so we don't rely on other files being "ready"
    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null || client.textRenderer == null) return;
        
        // Use local variables so we aren't constantly pinging the other class
        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;
        
        // 1. CALCULATE GHOST STATS RIGHT HERE
        int totalGhosts = GhostKillTrackerClient.SESSION.getSessionKills();
        long ghostTime = GhostKillTrackerClient.SESSION.getElapsedTime();
        double ghostHr = (totalGhosts / (Math.max(1, ghostTime) / 3600000.0));
        String ghostText = String.format(Locale.US, "Ghost/H: %.1f", ghostHr);

        // 2. CALCULATE WORM STATS RIGHT HERE
        int totalWorms = GhostKillTrackerClient.wormCount;
        double wormHr = (totalWorms / (Math.max(1, ghostTime) / 3600000.0));
        String wormText = String.format(Locale.US, "Worm/H: %.1f", wormHr);

        // --- DRAW GHOST BOX ---
        if (GhostKillTrackerClient.ghostEnabled) {
            // Background Plate
            ctx.fill(x - 5, y - 5, x + 130, y + 25, 0x90000000); 
            // Header
            ctx.drawTextWithShadow(client.textRenderer, Text.literal("§b§lGHOST TRACKER"), x, y, -1);
            // The Actual Stat you asked for
            ctx.drawTextWithShadow(client.textRenderer, Text.literal("§f" + ghostText), x, y + 12, -1);
            
            y += 40; // Force space for the next one
        }

        // --- DRAW WORM BOX ---
        if (GhostKillTrackerClient.scathaEnabled) {
            // Background Plate
            ctx.fill(x - 5, y - 5, x + 130, y + 25, 0x90000000);
            // Header
            ctx.drawTextWithShadow(client.textRenderer, Text.literal("§e§lWORM TRACKER"), x, y, -1);
            // The Actual Stat you asked for
            ctx.drawTextWithShadow(client.textRenderer, Text.literal("§f" + wormText), x, y + 12, -1);
        }
    }
}
