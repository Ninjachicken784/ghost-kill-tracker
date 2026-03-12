package com.ghostkilltracker.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class GhostTrackerEditScreen extends Screen {
    public GhostTrackerEditScreen() {
        super(Text.literal("Edit HUD"));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        GhostKillTrackerClient.hudX = (int) mouseX;
        GhostKillTrackerClient.hudY = (int) mouseY;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, "Drag to move the HUD", this.width / 2, 20, -1);
        GhostKillHud.render(context, this.client);
        super.render(context, mouseX, mouseY, delta);
    }
}
