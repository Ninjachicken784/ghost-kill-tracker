package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.text.DecimalFormat;

public class GhostKillHud {
    private static final DecimalFormat DF0 = new DecimalFormat("#,##0");
    private static final DecimalFormat DF1 = new DecimalFormat("#,##0.0");

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;
        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;

        // ONLY RENDER GHOST BOX IF ENABLED
        if (GhostKillTrackerClient.ghostEnabled) {
            drawBox(ctx, x, y, 160, 45, "Ghost Session");
            drawRow(ctx, client, x, y + 15, "Kills", DF0.format(GhostKillTrackerClient.SESSION.getSessionKills()));
            drawRow(ctx, client, x, y + 26, "Kills/h", DF1.format(GhostKillTrackerClient.SESSION.getSessionKillsPerHour()));
            y += 55; // Move down for next box
        }

        // ONLY RENDER SCATHA BOX IF ENABLED
        if (GhostKillTrackerClient.scathaEnabled) {
            drawBox(ctx, x, y, 160, 45, "Scatha Tracker");
            drawRow(ctx, client, x, y + 15, "Worms", DF0.format(GhostKillTrackerClient.wormCount));
            drawRow(ctx, client, x, y + 26, "Worms/h", DF1.format(GhostKillTrackerClient.wormRate));
        }
    }

    private static void drawRow(DrawContext ctx, MinecraftClient client, int x, int y, String label, String val) {
        ctx.drawTextWithShadow(client.textRenderer, "§7" + label, x + 5, y, 0xCCCCCC);
        ctx.drawTextWithShadow(client.textRenderer, "§e" + val, x + 100, y, 0xFFFFFF);
    }

    private static void drawBox(DrawContext ctx, int x, int y, int w, int h, String title) {
        ctx.fill(x, y, x + w, y + h, 0xCC1A1A1A);
        ctx.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, "§f" + title, x + 5, y + 4, 0xFFFFFF);
    }
}
