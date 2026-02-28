package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class DropNotification {
    private static String line1 = "";
    private static String line2 = "";
    private static long showUntil = 0;
    private static final int DURATION_MS = 4000;

    public static void show(String l1, String l2) {
        line1 = l1;
        line2 = l2;
        showUntil = System.currentTimeMillis() + DURATION_MS;
    }

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (System.currentTimeMillis() > showUntil) return;

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        int w = 160;
        int h = 30;
        int x = (screenW - w) / 2;
        int y = screenH / 3;

        // Background
        ctx.fill(x, y, x + w, y + h, 0xCC000000);
        ctx.fill(x,         y,         x + w, y + 1,     0xFFFFD700);
        ctx.fill(x,         y + h - 1, x + w, y + h,     0xFFFFD700);
        ctx.fill(x,         y,         x + 1, y + h,     0xFFFFD700);
        ctx.fill(x + w - 1, y,         x + w, y + h,     0xFFFFD700);

        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal(line1), screenW / 2, y + 6, 0xFFFFFFFF);
        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal(line2), screenW / 2, y + 17, 0xFFFFFFFF);
    }
}
