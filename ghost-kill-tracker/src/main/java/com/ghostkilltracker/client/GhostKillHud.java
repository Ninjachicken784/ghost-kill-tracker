package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GhostKillHud {
    public static void render(DrawContext context, MinecraftClient client) {
        if (!GhostKillTrackerClient.scathaEnabled || client.textRenderer == null) return;

        String worms = "Total Worms: §a" + GhostKillTrackerClient.totalWorms;
        String rate = "Worms/h: §e" + GhostKillTrackerClient.getWormsPerHour();

        context.drawTextWithShadow(client.textRenderer, worms, GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY, 0xFFFFFF);
        context.drawTextWithShadow(client.textRenderer, rate, GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY + 10, 0xFFFFFF);
    }
}
