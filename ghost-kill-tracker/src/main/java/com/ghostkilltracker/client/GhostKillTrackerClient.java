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
    
    // Toggles and persistence
    public static boolean hudVisible = true;
    public static boolean dropsEnabled = true;
    public static boolean ghostEnabled = true;
    public static boolean scathaEnabled = true;
    
    public static int hudX = 10;
    public static int hudY = 10;

    // Worm Stats
    public static int wormCount = 0;
    public static double wormRate = 0.0;
    private static final String WORM_MSG = "You hear the sound of something approaching...";

    private boolean nWasDown = false;
    private boolean rWasDown = false;
    private int lastGauntletKills = -1;
    private static final Pattern KILLS_PATTERN = Pattern.compile("Kills:\\s*([\\d,]+)");

    @Override
    public void onInitializeClient() {
        // Chat listener for Worms
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            if (scathaEnabled && message.getString().contains(WORM_MSG)) {
                wormCount++;
                long elapsed = SESSION.getElapsedTime(); 
                wormRate = (wormCount / (Math.max(1, elapsed) / 3600000.0));
            }
        });

        // Register /sbp command
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("sbp")
                .executes(ctx -> {
                    MinecraftClient.getInstance().send(() -> 
                        MinecraftClient.getInstance().setScreen(new SbpMenuScreen()));
                    return 1;
                })
            );
        });

        // Render Loop
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (hudVisible) {
                GhostKillHud.render(drawContext, MinecraftClient.getInstance());
            }
        });

        // Logic Tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.currentScreen != null) return;
            
            // Ghost Tracking logic
            if (ghostEnabled && client.world.getTime() % 20 == 0) {
                ItemStack held = client.player.getMainHandStack();
                if (!held.isEmpty() && held.contains(DataComponentTypes.LORE)) {
                    for (Text line : held.get(DataComponentTypes.LORE).lines()) {
                        Matcher m = KILLS_PATTERN.matcher(line.getString());
                        if (m.find()) {
                            int kills = Integer.parseInt(m.group(1).replace(",", ""));
                            if (lastGauntletKills >= 0 && kills > lastGauntletKills) {
                                int diff = kills - lastGauntletKills;
                                if (diff <= 20) {
                                    for (int i = 0; i < diff; i++) SESSION.addKill();
                                }
                            }
                            lastGauntletKills = kills;
                            break;
                        }
                    }
                }
            }

            // Controls
            boolean nDown = InputUtil.isKeyPressed(client.getWindow(), 78); // N
            boolean rDown = InputUtil.isKeyPressed(client.getWindow(), 82); // R

            if (nDown && !nWasDown) {
                SESSION.start();
                client.player.sendMessage(Text.literal("§aTracker Started!"), true);
            }
            if (rDown && !rWasDown) {
                SESSION.resetSession();
                wormCount = 0;
                wormRate = 0;
                client.player.sendMessage(Text.literal("§cSession Reset!"), true);
            }

            nWasDown = nDown;
            rWasDown = rDown;
        });
    }
}
