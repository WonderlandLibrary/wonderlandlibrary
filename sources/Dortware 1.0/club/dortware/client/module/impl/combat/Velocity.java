package club.dortware.client.module.impl.combat;

import club.dortware.client.module.annotations.ModuleData;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.event.impl.PacketEvent;
import club.dortware.client.event.impl.enums.PacketDirection;
import club.dortware.client.module.Module;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@ModuleData(name = "Velocity", description = "Cancels all velocity packets", category = ModuleCategory.COMBAT)
public class Velocity extends Module {

    @Override
    public void setup() {

    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (event.getPacketDirection() == PacketDirection.INBOUND)
            event.setCancelled(event.getPacket() instanceof S12PacketEntityVelocity || event.getPacket() instanceof S27PacketExplosion);
    }
}