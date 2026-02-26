package com.ghostkilltracker.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class GhostKillKeybinds {

    public static KeyBinding toggleHudKey;
    public static KeyBinding resetSessionKey;

    public static void register() {
        toggleHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostkilltracker.toggle_hud",
                GLFW.GLFW_KEY_H,
                "key.categories.misc"
        ));

        resetSessionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ghostkilltracker.reset_session",
                GLFW.GLFW_KEY_J,
                "key.categories.misc"
        ));
    }

    public static void handleKeys(MinecraftClient client) {
        while (toggleHudKey.wasPressed()) {
            GhostKillTrackerClient.hudVisible = !GhostKillTrackerClient.hudVisible;
            if (client.player != null) {
                client.player.sendMessage(
                        Text.literal("Ghost Kill Tracker HUD: " +
                                (GhostKillTrackerClient.hudVisible ? "§aON" : "§cOFF")), true);
            }
        }

        while (resetSessionKey.wasPressed()) {
            GhostKillTrackerClient.SESSION.reset();
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§aGhost kill session reset!"), true);
            }
        }
    }
}
