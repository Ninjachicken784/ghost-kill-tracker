package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "ghostkilltracker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KillSession SESSION = new KillSession();
    public static boolean hudVisible = true;
    private boolean nWasDown = false;
    private boolean mWasDown = false;
    private boolean rWasDown = false;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;
            long handle = client.getWindow().getHandle();

            if (InputUtil.isKeyPressed(handle, 78)) {
                if (!nWasDown) { nWasDown = true; SESSION.start(); client.player.sendMessage(Text.literal("§aTracker STARTED!"), true); }
            } else { nWasDown = false; }

            if (InputUtil.isKeyPressed(handle, 77)) {
                if (!mWasDown) { mWasDown = true; SESSION.pause(); client.player.sendMessage(Text.literal("§eTracker PAUSED!"), true); }
            } else { mWasDown = false; }

            if (InputUtil.isKeyPressed(handle, 82)) {
                if (!rWasDown) { rWasDown = true; SESSION.resetSession(); client.player.sendMessage(Text.literal("§cSession RESET!"), true); }
            } else { rWasDown = false; }
        });
    }
}
