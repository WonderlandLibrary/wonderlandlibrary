package com.initial.modules.impl.player;

import com.initial.modules.*;
import com.initial.events.*;
import com.initial.events.impl.*;
import net.minecraft.network.play.server.*;

public class NoRotate extends Module
{
    public NoRotate() {
        super("NoRotate", 0, Category.PLAYER);
    }
    
    @EventTarget
    public void onEvent(final EventUpdate event) {
        this.setDisplayName("No Rotate");
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)event.getPacket();
            if (this.mc.thePlayer != null && this.mc.thePlayer != null && this.mc.thePlayer.rotationYaw != -180.0f && this.mc.thePlayer.rotationPitch != 0.0f) {
                packet.yaw = this.mc.thePlayer.rotationYaw;
                packet.pitch = this.mc.thePlayer.rotationPitch;
            }
        }
    }
}
