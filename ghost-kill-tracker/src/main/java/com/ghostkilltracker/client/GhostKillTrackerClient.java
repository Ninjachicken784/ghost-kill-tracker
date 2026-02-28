package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "ghostkilltracker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KillSession SESSION = new KillSession();
    public static boolean hudVisible = true;
    public static int hudX = -1;
    public static int hudY = 5;

    private boolean nWasDown = false;
    private boolean mWasDown = false;
    private boolean rWasDown = false;
    private int lastGauntletKills = -1;
    private int tickCounter = 0;
    private static final Pattern KILLS_PATTERN = Pattern.compile("Kills:\\s*([\\d,]+)");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");

        // /ghosttracker to toggle HUD
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("ghosttracker")
                .executes(ctx -> {
                    hudVisible = !hudVisible;
                    ctx.getSource().sendFeedback(Text.literal("Ghost Tracker HUD: " + (hudVisible ? "§aON" : "§cOFF")));
                    return 1;
                })
            );
            // /editGhostTracker to open drag screen
            dispatcher.register(ClientCommandManager.literal("editGhostTracker")
                .executes(ctx -> {
                    MinecraftClient.getInstance().send(() ->
                        MinecraftClient.getInstance().setScreen(new GhostTrackerEditScreen()));
                    return 1;
                })
            );
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            // Set default X position if not set
            if (hudX == -1) hudX = client.getWindow().getScaledWidth() - 165;
            if (hudVisible) GhostKillHud.render(drawContext, client);
            DropNotification.render(drawContext, client);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;

            tickCounter++;
            if (tickCounter >= 20) {
                tickCounter = 0;
                ItemStack held = client.player.getMainHandStack();
                if (!held.isEmpty()) {
                    var lore = held.get(DataComponentTypes.LORE);
                    if (lore != null) {
                        for (Text line : lore.lines()) {
                            Matcher m = KILLS_PATTERN.matcher(line.getString());
                            if (m.find()) {
                                try {
                                    int kills = Integer.parseInt(m.group(1).replace(",", ""));
                                    if (lastGauntletKills >= 0 && kills > lastGauntletKills) {
                                        int diff = kills - lastGauntletKills;
                                        if (diff <= 20) {
                                            for (int i = 0; i < diff; i++) SESSION.addKill();
                                        }
                                    }
                                    lastGauntletKills = kills;
                                } catch (NumberFormatException ignored) {}
                                break;
                            }
                        }
                    }
                }
            }

            boolean nDown = InputUtil.isKeyPressed(client.getWindow(), 78);
            boolean mDown = InputUtil.isKeyPressed(client.getWindow(), 77);
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82);

            if (nDown && !nWasDown) { SESSION.start(); lastGauntletKills = -1; client.player.sendMessage(Text.literal("§aTracker STARTED!"), true); }
            if (mDown && !mWasDown) { SESSION.pause(); client.player.sendMessage(Text.literal("§eTracker PAUSED!"), true); }
            if (rDown && !rWasDown) { SESSION.resetSession(); lastGauntletKills = -1; client.player.sendMessage(Text.literal("§cSession RESET!"), true); }

            nWasDown = nDown;
            mWasDown = mDown;
            rWasDown = rDown;
        });
    }
}
