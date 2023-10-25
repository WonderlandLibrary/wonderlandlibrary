package wtf.evolution.module.impl.Combat;

import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventPacket;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;

@ModuleInfo(name = "Velocity", type = Category.Combat)
public class Velocity extends Module {

    @EventTarget
    public void onReceive(EventPacket e) {
        if (e.getPacket() instanceof SPacketEntityVelocity) {
            SPacketEntityVelocity packet = (SPacketEntityVelocity) e.getPacket();
            if (packet.getEntityID() == mc.player.getEntityId()) {
                e.cancel();
            }
        }
        if (e.getPacket() instanceof SPacketExplosion) {
            e.cancel();
        }
    }

}
