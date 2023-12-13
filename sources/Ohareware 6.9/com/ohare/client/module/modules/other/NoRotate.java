package com.ohare.client.module.modules.other;

import java.awt.Color;

import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;

import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

/**
 * made by oHare for oHareWare
 *
 * @since 7/19/2019
 **/
public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", Category.OTHER, new Color(0x9D9798).getRGB());
        setRenderlabel("No Rotate");
        setDescription("Cancel ncp rotation flags.");
    }
    
    @Subscribe
    public void handle(PacketEvent event) {
        if (!event.isSending() && event.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
            if (mc.thePlayer != null && mc.theWorld != null && mc.thePlayer.rotationYaw != -180 && mc.thePlayer.rotationPitch != 0) {
                packet.yaw = mc.thePlayer.rotationYaw;
                packet.pitch = mc.thePlayer.rotationPitch;
            }
        }
    }
}
