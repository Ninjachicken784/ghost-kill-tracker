package com.ghostkilltracker.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GhostTrackerEditScreen extends Screen {
    public GhostTrackerEditScreen() {
        super(Text.literal("Edit HUD Position"));
    }

    // In 1.21.1, the signature is double, double, int, double, double
    // If the @Override fails, it's usually because the button (int) or deltas are different.
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        GhostKillTrackerClient.hudX = (int) mouseX;
        GhostKillTrackerClient.hudY = (int) mouseY;
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        GhostKillHud.render(context, this.client);
    }
}
