package com.ghostkilltracker.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GhostKillTrackerClient implements ClientModInitializer {
    public static final KillSession SESSION = new KillSession();
    
    // Independent Toggles
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    
    public static int hudX = 10;
    public static int hudY = 10;

    public static int wormCount = 0;
    public static double wormRate = 0.0;
    private static final String WORM_MSG = "You hear the sound of something approaching...";

    private boolean nWasDown = false, mWasDown = false, rWasDown = false;
    private int lastGauntletKills = -1;
    private static final Pattern KILLS_PATTERN = Pattern.compile("Kills:\\s*([\\d,]+)");

    @Override
    public void onInitializeClient() {
        // FIXED CHAT LISTENER
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (scathaEnabled && message.getString().contains(WORM_MSG)) {
                wormCount++;
                long elapsed = SESSION.getElapsedTime(); 
                wormRate = (wormCount / (Math.max(1, elapsed) / 3600000.0));
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // NEW COMMAND /sbp to open the GUI
            dispatcher.register(ClientCommandManager.literal("sbp")
                .executes(ctx -> {
                    MinecraftClient.getInstance().send(() -> 
                        MinecraftClient.getInstance().setScreen(new SbpMenuScreen()));
                    return 1;
                })
            );
        });

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            GhostKillHud.render(drawContext, MinecraftClient.getInstance());
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;
            
            // GHOST LOGIC (Only runs if ghostEnabled is ON)
            if (ghostEnabled && client.player.getWorld().getTime() % 20 == 0) {
                ItemStack held = client.player.getMainHandStack();
                if (!held.isEmpty() && held.contains(DataComponentTypes.LORE)) {
                    for (Text line : held.get(DataComponentTypes.LORE).lines()) {
                        Matcher m = KILLS_PATTERN.matcher(line.getString());
                        if (m.find()) {
                            int kills = Integer.parseInt(m.group(1).replace(",", ""));
                            if (lastGauntletKills >= 0 && kills > lastGauntletKills) {
                                int diff = kills - lastGauntletKills;
                                if (diff <= 20) for (int i = 0; i < diff; i++) SESSION.addKill();
                            }
                            lastGauntletKills = kills;
                            break;
                        }
                    }
                }
            }

            // Keybinds
            boolean nDown = InputUtil.isKeyPressed(client.getWindow(), 78);
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82);
            if (nDown && !nWasDown) { SESSION.start(); client.player.sendMessage(Text.literal("§aTracker Started!"), true); }
            if (rDown && !rWasDown) { SESSION.resetSession(); wormCount = 0; wormRate = 0; client.player.sendMessage(Text.literal("§cReset!"), true); }
            nWasDown = nDown; rWasDown = rDown;
        });
    }
}
