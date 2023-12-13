/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.ModeSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

@ModuleInfo(name = "NoRot", description = "Prevents servers from modifying your rotation", category = Category.OTHER)
public final class NoRot extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Packet", "Delay", "Legit", "Edit", "Hypixel");
    private Float yaw, pitch;
    private Float startingYaw, startingPitch;

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook teleport = (S08PacketPlayerPosLook) packet;

            switch (mode.getMode()) {
                case "Vanilla":
                    yaw = teleport.yaw;
                    pitch = teleport.pitch;

                    teleport.yaw = mc.thePlayer.rotationYaw;
                    teleport.pitch = mc.thePlayer.rotationPitch;
                    break;

                case "Delay":
                    yaw = mc.thePlayer.rotationYaw;
                    pitch = mc.thePlayer.rotationPitch;
                    break;

                case "Legit":
                    yaw = teleport.getYaw();
                    pitch = teleport.getPitch();
                    startingYaw = mc.thePlayer.rotationYaw;
                    startingPitch = mc.thePlayer.rotationPitch;

                    teleport.yaw = mc.thePlayer.rotationYaw;
                    teleport.pitch = mc.thePlayer.rotationPitch;
                    break;
            }
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        switch (mode.getMode()) {
            case "Delay":
                if (yaw != null && pitch != null) {
                    mc.thePlayer.rotationYaw = yaw;
                    mc.thePlayer.rotationPitch = pitch;

                    yaw = null;
                    pitch = null;
                }
                break;

            case "Legit":
                if (yaw != null && pitch != null && startingYaw != null && startingPitch != null) {
                    if (startingYaw == event.getYaw() && startingPitch == event.getPitch()) {
                        event.setYaw(yaw);
                        event.setPitch(pitch);
                    } else {
                        startingPitch = startingYaw = yaw = pitch = null;
                    }
                }
                break;
        }
    }
}