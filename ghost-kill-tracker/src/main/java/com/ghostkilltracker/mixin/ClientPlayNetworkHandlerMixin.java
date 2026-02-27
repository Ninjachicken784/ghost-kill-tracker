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

    // +296 Combat (1,177,953,599/0)
    private static final Pattern COMBAT_XP = Pattern.compile(
        "\\+[\\d,]+ Combat \\(", Pattern.CASE_INSENSITIVE);

    // RARE DROP! Sorrow (+403 ✦ Magic Find)
    private static final Pattern SORROW = Pattern.compile(
        "RARE DROP!.*?Sorrow", Pattern.CASE_INSENSITIVE);

    // RARE DROP! Plasma (+403 ✦ Magic Find)
    private static final Pattern PLASMA = Pattern.compile(
        "RARE DROP!.*?Plasma", Pattern.CASE_INSENSITIVE);

    // Purse: 201,258 (+1,004)
    private static final Pattern PURSE = Pattern.compile(
        "Purse:.*?\\+([\\d,]+)\\)", Pattern.CASE_INSENSITIVE);

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChat(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text msg = packet.content();
        if (msg == null) return;
        String raw = msg.getString();

        if (COMBAT_XP.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addKill();
            return;
        }
        if (SORROW.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addSorrow();
            return;
        }
        if (PLASMA.matcher(raw).find()) {
            GhostKillTrackerClient.SESSION.addPlasma();
            return;
        }
        Matcher purse = PURSE.matcher(raw);
        if (purse.find()) {
            try {
                int amount = Integer.parseInt(purse.group(1).replace(",", ""));
                GhostKillTrackerClient.SESSION.addScav(amount);
            } catch (NumberFormatException ignored) {}
        }
    }
}
