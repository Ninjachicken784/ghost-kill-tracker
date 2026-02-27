package com.ghostkilltracker.mixin;

import com.ghostkilltracker.client.GhostKillTrackerClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    private static final Pattern COMBAT_XP = Pattern.compile(
            "\\+[\\d,]+(?:\\.\\d+)?\\s+Combat\\s+XP", Pattern.CASE_INSENSITIVE);
    private static final Pattern SORROW_DROP = Pattern.compile(
            "Rare Drop.*?Sorrow", Pattern.CASE_INSENSITIVE);
    private static final Pattern PLASMA_DROP = Pattern.compile(
            "Rare Drop.*?Plasma", Pattern.CASE_INSENSITIVE);
    private static final Pattern SCAV_COINS = Pattern.compile(
            "Scav\\s+Coins?[:\\s]+([\\d,]+)", Pattern.CASE_INSENSITIVE);

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text msg = packet.content();
        if (msg == null) return;
        String raw = msg.getString();

        if (COMBAT_XP.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addKill();
            return;
        }
        if (SORROW_DROP.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addSorrow();
            return;
        }
        if (PLASMA_DROP.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addPlasma();
            return;
        }
        Matcher scav = SCAV_COINS.matcher(raw);
        if (scav.find()) {
            try {
                int amount = Integer.parseInt(scav.group(1).replace(",", ""));
                GhostKillTrackerClient.SESSION.addScav(amount);
            } catch (NumberFormatException ignored) {}
        }
    }
}
