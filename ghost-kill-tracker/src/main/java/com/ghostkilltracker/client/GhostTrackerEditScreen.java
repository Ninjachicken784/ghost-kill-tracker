package com.ghostkilltracker.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.client.util.Click; // CRITICAL: This is the new import for 1.21.2+

public class GhostTrackerEditScreen extends Screen {
    public GhostTrackerEditScreen() {
        super(Text.literal("Edit HUD Position"));
    }

    /**
     * Updated for Minecraft 1.21.2+ 
     * The Click object contains the current X/Y and the button pressed.
     */
    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        // We pull the coordinates directly from the click object
        GhostKillTrackerClient.hudX = (int) click.x();
        GhostKillTrackerClient.hudY = (int) click.y();
        
        // We don't call super.mouseDragged here because we are handling the movement logic ourselves
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        // Ensure this matches the static method in your GhostKillHud class
        GhostKillHud.render(context, this.client);
    }
}
