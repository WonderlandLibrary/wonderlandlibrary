package me.nyan.flush.module.impl.misc;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.EventPacket;
import me.nyan.flush.event.impl.EventSkylightUpdate;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.notifications.Notification;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;

import java.util.Set;

public class AntiCrash extends Module {
    private final BooleanSetting noSkylightUpdates = new BooleanSetting("No Skylight Updates", this, false);

    public AntiCrash() {
        super("AntiCrash", Category.MISC);
    }

    @SubscribeEvent
    public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S2APacketParticles &&
                ((S2APacketParticles) e.getPacket()).getParticleCount() > 500) {
            e.cancel();
            preventedCrash();
        }

        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = e.getPacket();
            Set<S08PacketPlayerPosLook.EnumFlags> set = packet.func_179834_f();
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();
            if (set.contains(S08PacketPlayerPosLook.EnumFlags.X)) {
                x += mc.thePlayer.posX;
            }
            if (set.contains(S08PacketPlayerPosLook.EnumFlags.Y)) {
                y += mc.thePlayer.posY;
            }
            if (set.contains(S08PacketPlayerPosLook.EnumFlags.Z)) {
                z += mc.thePlayer.posZ;
            }

            if (!(Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z))) {
                e.cancel();
                preventedCrash();
                return;
            }

            double limit = 30e+6;
            if (Math.abs(x) > limit || Math.abs(y) > limit || Math.abs(z) > limit) {
                e.cancel();
                preventedCrash();
            }
        }

        if (e.getPacket() instanceof S27PacketExplosion) {
            S27PacketExplosion packet = e.getPacket();

            if (!(Double.isFinite(packet.getMotionX()) &&
                    Double.isFinite(packet.getMotionY()) &&
                    Double.isFinite(packet.getMotionZ()))) {
                e.cancel();
                preventedCrash();
                return;
            }

            double limit = 5e+6;
            if (Math.abs(packet.getMotionX()) > limit ||
                    Math.abs(packet.getMotionY()) > limit ||
                    Math.abs(packet.getMotionZ()) > limit) {
                e.cancel();
                preventedCrash();
            }
        }
    }

    @SubscribeEvent
    public void onSkylightUpdate(EventSkylightUpdate e) {
        if (noSkylightUpdates.getValue()) {
            e.cancel();
        }
    }

    private void preventedCrash() {
        flush.getNotificationManager().show(Notification.Type.WARNING, name, "Staff tried to crash Minecraft", 6000);
        Flush.LOGGER.warn("Staff tried to crash Minecraft");
    }
}
