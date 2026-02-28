package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DropNotification {
    private static Text message = null;
    private static long showUntil = 0;

    public static void show(Text msg) {
        message = msg;
        showUntil = System.currentTimeMillis() + 2000;
    }

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (message == null || System.currentTimeMillis() > showUntil) return;
        int cx = client.getWindow().getScaledWidth() / 2;
        int cy = client.getWindow().getScaledHeight() / 3;
        int textW = client.textRenderer.getWidth(message);
        ctx.drawTextWithShadow(client.textRenderer, message, cx - textW / 2, cy, 0xFFFFFFFF);
    }
}
