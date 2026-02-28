package com.ghostkilltracker.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GhostTrackerEditScreen extends Screen {
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    public GhostTrackerEditScreen() {
        super(Text.literal("Edit Ghost Tracker"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        GhostKillHud.render(ctx, client);
        ctx.drawCenteredTextWithShadow(textRenderer,
            Text.literal("§eDrag the tracker to reposition. Press ESC to save."),
            width / 2, height - 20, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int hx = GhostKillTrackerClient.hudX;
        int hy = GhostKillTrackerClient.hudY;
        if (mouseX >= hx && mouseX <= hx + 165 && mouseY >= hy && mouseY <= hy + 220) {
            dragging = true;
            dragOffsetX = (int) mouseX - hx;
            dragOffsetY = (int) mouseY - hy;
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            GhostKillTrackerClient.hudX = (int) mouseX - dragOffsetX;
            GhostKillTrackerClient.hudY = (int) mouseY - dragOffsetY;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return true;
    }

    @Override
    public boolean shouldPause() { return false; }
}
