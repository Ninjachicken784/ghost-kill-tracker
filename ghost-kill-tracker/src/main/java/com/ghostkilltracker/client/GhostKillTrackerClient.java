package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class GhostKillTrackerClient implements ClientModInitializer {
    // Session and Data
    public static final KillSession SESSION = new KillSession();
    public static int wormCount = 0;
    public static double wormRate = 0.0;
    
    // Menu & Visibility Toggles
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    public static boolean dropsEnabled = true;
    public static boolean hudVisible = true;
    public static int hudX = 10;
    public static int hudY = 10;

    private boolean rWasDown = false;

    @Override
    public void onInitializeClient() {
        
        // 1. THE BULLETPROOF TRACKER
        // This listens for the message regardless of dots, parentheses, or colors.
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String msg = message.getString().toLowerCase();
            
            if (msg.contains("you hear the sound of something approaching")) {
                wormCount++;
                updateWormRate();
                
                // Optional: Sends a message ONLY you can see to confirm it worked
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.sendMessage(
                        Text.literal("§a[Tracker] Worm Detected! Total: " + wormCount), true);
                }
            }
        });

        // 2. THE RESET KEY (Press R to clear stats)
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82); // 82 is Key R
            if (rDown && !rWasDown) {
                SESSION.resetSession();
                wormCount = 0;
                wormRate = 0;
                client.player.sendMessage(Text.literal("§cTrackers Reset!"), true);
            }
            rWasDown = rDown;
        });

        // 3. THE MENU COMMAND (/sbp)
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("sbp")
                .executes(ctx -> {
                    MinecraftClient.getInstance().send(() -> 
                        MinecraftClient.getInstance().setScreen(new SbpMenuScreen()));
                    return 1;
                })
            );
        });

        // 4. THE HUD HOOK
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) {
                GhostKillHud.render(drawContext, MinecraftClient.getInstance());
            }
        });
    }

    public static void updateWormRate() {
        long elapsed = SESSION.getElapsedTime();
        // Calculate Worms Per Hour: (Count / Hours)
        // 3600000ms = 1 hour
        wormRate = (wormCount / (Math.max(1, elapsed) / 3600000.0));
    }
}
