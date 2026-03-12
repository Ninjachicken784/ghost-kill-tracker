package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final KillSession SESSION = new KillSession();
    
    // Toggles - added dropsEnabled to fix the build error
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    public static boolean dropsEnabled = true; 
    public static boolean hudVisible = true;
    
    // Position
    public static int hudX = 10;
    public static int hudY = 10;

    // Worm Stats
    public static int wormCount = 0;
    public static double wormRate = 0.0;
    private boolean rWasDown = false;

    @Override
    public void onInitializeClient() {
        // 1. WORM TRACKING (Chat Listener)
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String msg = message.getString();
            if (scathaEnabled && msg.contains("You hear the sound of something approaching...")) {
                wormCount++;
                updateWormRate();
            }
        });

        // 2. RESET LOGIC (Hitting 'R' resets both)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82); // Key R
            if (rDown && !rWasDown) {
                SESSION.resetSession();
                wormCount = 0;
                wormRate = 0;
                client.player.sendMessage(Text.literal("§cTrackers Reset!"), true);
            }
            rWasDown = rDown;
        });

        // 3. MENU COMMAND (/sbp)
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("sbp")
                .executes(ctx -> {
                    MinecraftClient.getInstance().send(() -> 
                        MinecraftClient.getInstance().setScreen(new SbpMenuScreen()));
                    return 1;
                })
            );
        });

        // 4. HUD RENDERER
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) {
                GhostKillHud.render(drawContext, MinecraftClient.getInstance());
            }
        });
    }

    public static void updateWormRate() {
        long elapsed = SESSION.getElapsedTime();
        // Calculate Worms Per Hour
        wormRate = (wormCount / (Math.max(1, elapsed) / 3600000.0));
    }
}
