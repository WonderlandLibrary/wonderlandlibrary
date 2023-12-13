package me.nyan.flush.module.impl.combat;

import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.ModeSetting;
import me.nyan.flush.module.settings.NumberSetting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", this, "Packet", "Packet");
    private final NumberSetting hVelocity = new NumberSetting("H Velocity", this, 0.0, 0.0, 100.0);
    private final NumberSetting vVelocity = new NumberSetting("V Velocity", this, 0.0, 0.0, 100.0);

    public Velocity() {
        super("Velocity", Category.COMBAT);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (mc.thePlayer == null) {
            return;
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            if (hVelocity.getValueFloat() == 0f && vVelocity.getValueFloat() == 0f) {
                e.cancel();
            }
            S12PacketEntityVelocity packet = e.getPacket();
            if (packet.getEntityID() == mc.thePlayer.getEntityId()) {
                packet.setMotionX((int) (packet.getMotionX() * (hVelocity.getValueFloat() / 100f)));
                packet.setMotionY((int) (packet.getMotionY() * (vVelocity.getValueFloat() / 100f)));
                packet.setMotionZ((int) (packet.getMotionZ() * (hVelocity.getValueFloat() / 100f)));
            }
        }
        if (e.getPacket() instanceof S27PacketExplosion) {
            if (hVelocity.getValueFloat() == 0f && vVelocity.getValueFloat() == 0f) {
                e.cancel();
            }
            S27PacketExplosion packet = e.getPacket();
            packet.setMotionX(packet.getMotionX() * (hVelocity.getValueFloat() / 100f));
            packet.setMotionY(packet.getMotionY() * (vVelocity.getValueFloat() / 100f));
            packet.setMotionZ(packet.getMotionZ() * (hVelocity.getValueFloat() / 100f));
        }
    }

    @Override
    public String getSuffix() {
        return mode.getValue();
    }
}