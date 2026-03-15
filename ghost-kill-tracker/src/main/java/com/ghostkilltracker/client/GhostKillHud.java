package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class GhostKillHud {

    // This matches the "render(DrawContext, MinecraftClient)" error in your logs
    public static void render(DrawContext context, MinecraftClient client) {
        if (!GhostKillTrackerClient.scathaEnabled) return;

        GhostKillTrackerClient session = GhostKillTrackerClient.SESSION;
        if (session == null) return;

        String totalText = "Total Worms: §a" + session.getTotalWorms();
        String rateText = String.format("Worms/h: §e%.2f", session.getWormsPerHour());

        // Draw the text onto the screen at the saved coordinates
        context.drawTextWithShadow(client.textRenderer, totalText, 
            GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY, 0xFFFFFF);
        
        context.drawTextWithShadow(client.textRenderer, rateText, 
            GhostKillTrackerClient.hudX, GhostKillTrackerClient.hudY + 10, 0xFFFFFF);
    }
}
