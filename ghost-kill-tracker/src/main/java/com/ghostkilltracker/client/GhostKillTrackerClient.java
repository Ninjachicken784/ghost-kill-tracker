package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
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
    private boolean nWasDown = false;
    private boolean mWasDown = false;
    private boolean rWasDown = false;

    private int lastBestiaryKills = -1;
    private static final Pattern KILLS_PATTERN = Pattern.compile("Kills:\\s*([\\d,]+)");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Read bestiary kills when GUI is open
            if (client.currentScreen instanceof GenericContainerScreen screen) {
                GenericContainerScreenHandler handler = screen.getScreenHandler();
                for (int i = 0; i < handler.getRows() * 9; i++) {
                    ItemStack stack = handler.getSlot(i).getStack();
                    if (stack.isEmpty()) continue;
                    String name = stack.getName().getString();
                    if (name.contains("Ghost")) {
                        var lore = stack.get(net.minecraft.component.DataComponentTypes.LORE);
                        if (lore != null) {
                            for (Text line : lore.lines()) {
                                Matcher m = KILLS_PATTERN.matcher(line.getString());
                                if (m.find()) {
                                    try {
                                        int kills = Integer.parseInt(m.group(1).replace(",", ""));
                                        if (lastBestiaryKills >= 0 && kills > lastBestiaryKills) {
                                            int diff = kills - lastBestiaryKills;
                                            for (int k = 0; k < diff; k++) SESSION.addKill();
                                        }
                                        lastBestiaryKills = kills;
                                    } catch (NumberFormatException ignored) {}
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            boolean nDown = InputUtil.isKeyPressed(client.getWindow(), 78);
            boolean mDown = InputUtil.isKeyPressed(client.getWindow(), 77);
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82);

            if (nDown && !nWasDown) { SESSION.start(); lastBestiaryKills = -1; client.player.sendMessage(Text.literal("§aTracker STARTED!"), true); }
            if (mDown && !mWasDown) { SESSION.pause(); client.player.sendMessage(Text.literal("§eTracker PAUSED!"), true); }
            if (rDown && !rWasDown) { SESSION.resetSession(); lastBestiaryKills = -1; client.player.sendMessage(Text.literal("§cSession RESET!"), true); }

            nWasDown = nDown;
            mWasDown = mDown;
            rWasDown = rDown;
        });
    }
}
