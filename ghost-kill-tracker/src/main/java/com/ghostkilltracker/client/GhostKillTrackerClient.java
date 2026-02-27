package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final String MOD_ID = "ghostkilltracker";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final KillSession SESSION = new KillSession();
    public static boolean hudVisible = true;

    private static final KeyBinding.Category CATEGORY =
        KeyBinding.Category.create(Identifier.of("ghostkilltracker", "main"));

    private static final KeyBinding startKey = new KeyBinding(
        "key.ghostkilltracker.start",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_N,
        CATEGORY
    );
    private static final KeyBinding pauseKey = new KeyBinding(
        "key.ghostkilltracker.pause",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_M,
        CATEGORY
    );
    private static final KeyBinding resetKey = new KeyBinding(
        "key.ghostkilltracker.reset",
        InputUtil.Type.KEYSYM,
        GLFW.GLFW_KEY_R,
        CATEGORY
    );

    @Override
    public void onInitializeClient() {
        LOGGER.info("Ghost Kill Tracker initialized!");

        KeyBindingHelper.registerKeyBinding(startKey);
        KeyBindingHelper.registerKeyBinding(pauseKey);
        KeyBindingHelper.registerKeyBinding(resetKey);

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            while (startKey.wasPressed()) {
                SESSION.start();
                client.player.sendMessage(Text.literal("§aTracker STARTED!"), true);
            }
            while (pauseKey.wasPressed()) {
                SESSION.pause();
                client.player.sendMessage(Text.literal("§eTracker PAUSED!"), true);
            }
            while (resetKey.wasPressed()) {
                SESSION.resetSession();
                client.player.sendMessage(Text.literal("§cSession RESET!"), true);
            }
        });
    }
}
