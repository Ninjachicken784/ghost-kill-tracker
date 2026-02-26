package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "ghostkilltracker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KillSession SESSION = new KillSession();
    public static boolean hudVisible = true;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) {
                GhostKillHud.render(drawContext, MinecraftClient.getInstance());
            }
        });
    }
}
