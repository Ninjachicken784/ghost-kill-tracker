package com.ghostkilltracker.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GhostKillHud {

    private final GhostKillTrackerClient tracker;

    public GhostKillHud(GhostKillTrackerClient tracker) {
        this.tracker = tracker;
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        // Only draw when the main game HUD is drawing text
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        
        String totalStr = "Total Worms: §a" + tracker.getTotalWorms();
        String rateStr = String.format("Worms/h: §e%.2f", tracker.getWormsPerHour());

        // Renders at the top left
        fr.drawStringWithShadow(totalStr, 5, 5, 0xFFFFFF);
        fr.drawStringWithShadow(rateStr, 5, 15, 0xFFFFFF);
    }
}
