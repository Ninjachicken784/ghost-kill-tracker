package com.ghostkilltracker.mixin;

import com.ghostkilltracker.client.DropNotification;
import com.ghostkilltracker.client.GhostKillTrackerClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    private static final Pattern SORROW = Pattern.compile("RARE DROP!.*?Sorrow", Pattern.CASE_INSENSITIVE);
    private static final Pattern PLASMA = Pattern.compile("RARE DROP!.*?Plasma", Pattern.CASE_INSENSITIVE);
    private static final String WORM_MSG = "You hear the sound of something approaching...";

    private long lastSorrowTime = 0;
    private long lastPlasmaTime = 0;
    private long lastWormTime = 0;

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChat(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text msg = packet.content();
        if (msg == null) return;
        String raw = msg.getString();

        // 1. WORM TRACKER LOGIC
        if (raw.contains(WORM_MSG)) {
            long now = System.currentTimeMillis();
            if (now - lastWormTime > 1000) { // Anti-spam
                lastWormTime = now;
                if (GhostKillTrackerClient.SESSION != null) {
                    GhostKillTrackerClient.SESSION.addWorm();
                }
            }
        }

        // 2. GHOST DROPS (Existing logic)
        if (SORROW.matcher(raw).find()) {
            long now = System.currentTimeMillis();
            if (now - lastSorrowTime > 1000) {
                lastSorrowTime = now;
                if (GhostKillTrackerClient.SESSION != null) GhostKillTrackerClient.SESSION.addSorrow();
                if (GhostKillTrackerClient.dropsEnabled) DropNotification.show(msg);
            }
        }

        if (PLASMA.matcher(raw).find()) {
            long now = System.currentTimeMillis();
            if (now - lastPlasmaTime > 1000) {
                lastPlasmaTime = now;
                if (GhostKillTrackerClient.SESSION != null) GhostKillTrackerClient.SESSION.addPlasma();
                if (GhostKillTrackerClient.dropsEnabled) DropNotification.show(msg);
            }
        }
    }
}
