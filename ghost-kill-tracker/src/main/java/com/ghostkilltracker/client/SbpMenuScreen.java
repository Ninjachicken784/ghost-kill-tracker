package com.ghostkilltracker.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class SbpMenuScreen extends Screen {
    public SbpMenuScreen() {
        super(Text.literal("SBP Menu"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Toggle Ghost Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Ghost Tracker: " + (GhostKillTrackerClient.ghostEnabled ? "§aON" : "§cOFF")),
            button -> {
                GhostKillTrackerClient.ghostEnabled = !GhostKillTrackerClient.ghostEnabled;
                button.setMessage(Text.literal("Ghost Tracker: " + (GhostKillTrackerClient.ghostEnabled ? "§aON" : "§cOFF")));
            }).dimensions(centerX - 100, centerY - 45, 200, 20).build());

        // Toggle Scatha Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Scatha Tracker: " + (GhostKillTrackerClient.scathaEnabled ? "§aON" : "§cOFF")),
            button -> {
                GhostKillTrackerClient.scathaEnabled = !GhostKillTrackerClient.scathaEnabled;
                button.setMessage(Text.literal("Scatha Tracker: " + (GhostKillTrackerClient.scathaEnabled ? "§aON" : "§cOFF")));
            }).dimensions(centerX - 100, centerY - 20, 200, 20).build());

        // Move HUD Button
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Edit GUI Position"),
            button -> {
                if (this.client != null) {
                    this.client.setScreen(new GhostTrackerEditScreen());
                }
            }).dimensions(centerX - 100, centerY + 5, 200, 20).build());
            
        // Close Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> this.close())
            .dimensions(centerX - 100, centerY + 30, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, "§6§lSBP Tracker Settings", this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
    }
}
