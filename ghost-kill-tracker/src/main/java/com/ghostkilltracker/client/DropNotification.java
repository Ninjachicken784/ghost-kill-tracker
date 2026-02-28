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
        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal(message), screenW / 2, screenH / 3, 0xFFFFFFFF);
    }
}
