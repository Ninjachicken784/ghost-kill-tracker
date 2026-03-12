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
        KillSession s = GhostKillTrackerClient.SESSION;

        // --- GHOST STATS ---
        if (GhostKillTrackerClient.ghostEnabled) {
            ctx.fill(x - 2, y - 2, x + 140, y + 45, 0x80000000); // Background
            ctx.drawTextWithShadow(client.textRenderer, "§bGhost Tracker", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fKills: §6" + DF0.format(s.getSessionKills()), x, y + 12, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fKills/h: §6" + DF1.format(s.getSessionKillsPerHour()), x, y + 22, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fTotal Kills: §6" + DF0.format(s.getTotalKills()), x, y + 32, 0xFFFFFF);
            y += 55; // Space between boxes
        }

        // --- WORM STATS ---
        if (GhostKillTrackerClient.scathaEnabled) {
            ctx.fill(x - 2, y - 2, x + 140, y + 35, 0x80000000); // Background
            ctx.drawTextWithShadow(client.textRenderer, "§eWorm Tracker", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms: §6" + DF0.format(GhostKillTrackerClient.wormCount), x, y + 12, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms/h: §6" + DF1.format(GhostKillTrackerClient.wormRate), x, y + 22, 0xFFFFFF);
        }
    }
}
