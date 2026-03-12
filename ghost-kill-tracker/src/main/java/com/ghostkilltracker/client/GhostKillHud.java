package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GhostKillHud {
    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;

        // Draw background for the whole set
        ctx.fill(x - 5, y - 5, x + 130, y + 65, 0x90000000);

        // GHOST SECTION
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§bGhost Kills: §f" + GhostKillTrackerClient.ghostKills), x, y, -1);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§bGhost/H: §f" + String.format("%.1f", GhostKillTrackerClient.ghostPerHour)), x, y + 12, -1);

        // WORM SECTION
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§eWorm Kills: §f" + GhostKillTrackerClient.wormKills), x, y + 28, -1);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§eWorms/H: §f" + String.format("%.1f", GhostKillTrackerClient.wormPerHour)), x, y + 40, -1);

        // TIME SECTION
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§aSession: §f" + GhostKillTrackerClient.getSessionTime()), x, y + 54, -1);
    }
}
