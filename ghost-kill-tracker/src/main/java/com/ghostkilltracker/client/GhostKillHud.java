package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GhostKillHud {
    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;

        ctx.fill(x - 5, y - 5, x + 120, y + 40, 0x90000000); // Background box

        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§eWorm Kills: §f" + GhostKillTrackerClient.wormKills), x, y, -1);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§eWorms/H: §f" + String.format("%.1f", GhostKillTrackerClient.wormPerHour)), x, y + 12, -1);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§eSession: §f" + GhostKillTrackerClient.getSessionTime()), x, y + 24, -1);
    }
}
