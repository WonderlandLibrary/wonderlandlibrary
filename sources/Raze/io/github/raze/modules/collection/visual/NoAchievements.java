package io.github.raze.modules.collection.visual;

import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import net.minecraft.network.play.server.S37PacketStatistics;

public class NoAchievements extends AbstractModule {

    public NoAchievements() {
        super("NoAchievements", "Cancels Achievements.", ModuleCategory.VISUAL);
    }

    @Listen
    public void onPacket(EventPacketReceive eventPacketReceive) {
        if (eventPacketReceive.getPacket() instanceof S37PacketStatistics)
            eventPacketReceive.setCancelled(true);
    }

}
