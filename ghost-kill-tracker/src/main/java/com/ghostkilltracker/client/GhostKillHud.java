package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GhostKillHud {
    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        int x = GhostKillTrackerClient.hudX;
        int y = GhostKillTrackerClient.hudY;

        // Draw Background Box
        ctx.fill(x - 5, y - 5, x + 130, y + 50, 0x90000000);

        // 1. Worm Kills Line
        ctx.drawTextWithShadow(client.textRenderer, 
            Text.literal("§eWorm Kills: §f" + GhostKillTrackerClient.wormKills), x, y, -1);

        // 2. Worms/H Line
        ctx.drawTextWithShadow(client.textRenderer, 
            Text.literal("§eWorms/H: §f" + String.format("%.1f", GhostKillTrackerClient.wormPerHour)), x, y + 12, -1);

        // 3. Session Time Line
        ctx.drawTextWithShadow(client.textRenderer, 
            Text.literal("§eSession: §f" + GhostKillTrackerClient.getSessionTime()), x, y + 24, -1);
            
        // 4. Reset Instruction (Optional, helps you know it's there)
        ctx.drawTextWithShadow(client.textRenderer, 
            Text.literal("§7[R] to Reset"), x, y + 36, -1);
    }
}
