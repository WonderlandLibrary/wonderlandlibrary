package client.module.impl.combat.velocity;

import client.event.EventLink;
import client.event.Listener;
import client.event.impl.other.PacketReceiveEvent;
import client.module.impl.combat.Velocity;
import client.value.Mode;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class VanillaVelocity extends Mode<Velocity> {

    public VanillaVelocity(final String name, final Velocity parent) {
        super(name, parent);
    }

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = event -> {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) packet;
            if (wrapper.getEntityID() == mc.thePlayer.getEntityId()) event.setCancelled(true);
        }
    };
}
