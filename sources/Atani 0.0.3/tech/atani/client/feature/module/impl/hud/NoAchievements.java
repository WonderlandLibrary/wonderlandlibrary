package tech.atani.client.feature.module.impl.hud;

import net.minecraft.network.play.server.S37PacketStatistics;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;

@ModuleData(name = "NoAchievements", description = "Removes the annoying achievements.", category = Category.HUD)
public class NoAchievements extends Module {

    @Listen
    public void onPacketEvent(PacketEvent event) {
        if(mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if (event.getType() == PacketEvent.Type.INCOMING) {
            if(event.getPacket() instanceof S37PacketStatistics) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
