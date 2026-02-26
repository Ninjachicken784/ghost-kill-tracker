package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.text.DecimalFormat;

public class GhostKillHud {

    private static final int BG_COLOR       = 0xB0000000; // semi-transparent black
    private static final int BORDER_COLOR   = 0xFFFFFFFF; // white border
    private static final int TITLE_COLOR    = 0xFFFFFFFF; // white
    private static final int VALUE_COLOR    = 0xFFFFFF55; // yellow
    private static final int LABEL_COLOR    = 0xFFAAAAAA; // gray

    private static final DecimalFormat DF_0 = new DecimalFormat("#,##0");
    private static final DecimalFormat DF_1 = new DecimalFormat("#,##0.0");

    // HUD position (top-right area, matches image)
    private static final int HUD_X = 10;
    private static final int HUD_Y = 10;
    private static final int HUD_WIDTH  = 140;
    private static final int HUD_HEIGHT = 82;
    private static final int PADDING = 5;

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;

        KillSession s = GhostKillTrackerClient.SESSION;

        int x = client.getWindow().getScaledWidth() - HUD_WIDTH - 5;
        int y = HUD_Y;

        // Background
        ctx.fill(x, y, x + HUD_WIDTH, y + HUD_HEIGHT, BG_COLOR);

        // Border
        drawBorder(ctx, x, y, HUD_WIDTH, HUD_HEIGHT, BORDER_COLOR);

        // Title row
        ctx.drawTextWithShadow(client.textRenderer,
                Text.literal("§f§lGhost Kill Tracker"),
                x + PADDING, y + PADDING, TITLE_COLOR);

        int lineH = 12;
        int startY = y + PADDING + lineH + 3;

        // T/k  (total kills)
        renderRow(ctx, client, x + PADDING, startY,
                "T/k", DF_0.format(s.getTotalKills()));

        // K/h  (kills per hour)
        renderRow(ctx, client, x + PADDING, startY + lineH,
                "K/h", DF_1.format(s.getKillsPerHour()));

        // C/k  (coins per kill)
        renderRow(ctx, client, x + PADDING, startY + lineH * 2,
                "C/k", DF_1.format(s.getAvgCoinsPerKill()));

        // Session total coins (bonus row)
        renderRow(ctx, client, x + PADDING, startY + lineH * 3,
                "Coins", DF_0.format(s.getTotalCoins()));
    }

    private static void renderRow(DrawContext ctx, MinecraftClient client,
                                  int x, int y, String label, String value) {
        ctx.drawTextWithShadow(client.textRenderer,
                Text.literal("§7" + label + ":"), x, y, LABEL_COLOR);
        ctx.drawTextWithShadow(client.textRenderer,
                Text.literal("§e" + value), x + 36, y, VALUE_COLOR);
    }

    private static void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x,         y,         x + w, y + 1,     color); // top
        ctx.fill(x,         y + h - 1, x + w, y + h,     color); // bottom
        ctx.fill(x,         y,         x + 1, y + h,     color); // left
        ctx.fill(x + w - 1, y,         x + w, y + h,     color); // right
    }
}
