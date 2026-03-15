package com.ghostkilltracker.mixin;

import com.ghostkilltracker.client.GhostKillTrackerClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    private long lastWormTime = 0;

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChat(GameMessageS2CPacket packet, CallbackInfo ci) {
        if (packet.content() == null) return;
        String raw = packet.content().getString();

        if (raw.contains("You hear the sound of something approaching...")) {
            long now = System.currentTimeMillis();
            if (now - lastWormTime > 1000) {
                lastWormTime = now;
                if (GhostKillTrackerClient.SESSION != null) {
                    GhostKillTrackerClient.SESSION.addWorm();
                }
            }
        }
    }
}
