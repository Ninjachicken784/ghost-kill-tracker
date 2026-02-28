package com.ghostkilltracker.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import java.text.DecimalFormat;

public class GhostKillHud {
    private static final int BG_COLOR     = 0xCC1A1A1A;
    private static final int BORDER_COLOR = 0xFF555555;
    private static final int TITLE_COLOR  = 0xFFFFFFFF;
    private static final int LABEL_COLOR  = 0xFFCCCCCC;
    private static final int VALUE_COLOR  = 0xFFFFFF55;
    private static final DecimalFormat DF0 = new DecimalFormat("#,##0");
    private static final DecimalFormat DF1 = new DecimalFormat("#,##0.0");
    private static final int BOX_W  = 160;
    private static final int BOX_H  = 84;
    private static final int PAD    = 6;
    private static final int LINE_H = 11;
    private static final int GAP    = 6;

    public static void render(DrawContext ctx, MinecraftClient client) {
        if (client.player == null) return;
        KillSession s = GhostKillTrackerClient.SESSION;
        int x = client.getWindow().getScaledWidth() - BOX_W - 5;
        int y = 5;

        String status = !s.isRunning() ? "§7[STOPPED]" : s.isPaused() ? "§c[PAUSED]" : "§a[RUNNING]";
        ctx.drawTextWithShadow(client.textRenderer, Text.literal(status), x, y, 0xFFFFFFFF);
        y += 12;

        // Total box
        drawBox(ctx, x, y, BOX_W, BOX_H);
        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§fTotal"), x + BOX_W / 2, y + PAD, TITLE_COLOR);
        int ly = y + PAD + LINE_H + 2;
        drawRow(ctx, client, x, ly,              "Kills",    DF0.format(s.getTotalKills()));
        drawRow(ctx, client, x, ly + LINE_H,     "Kills/h",  DF1.format(s.getTotalKillsPerHour()));
        drawRow(ctx, client, x, ly + LINE_H * 2, "Sorrow",   DF0.format(s.getTotalSorrow()));
        drawRow(ctx, client, x, ly + LINE_H * 3, "Plasma",   DF0.format(s.getTotalPlasma()));
        drawRow(ctx, client, x, ly + LINE_H * 4, "Uptime",   s.getTotalUptime());
        y += BOX_H + GAP;

        // Session box
        drawBox(ctx, x, y, BOX_W, BOX_H);
        ctx.drawCenteredTextWithShadow(client.textRenderer, Text.literal("§fSession"), x + BOX_W / 2, y + PAD, TITLE_COLOR);
        ly = y + PAD + LINE_H + 2;
        drawRow(ctx, client, x, ly,              "Kills",    DF0.format(s.getSessionKills()));
        drawRow(ctx, client, x, ly + LINE_H,     "Kills/h",  DF1.format(s.getSessionKillsPerHour()));
        drawRow(ctx, client, x, ly + LINE_H * 2, "Sorrow",   DF0.format(s.getSessionSorrow()));
        drawRow(ctx, client, x, ly + LINE_H * 3, "Plasma",   DF0.format(s.getSessionPlasma()));
        drawRow(ctx, client, x, ly + LINE_H * 4, "Uptime",   s.getSessionUptime());
        y += BOX_H + GAP;

        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§7[N] Start  [M] Pause  [R] Reset"), x, y, 0xFFAAAAAA);
    }

    private static void drawRow(DrawContext ctx, MinecraftClient client, int boxX, int y, String label, String value) {
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§7" + label), boxX + PAD, y, LABEL_COLOR);
        ctx.drawTextWithShadow(client.textRenderer, Text.literal("§e" + value), boxX + 100, y, VALUE_COLOR);
    }

    private static void drawBox(DrawContext ctx, int x, int y, int w, int h) {
        ctx.fill(x, y, x + w, y + h, BG_COLOR);
        ctx.fill(x,         y,         x + w, y + 1,     BORDER_COLOR);
        ctx.fill(x,         y + h - 1, x + w, y + h,     BORDER_COLOR);
        ctx.fill(x,         y,         x + 1, y + h,     BORDER_COLOR);
        ctx.fill(x + w - 1, y,         x + w, y + h,     BORDER_COLOR);
    }
}
