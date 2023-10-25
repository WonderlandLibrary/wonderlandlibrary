package club.async.module.impl.combat;

import club.async.event.impl.EventPacket;
import club.async.module.Category;
import club.async.module.Module;
import club.async.module.ModuleInfo;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import rip.hippo.lwjeb.annotation.Handler;

@ModuleInfo(name = "Velocity", description = "no more kb", category = Category.COMBAT)
public class Velocity extends Module {

    @Handler
    public void packet(EventPacket event) {
        if(event.getPacket() instanceof S12PacketEntityVelocity) {
            event.setCancelled(true);
        }
    }

}
