/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.render;

import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import net.minecraft.network.play.server.S37PacketStatistics;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION})
@ModuleInfo(name = "NoAchievements", description = "Removes the achievement notification", category = Category.RENDER)
public final class NoAchievements extends Module {

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S37PacketStatistics)
            event.setCancelled(true);
    }
}
