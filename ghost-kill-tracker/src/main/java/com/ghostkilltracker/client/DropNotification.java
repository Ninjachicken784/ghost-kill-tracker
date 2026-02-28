package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DropNotification {
    private static String message = "";
    private static long showUntil = 0;
    private static final int DURATION_MS = 4000;

    public static void show(String msg) {
        message = msg;
        showUntil = System.currentTimeMillis() + DURATION_MS;
    }

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (System.currentTimeMillis() > showUntil || message.isEmpty()) return;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();

        Text text = Text.literal(message);
        int textW = client.textRenderer.getWidth(text);
        int cx = (screenW - textW) / 2;
        int cy = screenH / 3;

        // Draw 9 times offset to fake bold/thick
        for (int ox = -1; ox <= 1; ox++) {
            for (int oy = -1; oy <= 1; oy++) {
                ctx.drawTextWithShadow(client.textRenderer, text, cx + ox, cy + oy, 0xFF000000);
            }
        }
        // Draw main text on top
        ctx.drawTextWithShadow(client.textRenderer, text, cx, cy, 0xFFFFFFFF);
    }
}
