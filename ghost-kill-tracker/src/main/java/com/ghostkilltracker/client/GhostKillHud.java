package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.text.DecimalFormat;

public class GhostKillHud {
    private static final int BG_COLOR = 0x80000000; // Transparent Black
    private static final DecimalFormat DF0 = new DecimalFormat("#,##0");
    private static final DecimalFormat DF1 = new DecimalFormat("#,##0.0");

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;
        KillSession s = GhostKillTrackerClient.SESSION;
        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;
        int width = 140;

        // --- GHOST SECTION ---
        if (GhostKillTrackerClient.ghostEnabled) {
            // Draw the background plate
            ctx.fill(x - 2, y - 2, x + width, y + 52, BG_COLOR);
            
            // Draw the Header
            ctx.drawTextWithShadow(client.textRenderer, "§b§lGHOST TRACKER", x, y, 0xFFFFFF);
            y += 12;

            // Draw the Stats (Label : Value)
            drawStatRow(ctx, client, x, y, "Kills", DF0.format(s.getSessionKills()));
            drawStatRow(ctx, client, x, y + 10, "Kills/h", DF1.format(s.getSessionKillsPerHour()));
            drawStatRow(ctx, client, x, y + 20, "Sorrow", DF0.format(s.getSessionSorrow()));
            drawStatRow(ctx, client, x, y + 30, "Plasma", DF0.format(s.getSessionPlasma()));
            
            y += 50; // Move down for the next section
        }

        // --- WORM SECTION ---
        if (GhostKillTrackerClient.scathaEnabled) {
            // Draw the background plate
            ctx.fill(x - 2, y - 2, x + width, y + 32, BG_COLOR);
            
            // Draw the Header
            ctx.drawTextWithShadow(client.textRenderer, "§e§lWORM TRACKER", x, y, 0xFFFFFF);
            y += 12;

            // Draw the Stats
            drawStatRow(ctx, client, x, y, "Worms", DF0.format(GhostKillTrackerClient.wormCount));
            drawStatRow(ctx, client, x, y + 10, "Worms/h", DF1.format(GhostKillTrackerClient.wormRate));
        }
    }

    private static void drawStatRow(DrawContext ctx, MinecraftClient client, int x, int y, String label, String value) {
        // Draw Label on the left
        ctx.drawTextWithShadow(client.textRenderer, "§f" + label + ":", x, y, 0xFFFFFF);
        
        // Draw Value on the right
        int valWidth = client.textRenderer.getWidth(value);
        ctx.drawTextWithShadow(client.textRenderer, "§6" + value, x + 135 - valWidth, y, 0xFFFFFF);
    }
}
