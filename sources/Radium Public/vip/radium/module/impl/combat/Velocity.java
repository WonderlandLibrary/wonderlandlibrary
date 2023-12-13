package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import vip.radium.event.impl.packet.PacketReceiveEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.ServerUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Velocity", category = ModuleCategory.COMBAT)
public final class Velocity extends Module {

    private final DoubleProperty horizontalPercentProperty = new DoubleProperty("Horizontal", 0, 0,
            100, 0.5, Representation.PERCENTAGE);
    private final DoubleProperty verticalPercentProperty = new DoubleProperty("Vertical", 0, 0,
            100, 0.5, Representation.PERCENTAGE);

    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = e -> {
        Packet<?> packet = e.getPacket();
        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;
            if (velocityPacket.getEntityID() == Wrapper.getPlayer().getEntityId()) {
                double verticalPerc = verticalPercentProperty.getValue();
                double horizontalPerc = horizontalPercentProperty.getValue();
                if (verticalPerc == 0 && horizontalPerc == 0) {
                    e.setCancelled();
                    return;
                }
                velocityPacket.motionX *= (horizontalPercentProperty.getValue() / 100);
                velocityPacket.motionY *= (verticalPercentProperty.getValue() / 100);
                velocityPacket.motionZ *= (horizontalPercentProperty.getValue() / 100);
            }
        } else if (packet instanceof S27PacketExplosion && ServerUtils.isOnHypixel()) {
            double verticalPerc = verticalPercentProperty.getValue();
            double horizontalPerc = horizontalPercentProperty.getValue();
            if (verticalPerc == 0 && horizontalPerc == 0) {
                e.setCancelled();
                return;
            }
            S27PacketExplosion packetExplosion = (S27PacketExplosion) packet;
            packetExplosion.motionX *= (horizontalPercentProperty.getValue() / 100);
            packetExplosion.motionY *= (verticalPercentProperty.getValue() / 100);
            packetExplosion.motionZ *= (horizontalPercentProperty.getValue() / 100);
        }
    };
}
