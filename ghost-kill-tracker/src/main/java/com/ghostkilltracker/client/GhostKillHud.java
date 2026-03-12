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

        // --- GHOST SECTION (Kept original) ---
        if (GhostKillTrackerClient.ghostEnabled) {
            ctx.fill(x - 2, y - 2, x + 140, y + 42, 0x80000000); 
            ctx.drawTextWithShadow(client.textRenderer, "§bGhost Tracker", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fKills: §6" + DF0.format(GhostKillTrackerClient.SESSION.getSessionKills()), x, y + 10, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fKills/H: §6" + DF1.format(GhostKillTrackerClient.SESSION.getSessionKillsPerHour()), x, y + 20, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fTotal: §6" + DF0.format(GhostKillTrackerClient.SESSION.getTotalKills()), x, y + 30, 0xFFFFFF);
            y += 50; 
        }

        // --- WORM SECTION (The New Part) ---
        if (GhostKillTrackerClient.scathaEnabled) {
            ctx.fill(x - 2, y - 2, x + 140, y + 32, 0x80000000);
            ctx.drawTextWithShadow(client.textRenderer, "§eWorm Tracker", x, y, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms: §6" + DF0.format(GhostKillTrackerClient.wormCount), x, y + 10, 0xFFFFFF);
            ctx.drawTextWithShadow(client.textRenderer, "§fWorms/H: §6" + DF1.format(GhostKillTrackerClient.wormRate), x, y + 20, 0xFFFFFF);
        }
    }
}
