package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DropNotification {
    private static Text message = null;
    private static long showUntil = 0;
    private static final int DURATION_MS = 4000;

    public static void show(Text msg) {
        message = msg;
        showUntil = System.currentTimeMillis() + DURATION_MS;
    }

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (System.currentTimeMillis() > showUntil || message == null) return;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        int textW = client.textRenderer.getWidth(message);
        int cx = (screenW - textW) / 2;
        int cy = screenH / 3;

        // Draw offset copies to make it appear thicker/bigger
        ctx.drawTextWithShadow(client.textRenderer, message, cx - 1, cy - 1, 0xFFFFFFFF);
        ctx.drawTextWithShadow(client.textRenderer, message, cx + 1, cy - 1, 0xFFFFFFFF);
        ctx.drawTextWithShadow(client.textRenderer, message, cx - 1, cy + 1, 0xFFFFFFFF);
        ctx.drawTextWithShadow(client.textRenderer, message, cx + 1, cy + 1, 0xFFFFFFFF);
        ctx.drawTextWithShadow(client.textRenderer, message, cx,     cy,     0xFFFFFFFF);
    }
}
