package com.ghostkilltracker.client;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class GhostTrackerCommand {
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                ClientCommandManager.literal("ghosttracker")
                    .executes(ctx -> {
                        GhostKillTrackerClient.hudVisible = !GhostKillTrackerClient.hudVisible;
                        String state = GhostKillTrackerClient.hudVisible ? "§aON" : "§cOFF";
                        ctx.getSource().sendFeedback(Text.literal("Ghost Tracker HUD: " + state));
                        return 1;
                    })
            );
        });
    }
}
