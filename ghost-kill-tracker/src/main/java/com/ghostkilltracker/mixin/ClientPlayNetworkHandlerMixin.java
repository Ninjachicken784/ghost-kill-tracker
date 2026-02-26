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

    // Matches Hypixel Skyblock ghost kill message: "Ghost  +X coins"
    // Adjust these patterns to match the exact server messages you see
    private static final Pattern GHOST_KILL_PATTERN = Pattern.compile(
            "(?i)(?:ghost|phantom).*?kill|you slew a ghost",
            Pattern.CASE_INSENSITIVE
    );

    // Matches coin drops like "+123 Coins" or "Ghost  +500"
    private static final Pattern COIN_PATTERN = Pattern.compile(
            "\\+([\\d,]+)\\s*(?:coins?|coin)",
            Pattern.CASE_INSENSITIVE
    );

    // For Hypixel Skyblock specifically - ghost kill + sorrow/plasma drop line
    // "  Ghost Kill  +X Coins"
    private static final Pattern HYPIXEL_GHOST_KILL = Pattern.compile(
            "Ghost\\s+Kill.*?\\+(\\d[\\d,]*)\\s+Coins?",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        Text msg = packet.content();
        if (msg == null) return;

        String raw = msg.getString();

        // Try Hypixel-style "Ghost Kill  +500 Coins" in one line
        Matcher hm = HYPIXEL_GHOST_KILL.matcher(raw);
        if (hm.find()) {
            String coinsStr = hm.group(1).replace(",", "");
            long coins = 0;
            try { coins = Long.parseLong(coinsStr); } catch (NumberFormatException ignored) {}
            GhostKillTrackerClient.SESSION.addKill(coins);
            return;
        }

        // Generic: detect any ghost kill message and try to extract coins from same line
        if (GHOST_KILL_PATTERN.matcher(raw).find()) {
            long coins = 0;
            Matcher cm = COIN_PATTERN.matcher(raw);
            if (cm.find()) {
                try { coins = Long.parseLong(cm.group(1).replace(",", "")); }
                catch (NumberFormatException ignored) {}
            }
            GhostKillTrackerClient.SESSION.addKill(coins);
        }
    }
}
