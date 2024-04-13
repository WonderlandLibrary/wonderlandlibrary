package dev.tenacity.module.impl.movement;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.packet.PacketReceiveEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.NumberSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class VelocityModule extends Module {

    private final NumberSetting horizontalVelocity = new NumberSetting("Horizontal Velocity", 0, 0, 100, 0.1),
            verticalVelocity = new NumberSetting("Vertical Velocity", 0, 0, 100, 0.1);


    public VelocityModule() {
        super("Velocity", "Removes your velocity movement aspect", ModuleCategory.MOVEMENT);
        initializeSettings(horizontalVelocity, verticalVelocity);
    }

    private final IEventListener<PacketReceiveEvent> onPacketReceiveEvent = event -> {


        if(event.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) event.getPacket();
            if(s12.getEntityID() == mc.thePlayer.getEntityId()) {
                if(horizontalVelocity.getCurrentValue() == 0 && verticalVelocity.getCurrentValue() == 0) {
                    event.cancel();
                    return;
                }
                s12.motionX *= (int) (horizontalVelocity.getCurrentValue() / 100);
                s12.motionY *= (int) (verticalVelocity.getCurrentValue() / 100);
                s12.motionZ *= (int) (horizontalVelocity.getCurrentValue() / 100);
            }
        }
        if(event.getPacket() instanceof S27PacketExplosion) {
            final S27PacketExplosion s27 = (S27PacketExplosion) event.getPacket();
            if(horizontalVelocity.getCurrentValue() == 0 && verticalVelocity.getCurrentValue() == 0) {
                event.cancel();
                return;
            }
            s27.motionX *= (float) (horizontalVelocity.getCurrentValue() / 100);
            s27.motionY *= (float) (verticalVelocity.getCurrentValue() / 100);
            s27.motionZ *= (float) (horizontalVelocity.getCurrentValue() / 100);
        }
    };
}
