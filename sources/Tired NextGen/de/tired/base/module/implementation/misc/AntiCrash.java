package de.tired.base.module.implementation.misc;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.PacketEvent;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.render.notification.Notify;
import de.tired.util.render.notification.NotifyManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

@ModuleAnnotation(name = "AntiCrash", category = ModuleCategory.MISC)
public class AntiCrash extends Module {

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof S2APacketParticles) {
            final S2APacketParticles packetParticles = (S2APacketParticles) e.getPacket();
            if (packetParticles.getParticleCount() > 400 || Math.abs(packetParticles.getParticleSpeed()) > 400) {
                e.setCancelled(true);
                NotifyManager.sendClientMessage("Tired", "Server tried to crash client with particles", Notify.NotificationType.WARNING);
            }
        }
        if (e.getPacket() instanceof S27PacketExplosion) {
            final S27PacketExplosion ePacket = (S27PacketExplosion) e.getPacket();
            if (Math.abs(ePacket.getStrength()) > 99 || Math.abs(ePacket.getX()) > 99 || Math.abs(ePacket.getZ()) > 99 || Math.abs(ePacket.getY()) > 99) {
                e.setCancelled(true);
                NotifyManager.sendClientMessage("Tired", "Server tried to crash client with explosion", Notify.NotificationType.WARNING);
            }
        }
        if (e.getPacket() instanceof S2DPacketOpenWindow) {
            final S2DPacketOpenWindow packetOpenWindow = (S2DPacketOpenWindow) e.getPacket();
            if (Math.abs(packetOpenWindow.getSlotCount()) > 70) {
                NotifyManager.sendClientMessage("Tired", "Server tried to crash client with inventory", Notify.NotificationType.WARNING);
                e.setCancelled(true);
            }
        }

        if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) e.getPacket();

            if (Math.abs(packetPlayerPosition.x) > 3000 || Math.abs(packetPlayerPosition.y) > 3000 || Math.abs(packetPlayerPosition.z) > 3000) {
                NotifyManager.sendClientMessage("Tired", "Server tried to crash client with pos", Notify.NotificationType.WARNING);
                e.setCancelled(true);
            }

        }

    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
