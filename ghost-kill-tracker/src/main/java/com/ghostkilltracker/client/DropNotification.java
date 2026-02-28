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
        int cx = screenW / 2;
        int cy = screenH / 3;

        float scale = 2.5f;
        var matrices = ctx.getMatrices();
        matrices.push();
        matrices.translate(cx, cy, 0);
        matrices.scale(scale, scale, 1.0f);

        Text text = Text.literal(message);
        int textW = client.textRenderer.getWidth(text);

        // Draw thick/bold by rendering multiple times with offsets
        for (int ox = -1; ox <= 1; ox++) {
            for (int oy = -1; oy <= 1; oy++) {
                ctx.drawTextWithShadow(client.textRenderer, text, -textW / 2 + ox, -4 + oy, 0xFF000000);
            }
        }
        ctx.drawTextWithShadow(client.textRenderer, text, -textW / 2, -4, 0xFFFFFFFF);

        matrices.pop();
    }
}
