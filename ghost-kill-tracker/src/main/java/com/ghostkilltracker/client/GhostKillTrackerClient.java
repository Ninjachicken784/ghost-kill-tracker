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

            boolean nDown = InputUtil.isKeyPressed(client.getWindow(), 78);
            boolean mDown = InputUtil.isKeyPressed(client.getWindow(), 77);
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82);

            if (nDown && !nWasDown) {
                SESSION.start();
                client.player.sendMessage(Text.literal("§aTracker STARTED!"), true);
            }
            if (mDown && !mWasDown) {
                SESSION.pause();
                client.player.sendMessage(Text.literal("§eTracker PAUSED!"), true);
            }
            if (rDown && !rWasDown) {
                SESSION.resetSession();
                client.player.sendMessage(Text.literal("§cSession RESET!"), true);
            }
            nWasDown = nDown;
            mWasDown = mDown;
            rWasDown = rDown;
        });
    }
}
