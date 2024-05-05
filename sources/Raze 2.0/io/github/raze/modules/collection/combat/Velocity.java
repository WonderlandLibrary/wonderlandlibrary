package io.github.raze.modules.collection.combat;

import io.github.raze.Raze;
import io.github.raze.events.collection.network.EventPacketReceive;
import io.github.raze.events.collection.network.EventPacketSend;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends AbstractModule {

    private final ArraySetting mode;
    private final NumberSetting horizontal, vertical;
    private final BooleanSetting explosionPacket;

    public Velocity() {
        super("Velocity", "Modifies your knockback.", ModuleCategory.COMBAT);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                mode = new ArraySetting(this, "Mode", "Packet", "Packet", "Cancel", "Reverse", "Vulcan", "MineMenClub", "Matrix Semi"),

                horizontal = new NumberSetting(this, "Horizontal", 0, 100, 0)
                        .setHidden(() -> (mode.compare("Cancel") || mode.compare("Vulcan") || mode.compare("MineMenClub") || mode.compare("Matrix Semi"))),

                vertical = new NumberSetting(this, "Vertical", 0, 100, 0)
                        .setHidden(() -> (mode.compare("Cancel") || mode.compare("Vulcan") || mode.compare("MineMenClub") || mode.compare("Matrix Semi"))),

                explosionPacket = new BooleanSetting(this, "Explosion Velocity", false)
                        .setHidden(() -> (mode.compare("Reverse") || mode.compare("Vulcan") || mode.compare("Matrix Semi")))

        );
    }

    @Listen
    public void onPacketSend(EventPacketSend event) {
        if (mode.compare("Vulcan")) {
            if (mc.thePlayer.hurtTime > 0 && event.getPacket() instanceof C0FPacketConfirmTransaction) {
                event.setCancelled(true);
            }
        }
    }

    @Listen
    public void onPacketReceived(EventPacketReceive event) {

        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();

            switch (mode.get()) {
                case "Packet":
                    packet.setMotionX((int) (((packet.getMotionX() * horizontal.get().doubleValue())) / 100.0));
                    packet.setMotionY((int) (((packet.getMotionY() * vertical.get().doubleValue())) / 100.0));
                    packet.setMotionZ((int) (((packet.getMotionZ() * horizontal.get().doubleValue())) / 100.0));
                    break;

                case "Cancel":
                case "Vulcan":
                case "MineMenClub":
                    event.setCancelled(true);
                    break;

                case "Reverse":
                    packet.setMotionX((int) ((packet.getMotionX() * -horizontal.get().doubleValue()) / 100.0));
                    packet.setMotionY((int) ((packet.getMotionY() * -vertical.get().doubleValue()) / 100.0));
                    packet.setMotionZ((int) ((packet.getMotionZ() * -horizontal.get().doubleValue()) / 100.0));
                    break;

                case "Matrix Semi":
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= 0.6;
                        mc.thePlayer.motionZ *= 0.6;
                    }
                    break;
            }
        }

        if (event.getPacket() instanceof S27PacketExplosion && explosionPacket.get()) {
            S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();

            switch (mode.get()) {
                case "Packet":
                    packet.setMotionX((int) (((packet.getMotionX() * horizontal.get().doubleValue())) / 100.0));
                    packet.setMotionY((int) (((packet.getMotionY() * vertical.get().doubleValue())) / 100.0));
                    packet.setMotionZ((int) (((packet.getMotionZ() * horizontal.get().doubleValue())) / 100.0));
                    break;

                case "Cancel":
                case "MineMenClub":
                    event.setCancelled(true);
                    break;
            }
        }
    }

}
