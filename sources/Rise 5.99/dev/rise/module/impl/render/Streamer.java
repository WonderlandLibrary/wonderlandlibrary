/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.render.Render2DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import net.minecraft.client.network.NetworkPlayerInfo;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.ArrayList;
import java.util.List;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "Streamer", description = "Hides everyones names", category = Category.RENDER)
public final class Streamer extends Module {

    public static final List<String> PLAYERS = new ArrayList<>();
    public static boolean enabled;

    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        enabled = true;
        if (mc.thePlayer.ticksExisted < 5) PLAYERS.clear();

        for (final NetworkPlayerInfo player : mc.getNetHandler().getPlayerInfoMap()) {
            if (player.getGameProfile().getName().length() < 3) continue;

            if (!PLAYERS.contains(player.getGameProfile().getName())) {
                PLAYERS.add(player.getGameProfile().getName());
            }
        }
    }

    @Override
    protected void onDisable() {
        PLAYERS.clear();
        enabled = false;
    }
}
