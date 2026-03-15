package com.ghostkilltracker.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class GhostKillTrackerClient {

    private int totalWorms = 0;
    private long startTime = System.currentTimeMillis();

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        // We use unformatted text to ignore color codes like §6
        String message = event.message.getUnformattedText();

        if (message.contains("You hear the sound of something approaching...")) {
            totalWorms++;
            // Optional: A little ding so you know it worked
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 1.0F);
        }
    }

    public int getTotalWorms() {
        return totalWorms;
    }

    public double getWormsPerHour() {
        long elapsedMillis = System.currentTimeMillis() - startTime;
        if (elapsedMillis < 1000) return 0.0; 
        
        return totalWorms / (elapsedMillis / 3600000.0);
    }

    public void resetStats() {
        this.totalWorms = 0;
        this.startTime = System.currentTimeMillis();
    }
}
