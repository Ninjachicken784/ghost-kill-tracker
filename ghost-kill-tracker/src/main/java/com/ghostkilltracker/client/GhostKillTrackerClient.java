package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "ghostkilltracker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KillSession SESSION = new KillSession();
    public static boolean hudVisible = true;

    private static KeyBinding startKey;
    private static KeyBinding pauseKey;
    private static KeyBinding resetKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");

        startKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostkilltracker.start",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.categories.misc"
        ));
        pauseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostkilltracker.pause",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "key.categories.misc"
        ));
        resetKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostkilltracker.reset",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.misc"
        ));

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (startKey.wasPressed()) {
                SESSION.start();
                client.player.sendMessage(Text.literal("§aGhost Kill Tracker STARTED!"), true);
            }
            while (pauseKey.wasPressed()) {
                SESSION.pause();
                client.player.sendMessage(Text.literal("§eGhost Kill Tracker PAUSED!"), true);
            }
            while (resetKey.wasPressed()) {
                SESSION.resetSession();
                client.player.sendMessage(Text.literal("§cSession RESET!"), true);
            }
        });
    }
}
