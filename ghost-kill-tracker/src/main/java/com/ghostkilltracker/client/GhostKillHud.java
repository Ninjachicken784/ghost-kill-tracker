package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GhostKillHud {
    public static void render(DrawContext context, MinecraftClient client) {
        if (!GhostKillTrackerClient.scathaEnabled) return;

        String line1 = "Total Worms: §a" + GhostKillTrackerClient.totalWorms;
        String line2 = "Worms/h: §e" + GhostKillTrackerClient.getWormsPerHour();

        context.drawTextWithShadow(client.textRenderer, line1, GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, line2, GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY + 10, 0xFFFFFF);
    }
}
