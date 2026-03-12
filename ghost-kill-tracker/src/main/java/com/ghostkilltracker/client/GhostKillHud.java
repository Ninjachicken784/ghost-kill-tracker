package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import java.text.DecimalFormat;

public class GhostKillHud {
    private static final DecimalFormat DF0 = new DecimalFormat("#,##0");
    private static final DecimalFormat DF1 = new DecimalFormat("#,##0.0");

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;
        
        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;
        
        // Use direct variables to avoid "Empty Box" errors
        int gKills = GhostKillTrackerClient.SESSION.getSessionKills();
        long elapsed = GhostKillTrackerClient.SESSION.getElapsedTime();
        double gRate = (gKills / (Math.max(1, elapsed) / 3600000.0));
        
        // 1. GHOST BOX
        if (GhostKillTrackerClient.ghostEnabled) {
            ctx.fill(x - 4, y - 4, x + 145, y + 45, 0x90000000); // Darker, slightly larger
            ctx.drawTextWithShadow(client.textRenderer, "§b§lGHOST TRACKER", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fSession: §6" + DF0.format(gKills), x, y + 12, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fKills/H: §6" + DF1.format(gRate), x, y + 22, 0xFFFFFF);
            // Using the session's internal total if available, or just session for now to ensure NO CRASH
            ctx.drawTextWithShadow(client.textRenderer, "§fTotal: §6" + DF0.format(gKills), x, y + 32, 0xFFFFFF);
            y += 55; 
        }

        // 2. WORM BOX
        if (GhostKillTrackerClient.scathaEnabled) {
            ctx.fill(x - 4, y - 4, x + 145, y + 35, 0x90000000);
            ctx.drawTextWithShadow(client.textRenderer, "§e§lWORM TRACKER", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms: §6" + DF0.format(GhostKillTrackerClient.wormCount), x, y + 12, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms/H: §6" + DF1.format(GhostKillTrackerClient.wormRate), x, y + 22, 0xFFFFFF);
        }
    }
}
