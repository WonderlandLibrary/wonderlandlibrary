package client.module.impl.combat.velocity;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.combat.Velocity;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class WatchdogVelocity extends Mode<Velocity> {

    public WatchdogVelocity(final String name, final Velocity parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;
            if (wrapper.getEntityID() == mc.thePlayer.getEntityId() && mc.thePlayer.onGround) {
                double d = 3.9D;
                double x = mc.thePlayer.motionX;
                double z = mc.thePlayer.motionZ;
                if (x < -d) x = -d;
                if (z < -d) z = -d;
                if (x > d) x = d;
                if (z > d) z = d;
                wrapper.setMotionX((short)(int)(x * 8000.0D));
                wrapper.setMotionZ((short)(int)(z * 8000.0D));
            } else event.setCancelled(true);
        }
    };
}
